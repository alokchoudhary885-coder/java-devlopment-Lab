package com.shopsphere.network;

import com.shopsphere.model.Product;
import com.shopsphere.service.ProductService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Multi-threaded TCP Inventory Server using Java Sockets.
 * <p>
 * Academic Purpose: Fulfills RTU Advanced Java Syllabus (Unit: Networking & Sockets).
 * Demonstrates connection-oriented ServerSocket, stream handling (BufferedReader/PrintWriter),
 * multi-threaded client isolation via ExecutorService thread pool, and transactional catalog queries.
 * </p>
 */
public class InventorySocketServer {

    private final int port;
    private final ProductService productService;
    private final ExecutorService threadPool;
    private final AtomicBoolean running;
    private ServerSocket serverSocket;

    public InventorySocketServer(int port) {
        this.port = port;
        this.productService = new ProductService();
        this.threadPool = Executors.newCachedThreadPool();
        this.running = new AtomicBoolean(false);
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        running.set(true);
        System.out.println("=================================================");
        System.out.println(" ShopSphere Multi-threaded TCP Inventory Server");
        System.out.println(" Listening on Port: " + port);
        System.out.println(" RTU Advanced Java Networking Module Active");
        System.out.println("=================================================");

        // Graceful shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));

        while (running.get()) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[InventorySocketServer] Accepted connection from: "
                        + clientSocket.getRemoteSocketAddress());
                threadPool.submit(new ClientHandler(clientSocket, productService));
            } catch (IOException e) {
                if (!running.get()) {
                    System.out.println("[InventorySocketServer] Server socket closed.");
                    break;
                }
                System.err.println("[InventorySocketServer] Accept error: " + e.getMessage());
            }
        }
    }

    public void stop() {
        if (running.compareAndSet(true, false)) {
            System.out.println("[InventorySocketServer] Stopping server...");
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                System.err.println("[InventorySocketServer] Error closing server socket: " + e.getMessage());
            }
            threadPool.shutdown();
            System.out.println("[InventorySocketServer] Server stopped successfully.");
        }
    }

    /**
     * Runnable task handling a single connected socket client.
     */
    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private final ProductService productService;

        ClientHandler(Socket socket, ProductService productService) {
            this.socket = socket;
            this.productService = productService;
        }

        @Override
        public void run() {
            try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8)
            ) {
                writer.println(SocketProtocol.STATUS_OK + "Connected to ShopSphere Inventory Server. Send 'HELP' for commands.");

                String line;
                while ((line = reader.readLine()) != null) {
                    String trimmed = line.trim();
                    if (trimmed.isEmpty()) continue;

                    String response = processCommand(trimmed);
                    writer.println(response);

                    if (trimmed.equalsIgnoreCase(SocketProtocol.CMD_QUIT)) {
                        break;
                    }
                }
            } catch (IOException e) {
                System.err.println("[ClientHandler] Client communication error: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                    System.out.println("[ClientHandler] Client disconnected: " + socket.getRemoteSocketAddress());
                } catch (IOException ignored) {}
            }
        }

        private String processCommand(String commandLine) {
            String[] tokens = commandLine.split("\\s+");
            String command = tokens[0].toUpperCase();

            switch (command) {
                case SocketProtocol.CMD_PING -> {
                    return SocketProtocol.STATUS_OK + "PONG (Server Alive)";
                }

                case SocketProtocol.CMD_GET_STOCK -> {
                    if (tokens.length < 2) {
                        return SocketProtocol.STATUS_ERR + "Usage: GET_STOCK <productId>";
                    }
                    try {
                        int id = Integer.parseInt(tokens[1]);
                        Product p = productService.getProductById(id);
                        if (p != null) {
                            return SocketProtocol.STATUS_OK + "Product #" + id + " ('" + p.getName() + "') Stock = " + p.getStockQuantity();
                        } else {
                            return SocketProtocol.STATUS_ERR + "Product #" + id + " not found.";
                        }
                    } catch (NumberFormatException e) {
                        return SocketProtocol.STATUS_ERR + "Invalid product ID: " + tokens[1];
                    }
                }

                case SocketProtocol.CMD_GET_PRODUCT -> {
                    if (tokens.length < 2) {
                        return SocketProtocol.STATUS_ERR + "Usage: GET_PRODUCT <productId>";
                    }
                    try {
                        int id = Integer.parseInt(tokens[1]);
                        Product p = productService.getProductById(id);
                        if (p != null) {
                            return SocketProtocol.STATUS_OK + "ID=" + p.getId() + " | Name=" + p.getName()
                                    + " | Brand=" + p.getBrand() + " | Price=$" + p.getPrice()
                                    + " | Stock=" + p.getStockQuantity() + " | Status=" + (p.isStatus() ? "ACTIVE" : "DRAFT");
                        } else {
                            return SocketProtocol.STATUS_ERR + "Product #" + id + " not found.";
                        }
                    } catch (NumberFormatException e) {
                        return SocketProtocol.STATUS_ERR + "Invalid product ID: " + tokens[1];
                    }
                }

                case SocketProtocol.CMD_UPDATE_STOCK -> {
                    if (tokens.length < 3) {
                        return SocketProtocol.STATUS_ERR + "Usage: UPDATE_STOCK <productId> <newQuantity>";
                    }
                    try {
                        int id = Integer.parseInt(tokens[1]);
                        int qty = Integer.parseInt(tokens[2]);
                        if (qty < 0) {
                            return SocketProtocol.STATUS_ERR + "Quantity cannot be negative.";
                        }
                        boolean ok = productService.updateStock(id, qty);
                        if (ok) {
                            return SocketProtocol.STATUS_OK + "Stock updated for Product #" + id + " to " + qty + ".";
                        } else {
                            return SocketProtocol.STATUS_ERR + "Failed to update stock for Product #" + id + ".";
                        }
                    } catch (NumberFormatException e) {
                        return SocketProtocol.STATUS_ERR + "Invalid arguments: numbers required.";
                    }
                }

                case SocketProtocol.CMD_SEARCH -> {
                    if (tokens.length < 2) {
                        return SocketProtocol.STATUS_ERR + "Usage: SEARCH <keyword>";
                    }
                    String keyword = commandLine.substring(commandLine.indexOf(' ') + 1).trim();
                    List<Product> results = productService.searchProducts(keyword);
                    if (results.isEmpty()) {
                        return SocketProtocol.STATUS_OK + "No products matching '" + keyword + "'.";
                    }
                    StringBuilder sb = new StringBuilder(SocketProtocol.STATUS_OK + "Found " + results.size() + " products:\n");
                    for (Product p : results) {
                        sb.append("   - #").append(p.getId()).append(": ").append(p.getName())
                          .append(" ($").append(p.getPrice()).append(", Stock: ").append(p.getStockQuantity()).append(")\n");
                    }
                    return sb.toString().trim();
                }

                case SocketProtocol.CMD_QUIT -> {
                    return SocketProtocol.STATUS_BYE;
                }

                case "HELP" -> {
                    return """
                            Available Protocol Commands:
                              PING                          - Test server heartbeat
                              GET_STOCK <id>                - Query real-time inventory count
                              GET_PRODUCT <id>              - Retrieve full product details
                              UPDATE_STOCK <id> <quantity>  - Persistently update stock count
                              SEARCH <keyword>              - Search catalog products
                              QUIT                          - Disconnect from session
                            """;
                }

                default -> {
                    return SocketProtocol.STATUS_ERR + "Unknown command '" + command + "'. Type HELP for command list.";
                }
            }
        }
    }

    public static void main(String[] args) {
        int port = SocketProtocol.DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }

        InventorySocketServer server = new InventorySocketServer(port);
        try {
            server.start();
        } catch (IOException e) {
            System.err.println("[InventorySocketServer] Fatal error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
