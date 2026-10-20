package com.shopsphere.dao;

import com.shopsphere.model.CartItem;
import com.shopsphere.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing customer shopping {@link CartItem} entities.
 * <p>
 * Handles cart item persistence, quantity increments, joins with products for enriched
 * cart line-item displays, and cart cleanup upon checkout completion.
 * </p>
 */
public class CartDAO {

    private static final String SQL_SELECT_JOIN =
            "SELECT c.id, c.user_id, c.product_id, c.quantity, c.added_at, " +
            "       p.name AS product_name, p.price AS product_price, " +
            "       p.discount_percent AS product_discount, p.image_url AS product_image_url " +
            "FROM cart c " +
            "INNER JOIN products p ON c.product_id = p.id";

    private static final String SQL_FIND_BY_USER_ID =
            SQL_SELECT_JOIN + " WHERE c.user_id = ? ORDER BY c.added_at DESC";

    private static final String SQL_FIND_BY_USER_AND_PRODUCT =
            SQL_SELECT_JOIN + " WHERE c.user_id = ? AND c.product_id = ?";

    private static final String SQL_ADD_OR_INCREMENT =
            "INSERT INTO cart (user_id, product_id, quantity) " +
            "VALUES (?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)";

    private static final String SQL_UPDATE_QUANTITY =
            "UPDATE cart SET quantity = ? WHERE id = ?";

    private static final String SQL_REMOVE_ITEM =
            "DELETE FROM cart WHERE id = ?";

    private static final String SQL_CLEAR_CART =
            "DELETE FROM cart WHERE user_id = ?";

    private static final String SQL_GET_CART_COUNT =
            "SELECT COALESCE(SUM(quantity), 0) FROM cart WHERE user_id = ?";

    /**
     * Retrieves all cart items associated with a given user ID, joining with the products table
     * to populate transient display fields (productName, productPrice, productDiscount, productImageUrl).
     *
     * @param userId the user ID
     * @return list of populated {@link CartItem} records; never null
     */
    public List<CartItem> findByUserId(int userId) {
        List<CartItem> items = new ArrayList<>();
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
            System.err.println("[CartDAO] Error in findByUserId(" + userId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return items;
    }

    /**
     * Finds a single cart item by the user ID and product ID, joining with products for display attributes.
     *
     * @param userId    the user ID
     * @param productId the product ID
     * @return matching {@link CartItem}, or {@code null} if not found or on error
     */
    public CartItem findByUserAndProduct(int userId, int productId) {
        if (userId <= 0 || productId <= 0) {
            return null;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_USER_AND_PRODUCT)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[CartDAO] Error in findByUserAndProduct(User: " + userId + ", Product: " + productId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return null;
    }

    /**
     * Adds an item to the user's cart. If the product already exists in the cart for that user,
     * its quantity is incremented by the specified amount using MySQL {@code ON DUPLICATE KEY UPDATE}.
     *
     * @param userId    the user ID
     * @param productId the product ID
     * @param quantity  the quantity to add (must be &gt; 0)
     * @return {@code true} if added or updated successfully, {@code false} otherwise
     */
    public boolean addItem(int userId, int productId, int quantity) {
        if (userId <= 0 || productId <= 0 || quantity <= 0) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_ADD_OR_INCREMENT)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CartDAO] Error in addItem(User: " + userId + ", Product: " + productId + ", Qty: " + quantity + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Updates the designated quantity for an existing cart item by its cart primary key ID.
     *
     * @param cartId   the cart item ID
     * @param quantity the new desired quantity (must be &gt; 0)
     * @return {@code true} if updated successfully, {@code false} otherwise
     */
    public boolean updateQuantity(int cartId, int quantity) {
        if (cartId <= 0 || quantity <= 0) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_QUANTITY)) {

            ps.setInt(1, quantity);
            ps.setInt(2, cartId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CartDAO] Error in updateQuantity(Cart ID: " + cartId + ", Qty: " + quantity + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Removes an item from the cart by its cart primary key ID.
     *
     * @param cartId the cart item ID
     * @return {@code true} if deleted successfully, {@code false} otherwise
     */
    public boolean removeItem(int cartId) {
        if (cartId <= 0) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_REMOVE_ITEM)) {

            ps.setInt(1, cartId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CartDAO] Error in removeItem(Cart ID: " + cartId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Clears all items in the shopping cart for a specific user.
     * Frequently called upon successful order placement.
     *
     * @param userId the user ID
     * @return {@code true} if cart cleared successfully, {@code false} otherwise
     */
    public boolean clearCart(int userId) {
        if (userId <= 0) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_CLEAR_CART)) {

            ps.setInt(1, userId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("[CartDAO] Error in clearCart(User ID: " + userId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Computes the total quantity of items in the cart for a user (sum of all item quantities).
     *
     * @param userId the user ID
     * @return aggregate item quantity in the cart; 0 if empty or on error
     */
    public int getCartCount(int userId) {
        if (userId <= 0) {
            return 0;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_CART_COUNT)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("[CartDAO] Error in getCartCount(User ID: " + userId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return 0;
    }

    /**
     * Maps a {@link ResultSet} row to a {@link CartItem} instance, including transient product details.
     *
     * @param rs the active ResultSet cursor
     * @return mapped {@link CartItem} instance
     * @throws SQLException if a column reading error occurs
     */
    private CartItem mapRow(ResultSet rs) throws SQLException {
        CartItem item = new CartItem();
        item.setId(rs.getInt("id"));
        item.setUserId(rs.getInt("user_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setAddedAt(rs.getTimestamp("added_at"));

        // Transient fields from products JOIN
        item.setProductName(rs.getString("product_name"));
        item.setProductPrice(rs.getBigDecimal("product_price"));
        item.setProductDiscount(rs.getBigDecimal("product_discount"));
        item.setProductImageUrl(rs.getString("product_image_url"));

        return item;
    }
}
