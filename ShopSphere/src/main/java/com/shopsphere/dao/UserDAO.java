package com.shopsphere.dao;

import com.shopsphere.model.User;
import com.shopsphere.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing {@link User} entities in the MySQL database.
 * <p>
 * Implements full CRUD operations, role filtering, status modifications, and metric counts.
 * All queries strictly adhere to parameterized {@link PreparedStatement} usage within
 * try-with-resources blocks.
 * </p>
 */
public class UserDAO {

    private static final String SQL_SELECT_BASE =
            "SELECT id, full_name, email, password_hash, phone, role, status, created_at FROM users";

    private static final String SQL_FIND_BY_ID =
            SQL_SELECT_BASE + " WHERE id = ?";

    private static final String SQL_FIND_BY_EMAIL =
            SQL_SELECT_BASE + " WHERE email = ?";

    private static final String SQL_FIND_ALL =
            SQL_SELECT_BASE + " ORDER BY id DESC";

    private static final String SQL_FIND_BY_ROLE =
            SQL_SELECT_BASE + " WHERE role = ? ORDER BY id DESC";

    private static final String SQL_INSERT =
            "INSERT INTO users (full_name, email, password_hash, phone, role, status) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE =
            "UPDATE users SET full_name = ?, email = ?, password_hash = ?, phone = ?, role = ?, status = ? WHERE id = ?";

    private static final String SQL_UPDATE_STATUS =
            "UPDATE users SET status = ? WHERE id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM users WHERE id = ?";

    private static final String SQL_COUNT_BY_ROLE =
            "SELECT COUNT(*) FROM users WHERE role = ?";

    /**
     * Finds a user by their unique primary key ID.
     *
     * @param id the user ID
     * @return the matching {@link User}, or {@code null} if not found or on error
     */
    public User findById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] Error in findById(" + id + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return null;
    }

    /**
     * Finds a user by their unique email address.
     *
     * @param email the user email address
     * @return the matching {@link User}, or {@code null} if not found or on error
     */
    public User findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_EMAIL)) {

            ps.setString(1, email.trim().toLowerCase());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] Error in findByEmail('" + email + "'): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return null;
    }

    /**
     * Retrieves all registered users ordered by ID descending.
     *
     * @return list of {@link User} entities; never null
     */
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] Error in findAll: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return list;
    }

    /**
     * Retrieves all users associated with a specific role (e.g. 'CUSTOMER', 'ADMIN').
     *
     * @param role the role name to filter by
     * @return list of matching {@link User} entities; never null
     */
    public List<User> findByRole(String role) {
        List<User> list = new ArrayList<>();
        if (role == null || role.trim().isEmpty()) {
            return list;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ROLE)) {

            ps.setString(1, role.trim().toUpperCase());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] Error in findByRole('" + role + "'): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return list;
    }

    /**
     * Inserts a new user record into the database.
     * Stores the {@code passwordHash} as-is (hashing is performed in the service layer).
     * Populates the generated primary key into the provided {@link User} object upon success.
     *
     * @param user the user entity to insert
     * @return {@code true} if insertion was successful, {@code false} otherwise
     */
    public boolean insert(User user) {
        if (user == null) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail() != null ? user.getEmail().trim().toLowerCase() : null);
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getRole() != null ? user.getRole().toUpperCase() : User.ROLE_CUSTOMER);
            ps.setString(6, user.getStatus() != null ? user.getStatus().toUpperCase() : User.STATUS_ACTIVE);

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] Error in insert(" + user.getEmail() + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Updates an existing user record.
     *
     * @param user the user entity containing updated values
     * @return {@code true} if the record was updated, {@code false} otherwise
     */
    public boolean update(User user) {
        if (user == null || user.getId() <= 0) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail() != null ? user.getEmail().trim().toLowerCase() : null);
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getRole() != null ? user.getRole().toUpperCase() : User.ROLE_CUSTOMER);
            ps.setString(6, user.getStatus() != null ? user.getStatus().toUpperCase() : User.STATUS_ACTIVE);
            ps.setInt(7, user.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UserDAO] Error in update(ID: " + user.getId() + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Updates the status ('ACTIVE', 'INACTIVE') of a user account.
     *
     * @param id     the user ID
     * @param status the new status value
     * @return {@code true} if updated successfully, {@code false} otherwise
     */
    public boolean updateStatus(int id, String status) {
        if (id <= 0 || status == null || status.trim().isEmpty()) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_STATUS)) {

            ps.setString(1, status.trim().toUpperCase());
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UserDAO] Error in updateStatus(ID: " + id + ", status: " + status + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Deletes a user record by primary key ID.
     *
     * @param id the user ID
     * @return {@code true} if deleted successfully, {@code false} otherwise
     */
    public boolean delete(int id) {
        if (id <= 0) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UserDAO] Error in delete(ID: " + id + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Counts the total number of users matching a given role.
     *
     * @param role the user role ('CUSTOMER', 'ADMIN')
     * @return total count of users with the given role
     */
    public int countByRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            return 0;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_COUNT_BY_ROLE)) {

            ps.setString(1, role.trim().toUpperCase());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] Error in countByRole('" + role + "'): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return 0;
    }

    /**
     * Helper method to map a single {@link ResultSet} row to a {@link User} model object.
     *
     * @param rs the active ResultSet cursor
     * @return mapped {@link User} object
     * @throws SQLException if a column index or name error occurs
     */
    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setPhone(rs.getString("phone"));
        user.setRole(rs.getString("role"));
        user.setStatus(rs.getString("status"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        return user;
    }
}
