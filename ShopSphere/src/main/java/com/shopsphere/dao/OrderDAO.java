package com.shopsphere.dao;

import com.shopsphere.model.Order;
import com.shopsphere.util.DBConnection;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Data Access Object (DAO) for managing {@link Order} lifecycle entities.
 * <p>
 * Handles order placements with transactional connection support, status modifications,
 * financial aggregate metrics, and standardized order tracking identifier generation.
 * </p>
 */
public class OrderDAO {

    private static final String SQL_SELECT_BASE =
            "SELECT o.id, o.order_number, o.user_id, o.address_id, o.total_amount, " +
            "       o.discount_amount, o.final_amount, o.status, o.payment_status, o.created_at, " +
            "       u.full_name AS customer_name, u.email AS customer_email " +
            "FROM orders o " +
            "LEFT JOIN users u ON o.user_id = u.id";

    private static final String SQL_FIND_BY_ID =
            SQL_SELECT_BASE + " WHERE o.id = ?";

    private static final String SQL_FIND_BY_ORDER_NUMBER =
            SQL_SELECT_BASE + " WHERE o.order_number = ?";

    private static final String SQL_FIND_BY_USER_ID =
            SQL_SELECT_BASE + " WHERE o.user_id = ? ORDER BY o.created_at DESC";

    private static final String SQL_FIND_ALL =
            SQL_SELECT_BASE + " ORDER BY o.created_at DESC";

    private static final String SQL_FIND_BY_STATUS =
            SQL_SELECT_BASE + " WHERE o.status = ? ORDER BY o.created_at DESC";

    private static final String SQL_INSERT =
            "INSERT INTO orders (order_number, user_id, address_id, total_amount, " +
            "                    discount_amount, final_amount, status, payment_status) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE_STATUS =
            "UPDATE orders SET status = ? WHERE id = ?";

    private static final String SQL_UPDATE_PAYMENT_STATUS =
            "UPDATE orders SET payment_status = ? WHERE id = ?";

    private static final String SQL_COUNT_ALL =
            "SELECT COUNT(*) FROM orders";

    private static final String SQL_COUNT_BY_STATUS =
            "SELECT COUNT(*) FROM orders WHERE status = ?";

    private static final String SQL_TOTAL_EARNINGS =
            "SELECT COALESCE(SUM(final_amount), 0.00) FROM orders " +
            "WHERE payment_status = 'PAID' AND status != 'CANCELLED'";

    /**
     * Finds an order by its unique database primary key ID.
     *
     * @param id the order ID
     * @return the matching {@link Order}, or {@code null} if not found or on error
     */
    public Order findById(int id) {
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
            System.err.println("[OrderDAO] Error in findById(" + id + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return null;
    }

    /**
     * Finds an order by its business order tracking number (e.g. 'ORD-20261013-1001').
     *
     * @param orderNumber the unique order reference string
     * @return matching {@link Order}, or {@code null} if not found or on error
     */
    public Order findByOrderNumber(String orderNumber) {
        if (orderNumber == null || orderNumber.trim().isEmpty()) {
            return null;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ORDER_NUMBER)) {

            ps.setString(1, orderNumber.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[OrderDAO] Error in findByOrderNumber('" + orderNumber + "'): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return null;
    }

    /**
     * Retrieves all orders placed by a specific user, ordered from newest to oldest.
     *
     * @param userId the user ID
     * @return list of {@link Order} records; never null
     */
    public List<Order> findByUserId(int userId) {
        List<Order> list = new ArrayList<>();
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
            System.err.println("[OrderDAO] Error in findByUserId(" + userId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return list;
    }

    /**
     * Retrieves all orders in the entire system, ordered from newest to oldest.
     * Primarily used for administration dashboards and order management consoles.
     *
     * @return list of all {@link Order} entities; never null
     */
    public List<Order> findAll() {
        List<Order> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[OrderDAO] Error in findAll: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return list;
    }

    /**
     * Retrieves all orders matching a designated lifecycle fulfillment status.
     *
     * @param status the order status (e.g. 'PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED')
     * @return list of matching {@link Order} entities; never null
     */
    public List<Order> findByStatus(String status) {
        List<Order> list = new ArrayList<>();
        if (status == null || status.trim().isEmpty()) {
            return list;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_STATUS)) {

            ps.setString(1, status.trim().toUpperCase());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[OrderDAO] Error in findByStatus('" + status + "'): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return list;
    }

    /**
     * Inserts an order using an externally managed {@link Connection} (essential for transactional boundaries).
     * Populates the generated primary key into the provided {@link Order} instance and returns it.
     * <p>
     * <b>Note:</b> Does NOT close the provided connection, allowing parent transactions to commit or rollback.
     * </p>
     *
     * @param order the order entity to insert
     * @param conn  the active transaction database connection
     * @return generated order ID, or -1 on failure
     * @throws SQLException if a database error occurs
     */
    public int insert(Order order, Connection conn) {
        if (order == null || conn == null) {
            return -1;
        }

        try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, order.getOrderNumber());
            ps.setInt(2, order.getUserId());
            ps.setInt(3, order.getAddressId());
            ps.setBigDecimal(4, order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO);
            ps.setBigDecimal(5, order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO);
            ps.setBigDecimal(6, order.getFinalAmount() != null ? order.getFinalAmount() : BigDecimal.ZERO);
            ps.setString(7, order.getStatus() != null ? order.getStatus().toUpperCase() : Order.STATUS_PENDING);
            ps.setString(8, order.getPaymentStatus() != null ? order.getPaymentStatus().toUpperCase() : Order.PAYMENT_UNPAID);

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        order.setId(generatedId);
                        return generatedId;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[OrderDAO] Error in insert(Order: " + order.getOrderNumber() + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return -1;
    }

    /**
     * Standalone insert helper using an auto-acquired connection (non-transactional).
     *
     * @param order the order entity to insert
     * @return generated order ID, or -1 on failure
     */
    public int insert(Order order) {
        if (order == null) {
            return -1;
        }
        try (Connection conn = DBConnection.getConnection()) {
            return insert(order, conn);
        } catch (SQLException e) {
            System.err.println("[OrderDAO] Error in standalone insert: " + e.getMessage());
            e.printStackTrace(System.err);
            return -1;
        }
    }

    /**
     * Updates the fulfillment lifecycle status of an existing order.
     *
     * @param orderId the order ID
     * @param status  the new status (PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED)
     * @return {@code true} if updated successfully, {@code false} otherwise
     */
    public boolean updateStatus(int orderId, String status) {
        if (orderId <= 0 || status == null || status.trim().isEmpty()) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_STATUS)) {

            ps.setString(1, status.trim().toUpperCase());
            ps.setInt(2, orderId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[OrderDAO] Error in updateStatus(Order ID: " + orderId + ", Status: " + status + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Updates the settlement payment status of an existing order.
     *
     * @param orderId       the order ID
     * @param paymentStatus the new payment status (UNPAID, PAID, REFUNDED)
     * @return {@code true} if updated successfully, {@code false} otherwise
     */
    public boolean updatePaymentStatus(int orderId, String paymentStatus) {
        if (orderId <= 0 || paymentStatus == null || paymentStatus.trim().isEmpty()) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_PAYMENT_STATUS)) {

            ps.setString(1, paymentStatus.trim().toUpperCase());
            ps.setInt(2, orderId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[OrderDAO] Error in updatePaymentStatus(Order ID: " + orderId + ", Status: " + paymentStatus + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Counts the total number of orders placed in the system.
     *
     * @return total order count
     */
    public int countAll() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_COUNT_ALL);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[OrderDAO] Error in countAll: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return 0;
    }

    /**
     * Counts the total number of orders matching a specific lifecycle status.
     *
     * @param status the status to count by
     * @return count of matching orders
     */
    public int countByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return 0;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_COUNT_BY_STATUS)) {

            ps.setString(1, status.trim().toUpperCase());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("[OrderDAO] Error in countByStatus('" + status + "'): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return 0;
    }

    /**
     * Computes cumulative net earnings from all orders where payment is PAID and order is not CANCELLED.
     *
     * @return total revenue earned as a {@link BigDecimal} rounded to 2 decimal places; never null
     */
    public BigDecimal getTotalEarnings() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_TOTAL_EARNINGS);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal(1);
                return total != null ? total.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            }
        } catch (SQLException e) {
            System.err.println("[OrderDAO] Error in getTotalEarnings: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Generates a unique, standardized order tracking reference in the format:
     * {@code ORD-yyyyMMdd-XXXX} (where XXXX is a random 4-digit sequence).
     * Example: {@code ORD-20261013-4819}
     *
     * @return formatted order number string
     */
    public String generateOrderNumber() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int randomPart = ThreadLocalRandom.current().nextInt(1000, 10000);
        return String.format("ORD-%s-%04d", datePart, randomPart);
    }

    /**
     * Maps a {@link ResultSet} row to an {@link Order} entity, including transient customer details.
     *
     * @param rs the ResultSet cursor
     * @return populated {@link Order} entity
     * @throws SQLException if a column index or reading error occurs
     */
    private Order mapRow(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setOrderNumber(rs.getString("order_number"));
        order.setUserId(rs.getInt("user_id"));
        order.setAddressId(rs.getInt("address_id"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setDiscountAmount(rs.getBigDecimal("discount_amount"));
        order.setFinalAmount(rs.getBigDecimal("final_amount"));
        order.setStatus(rs.getString("status"));
        order.setPaymentStatus(rs.getString("payment_status"));
        order.setCreatedAt(rs.getTimestamp("created_at"));

        // Transient fields populated via users JOIN
        try {
            order.setCustomerName(rs.getString("customer_name"));
            order.setCustomerEmail(rs.getString("customer_email"));
        } catch (SQLException ignored) {
            // Suppressed if customer columns were not requested in specific queries
        }

        return order;
    }
}
