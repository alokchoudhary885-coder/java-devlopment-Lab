package com.shopsphere.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Thread-safe singleton Database Connection Manager for the ShopSphere application.
 * <p>
 * Supports both production MySQL and automatic embedded H2 (MySQL Mode) fallback.
 * When a local MySQL instance is not running or unconfigured, this manager automatically
 * initializes an embedded persistent H2 database with the full schema and seed catalog,
 * ensuring seamless, zero-config local execution and testing.
 * </p>
 */
public final class DBConnection {

    private static final String CONFIG_FILE = "db.properties";
    private static final Properties PROPERTIES = new Properties();

    private static volatile DBConnection instance;

    private static String dbDriver;
    private static String dbUrl;
    private static String dbUsername;
    private static String dbPassword;

    private static boolean isH2Fallback = false;
    private static volatile boolean h2Initialized = false;

    static {
        loadConfiguration();
        determineAndRegisterDriver();
    }

    /**
     * Private constructor to enforce singleton design pattern.
     */
    private DBConnection() {
        // Initialization handled in static block
    }

    /**
     * Retrieves the singleton instance of {@code DBConnection}.
     *
     * @return singleton {@link DBConnection} instance
     */
    public static DBConnection getInstance() {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }

    /**
     * Loads database configuration properties from the classpath.
     */
    private static void loadConfiguration() {
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                PROPERTIES.load(input);
            }
        } catch (IOException e) {
            System.err.println("[DBConnection] Warning loading " + CONFIG_FILE + ": " + e.getMessage());
        }

        dbDriver = PROPERTIES.getProperty("db.driver", "com.mysql.cj.jdbc.Driver").trim();
        dbUrl = PROPERTIES.getProperty("db.url", "jdbc:mysql://localhost:3306/shopsphere?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8").trim();
        dbUsername = PROPERTIES.getProperty("db.username", "root").trim();
        dbPassword = PROPERTIES.getProperty("db.password", "").trim();

        // Environment variables override properties for cloud production (Render, Railway, etc.)
        String envUrl = System.getenv("DB_URL");
        if (envUrl != null && !envUrl.trim().isEmpty()) {
            dbUrl = envUrl.trim();
        }
        String envUser = System.getenv("DB_USERNAME");
        if (envUser == null || envUser.trim().isEmpty()) {
            envUser = System.getenv("DB_USER");
        }
        if (envUser != null && !envUser.trim().isEmpty()) {
            dbUsername = envUser.trim();
        }
        String envPass = System.getenv("DB_PASSWORD");
        if (envPass != null) {
            dbPassword = envPass.trim();
        }
        String envDriver = System.getenv("DB_DRIVER");
        if (envDriver != null && !envDriver.trim().isEmpty()) {
            dbDriver = envDriver.trim();
        }
    }

    /**
     * Determines whether MySQL is reachable. If not, gracefully switches to embedded H2 (MySQL mode).
     */
    private static void determineAndRegisterDriver() {
        boolean mysqlAvailable = false;

        if (dbDriver.contains("mysql")) {
            String host = "localhost";
            int port = 3306;

            try {
                if (dbUrl.startsWith("jdbc:mysql://")) {
                    String clean = dbUrl.substring("jdbc:mysql://".length());
                    int slashIdx = clean.indexOf('/');
                    String hostPort = slashIdx > 0 ? clean.substring(0, slashIdx) : clean;
                    int colonIdx = hostPort.indexOf(':');
                    if (colonIdx > 0) {
                        host = hostPort.substring(0, colonIdx);
                        port = Integer.parseInt(hostPort.substring(colonIdx + 1));
                    } else if (!hostPort.isEmpty()) {
                        host = hostPort;
                    }
                }
                    int timeout = (host.equals("localhost") || host.equals("127.0.0.1")) ? 800 : 3000;
                    mysqlAvailable = isHostReachable(host, port, timeout);
            } catch (Exception e) {
                mysqlAvailable = false;
            }
        }

        if (mysqlAvailable) {
            try {
                Class.forName(dbDriver);
                System.out.println("[DBConnection] Connected to external MySQL database at " + dbUrl);
                return;
            } catch (ClassNotFoundException e) {
                System.err.println("[DBConnection] MySQL driver class not found: " + dbDriver + ". Falling back to H2.");
            }
        }

        // Activate embedded H2 fallback (MySQL Compatibility Mode)
        activateH2Fallback();
    }

    private static void activateH2Fallback() {
        isH2Fallback = true;
        dbDriver = "org.h2.Driver";

        String userHome = System.getProperty("user.home", ".");
        File dbDir = new File(userHome, ".shopsphere");
        if (!dbDir.exists()) {
            dbDir.mkdirs();
        }

        String dbPath = new File(dbDir, "shopsphere_db").getAbsolutePath().replace("\\", "/");
        dbUrl = "jdbc:h2:" + dbPath + ";MODE=MySQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH;AUTO_SERVER=TRUE";
        dbUsername = "sa";
        dbPassword = "";

        try {
            Class.forName(dbDriver);
            System.out.println("[DBConnection] Embedded H2 Database (MySQL Mode) initialized at: " + dbUrl);
        } catch (ClassNotFoundException e) {
            System.err.println("[DBConnection] Critical: H2 Driver not found: " + e.getMessage());
        }
    }

    private static boolean isHostReachable(String host, int port, int timeoutMs) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeoutMs);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Obtains a new database connection from the {@link DriverManager}.
     *
     * @return active {@link Connection} to the database
     * @throws SQLException if a database access error occurs or URL is invalid
     */
    public static Connection getConnection() throws SQLException {
        if (dbUrl == null || dbUrl.isEmpty()) {
            throw new SQLException("Database URL is unconfigured or null.");
        }

        Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

        if (isH2Fallback && !h2Initialized) {
            synchronized (DBConnection.class) {
                if (!h2Initialized) {
                    initH2SchemaAndSeed(conn);
                    h2Initialized = true;
                }
            }
        }

        return conn;
    }

    /**
     * Initializes the H2 schema and seed data from classpath resource {@code shopsphere.sql}.
     */
    private static void initH2SchemaAndSeed(Connection conn) {
        try (Statement checkStmt = conn.createStatement();
             ResultSet rs = checkStmt.executeQuery("SELECT COUNT(*) FROM information_schema.tables WHERE LOWER(table_name) = 'users'")) {
            if (rs.next() && rs.getInt(1) > 0) {
                // Check if users exist
                try (ResultSet countRs = checkStmt.executeQuery("SELECT COUNT(*) FROM users")) {
                    if (countRs.next() && countRs.getInt(1) > 0) {
                        return; // Already seeded
                    }
                }
            }
        } catch (SQLException ignored) {
            // Table doesn't exist, proceed to create
        }

        System.out.println("[DBConnection] Executing automatic database schema & seed population...");
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("shopsphere.sql")) {
            if (in == null) {
                System.err.println("[DBConnection] Seed file 'shopsphere.sql' not found on classpath!");
                return;
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                 Statement stmt = conn.createStatement()) {

                StringBuilder sql = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    String trimmed = line.trim();
                    if (trimmed.startsWith("--") || trimmed.startsWith("/*")) {
                        continue;
                    }
                    sql.append(line).append("\n");
                }

                for (String rawStatement : sql.toString().split(";")) {
                    String statement = rawStatement.trim();
                    if (statement.isEmpty()) continue;
                    String upper = statement.toUpperCase();
                    if (upper.startsWith("CREATE DATABASE") ||
                        upper.startsWith("USE ") ||
                        upper.startsWith("SET FOREIGN_KEY_CHECKS")) {
                        continue;
                    }

                    try {
                        stmt.execute(statement);
                    } catch (SQLException ex) {
                        // Ignore harmless drop table or duplicate notices during seed
                        if (!upper.startsWith("DROP TABLE")) {
                            System.err.println("[DBConnection] Notice on statement: " + ex.getMessage());
                        }
                    }
                }
                System.out.println("[DBConnection] Database schema and seed catalog successfully initialized!");
            }
        } catch (Exception e) {
            System.err.println("[DBConnection] Error initializing seed data: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    /**
     * Closes the provided database resources safely, suppressing any {@link SQLException}
     * while logging to standard error.
     *
     * @param conn the database connection to close, may be null
     * @param stmt the statement to close, may be null
     * @param rs   the result set to close, may be null
     */
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("[DBConnection] Warning closing ResultSet: " + e.getMessage());
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("[DBConnection] Warning closing Statement: " + e.getMessage());
            }
        }

        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("[DBConnection] Warning closing Connection: " + e.getMessage());
            }
        }
    }
}
