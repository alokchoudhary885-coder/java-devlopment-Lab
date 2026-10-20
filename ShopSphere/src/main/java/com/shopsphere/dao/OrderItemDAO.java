package com.shopsphere.dao;

import com.shopsphere.model.OrderItem;
import com.shopsphere.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing line items ({@link OrderItem}) within customer orders.
 * <p>
 * Supports order-item retrieval with product display attribute joins, transactional single-item
 * insertions, and high-performance batch insertions for multi-item checkouts.
 * </p>
 */
public class OrderItemDAO {

    private static final String SQL_SELECT_BY_ORDER_ID =
            "SELECT oi.id, oi.order_id, oi.product_id, oi.quantity, oi.unit_price, oi.subtotal, " +
            "       p.name AS product_name, p.image_url AS product_image_url " +
            "FROM order_items oi " +
            "LEFT JOIN products p ON oi.product_id = p.id " +
            "WHERE oi.order_id = ? " +
            "ORDER BY oi.id ASC";

    private static final String SQL_INSERT =
            "INSERT INTO order_items (order_id, product_id, quantity, unit_price, subtotal) " +
            "VALUES (?, ?, ?, ?, ?)";

    /**
     * Retrieves all ordered items belonging to a specific order ID, joining with the products table
     * to populate transient fields (productName, productImageUrl).
     *
     * @param orderId the parent order ID
     * @return list of populated {@link OrderItem} entries; never null
     */
    public List<OrderItem> findByOrderId(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        if (orderId <= 0) {
            return items;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ORDER_ID)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[OrderItemDAO] Error in findByOrderId(" + orderId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return items;
    }

    /**
     * Inserts a single order item using the provided active database connection.
     * Crucial for executing within a parent order checkout transaction.
     * <p>
     * <b>Note:</b> Does NOT close the provided connection.
     * </p>
     *
     * @param item the order item to persist
     * @param conn the active transaction connection
     * @return {@code true} if inserted successfully, {@code false} otherwise
     */
    public boolean insert(OrderItem item, Connection conn) {
        if (item == null || conn == null) {
            return false;
        }

        try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setBigDecimal(4, item.getUnitPrice() != null ? item.getUnitPrice() : BigDecimal.ZERO);
            ps.setBigDecimal(5, item.getSubtotal() != null ? item.getSubtotal() : BigDecimal.ZERO);

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        item.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[OrderItemDAO] Error in insert(Order ID: " + item.getOrderId() + ", Product ID: " + item.getProductId() + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Standalone insert helper using an auto-acquired connection (non-transactional).
     *
     * @param item the order item to persist
     * @return {@code true} if inserted successfully, {@code false} otherwise
     */
    public boolean insert(OrderItem item) {
        if (item == null) {
            return false;
        }
        try (Connection conn = DBConnection.getConnection()) {
            return insert(item, conn);
        } catch (SQLException e) {
            System.err.println("[OrderItemDAO] Error in standalone insert: " + e.getMessage());
            e.printStackTrace(System.err);
            return false;
        }
    }

    /**
     * Inserts a collection of order items in a single JDBC batch operation using the provided transaction connection.
     * <p>
     * <b>Note:</b> Does NOT close the provided connection.
     * </p>
     *
     * @param items the list of order items to persist
     * @param conn  the active transaction connection
     * @return {@code true} if all items were batched and executed successfully, {@code false} otherwise
     */
    public boolean insertBatch(List<OrderItem> items, Connection conn) {
        if (items == null || items.isEmpty() || conn == null) {
            return false;
        }

        try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            for (OrderItem item : items) {
                ps.setInt(1, item.getOrderId());
                ps.setInt(2, item.getProductId());
                ps.setInt(3, item.getQuantity());
                ps.setBigDecimal(4, item.getUnitPrice() != null ? item.getUnitPrice() : BigDecimal.ZERO);
                ps.setBigDecimal(5, item.getSubtotal() != null ? item.getSubtotal() : BigDecimal.ZERO);
                ps.addBatch();
            }

            int[] batchResults = ps.executeBatch();
            for (int result : batchResults) {
                if (result == Statement.EXECUTE_FAILED) {
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            System.err.println("[OrderItemDAO] Error in insertBatch: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Maps a {@link ResultSet} row to an {@link OrderItem} instance, including transient product details.
     *
     * @param rs the active ResultSet cursor
     * @return mapped {@link OrderItem} instance
     * @throws SQLException if a column index or read error occurs
     */
    private OrderItem mapRow(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setId(rs.getInt("id"));
        item.setOrderId(rs.getInt("order_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));
        item.setSubtotal(rs.getBigDecimal("subtotal"));

        // Transient fields populated via products JOIN
        try {
            item.setProductName(rs.getString("product_name"));
            item.setProductImageUrl(rs.getString("product_image_url"));
        } catch (SQLException ignored) {
            // Suppressed if product columns were not queried
        }

        return item;
    }
}
