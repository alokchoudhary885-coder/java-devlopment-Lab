package com.shopsphere.dao;

import com.shopsphere.model.WishlistItem;
import com.shopsphere.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing customer {@link WishlistItem} entries.
 * <p>
 * Handles wishlist persistence, product associations, transient detail joins,
 * and checking whether specific products are saved to a user's wishlist.
 * </p>
 */
public class WishlistDAO {

    private static final String SQL_SELECT_JOIN =
            "SELECT w.id, w.user_id, w.product_id, w.added_at, " +
            "       p.name AS product_name, p.price AS product_price, p.image_url AS product_image_url " +
            "FROM wishlist w " +
            "INNER JOIN products p ON w.product_id = p.id";

    private static final String SQL_FIND_BY_USER_ID =
            SQL_SELECT_JOIN + " WHERE w.user_id = ? ORDER BY w.added_at DESC";

    private static final String SQL_ADD_ITEM =
            "INSERT INTO wishlist (user_id, product_id) " +
            "VALUES (?, ?) " +
            "ON DUPLICATE KEY UPDATE added_at = CURRENT_TIMESTAMP";

    private static final String SQL_REMOVE_ITEM =
            "DELETE FROM wishlist WHERE id = ?";

    private static final String SQL_REMOVE_BY_USER_AND_PRODUCT =
            "DELETE FROM wishlist WHERE user_id = ? AND product_id = ?";

    private static final String SQL_IS_IN_WISHLIST =
            "SELECT COUNT(*) FROM wishlist WHERE user_id = ? AND product_id = ?";

    private static final String SQL_GET_COUNT =
            "SELECT COUNT(*) FROM wishlist WHERE user_id = ?";

    /**
     * Retrieves all wishlist items saved by a user, joining with the products table
     * to populate transient display fields (productName, productPrice, productImageUrl).
     *
     * @param userId the user ID
     * @return list of populated {@link WishlistItem} entities; never null
     */
    public List<WishlistItem> findByUserId(int userId) {
        List<WishlistItem> items = new ArrayList<>();
        if (userId <= 0) {
            return items;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_USER_ID)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[WishlistDAO] Error in findByUserId(" + userId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return items;
    }

    /**
     * Adds a product to the user's wishlist. If the item is already present in the wishlist,
     * the timestamp is refreshed to avoid duplicate key exceptions.
     *
     * @param userId    the user ID
     * @param productId the product ID
     * @return {@code true} if added successfully or updated, {@code false} otherwise
     */
    public boolean addItem(int userId, int productId) {
        if (userId <= 0 || productId <= 0) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_ADD_ITEM)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[WishlistDAO] Error in addItem(User: " + userId + ", Product: " + productId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Removes an item from the wishlist by its primary key ID.
     *
     * @param wishlistId the wishlist entry ID
     * @return {@code true} if deleted successfully, {@code false} otherwise
     */
    public boolean removeItem(int wishlistId) {
        if (wishlistId <= 0) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_REMOVE_ITEM)) {

            ps.setInt(1, wishlistId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[WishlistDAO] Error in removeItem(Wishlist ID: " + wishlistId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Removes an item from the wishlist by the combination of user ID and product ID.
     *
     * @param userId    the user ID
     * @param productId the product ID
     * @return {@code true} if deleted successfully, {@code false} otherwise
     */
    public boolean removeByUserAndProduct(int userId, int productId) {
        if (userId <= 0 || productId <= 0) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_REMOVE_BY_USER_AND_PRODUCT)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[WishlistDAO] Error in removeByUserAndProduct(User: " + userId + ", Product: " + productId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Checks whether a specific product currently exists in the user's wishlist.
     *
     * @param userId    the user ID
     * @param productId the product ID
     * @return {@code true} if the item is in the wishlist, {@code false} otherwise
     */
    public boolean isInWishlist(int userId, int productId) {
        if (userId <= 0 || productId <= 0) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_IS_IN_WISHLIST)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("[WishlistDAO] Error in isInWishlist(User: " + userId + ", Product: " + productId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Returns the total number of products saved in the user's wishlist.
     *
     * @param userId the user ID
     * @return total count of saved wishlist items
     */
    public int getCount(int userId) {
        if (userId <= 0) {
            return 0;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_COUNT)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("[WishlistDAO] Error in getCount(User ID: " + userId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return 0;
    }

    /**
     * Maps a {@link ResultSet} row to a {@link WishlistItem} instance, including transient product details.
     *
     * @param rs the ResultSet cursor
     * @return populated {@link WishlistItem} entity
     * @throws SQLException if a database column error occurs
     */
    private WishlistItem mapRow(ResultSet rs) throws SQLException {
        WishlistItem item = new WishlistItem();
        item.setId(rs.getInt("id"));
        item.setUserId(rs.getInt("user_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setAddedAt(rs.getTimestamp("added_at"));

        // Transient fields from products JOIN
        item.setProductName(rs.getString("product_name"));
        item.setProductPrice(rs.getBigDecimal("product_price"));
        item.setProductImageUrl(rs.getString("product_image_url"));

        return item;
    }
}
