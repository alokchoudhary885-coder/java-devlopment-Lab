package com.shopsphere.dao;

import com.shopsphere.model.Address;
import com.shopsphere.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for customer shipping and billing {@link Address} records.
 * <p>
 * Manages address persistence, retrieval by user, default address resolution, and
 * transactional default-flag updates.
 * </p>
 */
public class AddressDAO {

    private static final String SQL_SELECT_BASE =
            "SELECT id, user_id, street, city, state, postal_code, country, is_default FROM addresses";

    private static final String SQL_FIND_BY_ID =
            SQL_SELECT_BASE + " WHERE id = ?";

    private static final String SQL_FIND_BY_USER_ID =
            SQL_SELECT_BASE + " WHERE user_id = ? ORDER BY is_default DESC, id DESC";

    private static final String SQL_FIND_DEFAULT_BY_USER_ID =
            SQL_SELECT_BASE + " WHERE user_id = ? AND is_default = TRUE LIMIT 1";

    private static final String SQL_INSERT =
            "INSERT INTO addresses (user_id, street, city, state, postal_code, country, is_default) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE =
            "UPDATE addresses SET street = ?, city = ?, state = ?, postal_code = ?, country = ?, is_default = ? WHERE id = ? AND user_id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM addresses WHERE id = ?";

    private static final String SQL_UNSET_DEFAULTS =
            "UPDATE addresses SET is_default = FALSE WHERE user_id = ?";

    private static final String SQL_SET_DEFAULT =
            "UPDATE addresses SET is_default = TRUE WHERE id = ? AND user_id = ?";

    /**
     * Retrieves an address by its unique identifier.
     *
     * @param id the address ID
     * @return the {@link Address} entity, or {@code null} if not found or on error
     */
    public Address findById(int id) {
        if (id <= 0) {
            return null;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[AddressDAO] Error in findById(" + id + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return null;
    }

    /**
     * Retrieves all addresses belonging to a given user ID, sorted with default address first.
     *
     * @param userId the user ID
     * @return list of {@link Address} instances; never null
     */
    public List<Address> findByUserId(int userId) {
        List<Address> list = new ArrayList<>();
        if (userId <= 0) {
            return list;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_USER_ID)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[AddressDAO] Error in findByUserId(" + userId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return list;
    }

    /**
     * Retrieves the designated default address for a specific user.
     *
     * @param userId the user ID
     * @return the default {@link Address}, or {@code null} if none is designated
     */
    public Address findDefaultByUserId(int userId) {
        if (userId <= 0) {
            return null;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_DEFAULT_BY_USER_ID)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[AddressDAO] Error in findDefaultByUserId(" + userId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return null;
    }

    /**
     * Inserts a new address record. If marked as default, clears previous default flags
     * for the same user within a single atomic transaction.
     *
     * @param address the address entity to insert
     * @return {@code true} if inserted successfully, {@code false} otherwise
     */
    public boolean insert(Address address) {
        if (address == null || address.getUserId() <= 0) {
            return false;
        }

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // If new address is default, unset any existing default address for this user
            if (address.isDefault()) {
                try (PreparedStatement unsetPs = conn.prepareStatement(SQL_UNSET_DEFAULTS)) {
                    unsetPs.setInt(1, address.getUserId());
                    unsetPs.executeUpdate();
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, address.getUserId());
                ps.setString(2, address.getStreet());
                ps.setString(3, address.getCity());
                ps.setString(4, address.getState());
                ps.setString(5, address.getPostalCode());
                ps.setString(6, address.getCountry() != null && !address.getCountry().isBlank() ? address.getCountry() : "India");
                ps.setBoolean(7, address.isDefault());

                int affected = ps.executeUpdate();
                if (affected > 0) {
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            address.setId(keys.getInt(1));
                        }
                    }
                    conn.commit();
                    return true;
                }
            }
            conn.rollback();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rbEx) {
                    System.err.println("[AddressDAO] Rollback failed: " + rbEx.getMessage());
                }
            }
            System.err.println("[AddressDAO] Error in insert: " + e.getMessage());
            e.printStackTrace(System.err);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("[AddressDAO] Error closing connection: " + e.getMessage());
                }
            }
        }
        return false;
    }

    /**
     * Updates an existing address record. If marked as default, ensures other addresses
     * for this user are demoted from default status.
     *
     * @param address the address entity containing updated attributes
     * @return {@code true} if updated successfully, {@code false} otherwise
     */
    public boolean update(Address address) {
        if (address == null || address.getId() <= 0) {
            return false;
        }

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            if (address.isDefault()) {
                try (PreparedStatement unsetPs = conn.prepareStatement(SQL_UNSET_DEFAULTS)) {
                    unsetPs.setInt(1, address.getUserId());
                    unsetPs.executeUpdate();
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
                ps.setString(1, address.getStreet());
                ps.setString(2, address.getCity());
                ps.setString(3, address.getState());
                ps.setString(4, address.getPostalCode());
                ps.setString(5, address.getCountry() != null && !address.getCountry().isBlank() ? address.getCountry() : "India");
                ps.setBoolean(6, address.isDefault());
                ps.setInt(7, address.getId());
                ps.setInt(8, address.getUserId());

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    conn.commit();
                    return true;
                }
            }
            conn.rollback();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rbEx) {
                    System.err.println("[AddressDAO] Rollback failed: " + rbEx.getMessage());
                }
            }
            System.err.println("[AddressDAO] Error in update(ID: " + address.getId() + "): " + e.getMessage());
            e.printStackTrace(System.err);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("[AddressDAO] Error closing connection: " + e.getMessage());
                }
            }
        }
        return false;
    }

    /**
     * Deletes an address record by its primary key.
     *
     * @param id the address ID
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
            System.err.println("[AddressDAO] Error in delete(ID: " + id + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Sets a specific address as the default address for the user, resetting all other
     * addresses for that user to non-default in an atomic transaction.
     *
     * @param addressId the address ID to designate as default
     * @param userId    the owner user ID
     * @return {@code true} if successfully updated, {@code false} otherwise
     */
    public boolean setDefault(int addressId, int userId) {
        if (addressId <= 0 || userId <= 0) {
            return false;
        }

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Step 1: Clear current defaults for this user
            try (PreparedStatement unsetPs = conn.prepareStatement(SQL_UNSET_DEFAULTS)) {
                unsetPs.setInt(1, userId);
                unsetPs.executeUpdate();
            }

            // Step 2: Set the specified address as default
            try (PreparedStatement setPs = conn.prepareStatement(SQL_SET_DEFAULT)) {
                setPs.setInt(1, addressId);
                setPs.setInt(2, userId);
                int updated = setPs.executeUpdate();

                if (updated > 0) {
                    conn.commit();
                    return true;
                }
            }

            conn.rollback();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rbEx) {
                    System.err.println("[AddressDAO] Rollback failed: " + rbEx.getMessage());
                }
            }
            System.err.println("[AddressDAO] Error in setDefault(Address: " + addressId + ", User: " + userId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("[AddressDAO] Error closing connection: " + e.getMessage());
                }
            }
        }
        return false;
    }

    /**
     * Helper method to map a {@link ResultSet} row into an {@link Address} model instance.
     *
     * @param rs the ResultSet cursor
     * @return mapped {@link Address} instance
     * @throws SQLException if a column reading error occurs
     */
    private Address mapRow(ResultSet rs) throws SQLException {
        Address address = new Address();
        address.setId(rs.getInt("id"));
        address.setUserId(rs.getInt("user_id"));
        address.setStreet(rs.getString("street"));
        address.setCity(rs.getString("city"));
        address.setState(rs.getString("state"));
        address.setPostalCode(rs.getString("postal_code"));
        address.setCountry(rs.getString("country"));
        address.setDefault(rs.getBoolean("is_default"));
        return address;
    }
}
