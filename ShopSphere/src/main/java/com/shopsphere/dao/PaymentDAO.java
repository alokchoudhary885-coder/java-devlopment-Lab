package com.shopsphere.dao;

import com.shopsphere.model.Payment;
import com.shopsphere.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Data Access Object (DAO) for managing {@link Payment} transaction records.
 * <p>
 * Handles payment transaction logging with external connection support, status transitions,
 * lookup by order identifier or gateway transaction reference, and mock gateway transaction ID generation.
 * </p>
 */
public class PaymentDAO {

    private static final String SQL_SELECT_BASE =
            "SELECT id, order_id, payment_method, amount, transaction_id, status, payment_date " +
            "FROM payments";

    private static final String SQL_FIND_BY_ORDER_ID =
            SQL_SELECT_BASE + " WHERE order_id = ? ORDER BY id DESC LIMIT 1";

    private static final String SQL_FIND_BY_ID =
            SQL_SELECT_BASE + " WHERE id = ?";

    private static final String SQL_FIND_BY_TRANSACTION_ID =
            SQL_SELECT_BASE + " WHERE transaction_id = ?";

    private static final String SQL_INSERT =
            "INSERT INTO payments (order_id, payment_method, amount, transaction_id, status) " +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE_STATUS =
            "UPDATE payments SET status = ? WHERE id = ?";

    private static final String SQL_UPDATE_STATUS_BY_ORDER_ID =
            "UPDATE payments SET status = ? WHERE order_id = ?";

    /**
     * Finds the most recent payment record associated with a given order ID.
     *
     * @param orderId the order ID
     * @return the {@link Payment} record, or {@code null} if not found or on error
     */
    public Payment findByOrderId(int orderId) {
        if (orderId <= 0) {
            return null;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ORDER_ID)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[PaymentDAO] Error in findByOrderId(" + orderId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return null;
    }

    /**
     * Finds a payment record by its unique database primary key ID.
     *
     * @param id the payment primary key ID
     * @return matching {@link Payment}, or {@code null} if not found
     */
    public Payment findById(int id) {
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
            System.err.println("[PaymentDAO] Error in findById(" + id + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return null;
    }

    /**
     * Finds a payment record by its external gateway transaction identifier.
     *
     * @param transactionId the gateway transaction ID
     * @return matching {@link Payment}, or {@code null} if not found
     */
    public Payment findByTransactionId(String transactionId) {
        if (transactionId == null || transactionId.trim().isEmpty()) {
            return null;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_TRANSACTION_ID)) {

            ps.setString(1, transactionId.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[PaymentDAO] Error in findByTransactionId('" + transactionId + "'): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return null;
    }

    /**
     * Inserts a payment transaction record using the provided active database connection.
     * Essential for committing orders and payments atomically in the same database transaction.
     * <p>
     * <b>Note:</b> Does NOT close the provided connection.
     * </p>
     *
     * @param payment the payment record to persist
     * @param conn    the active transaction connection
     * @return {@code true} if inserted successfully, {@code false} otherwise
     */
    public boolean insert(Payment payment, Connection conn) {
        if (payment == null || conn == null) {
            return false;
        }

        try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, payment.getOrderId());
            ps.setString(2, payment.getPaymentMethod() != null ? payment.getPaymentMethod().toUpperCase() : Payment.METHOD_COD);
            ps.setBigDecimal(3, payment.getAmount() != null ? payment.getAmount() : BigDecimal.ZERO);
            ps.setString(4, payment.getTransactionId());
            ps.setString(5, payment.getStatus() != null ? payment.getStatus().toUpperCase() : Payment.STATUS_PENDING);

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        payment.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[PaymentDAO] Error in insert(Order ID: " + payment.getOrderId() + ", Txn: " + payment.getTransactionId() + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Standalone insert helper using an auto-acquired connection (non-transactional).
     *
     * @param payment the payment record to persist
     * @return {@code true} if inserted successfully, {@code false} otherwise
     */
    public boolean insert(Payment payment) {
        if (payment == null) {
            return false;
        }
        try (Connection conn = DBConnection.getConnection()) {
            return insert(payment, conn);
        } catch (SQLException e) {
            System.err.println("[PaymentDAO] Error in standalone insert: " + e.getMessage());
            e.printStackTrace(System.err);
            return false;
        }
    }

    /**
     * Updates the processing status of a payment record by its primary key ID.
     *
     * @param paymentId the payment primary key ID
     * @param status    the new status ('SUCCESS', 'FAILED', 'PENDING')
     * @return {@code true} if updated successfully, {@code false} otherwise
     */
    public boolean updateStatus(int paymentId, String status) {
        if (paymentId <= 0 || status == null || status.trim().isEmpty()) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_STATUS)) {

            ps.setString(1, status.trim().toUpperCase());
            ps.setInt(2, paymentId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[PaymentDAO] Error in updateStatus(Payment ID: " + paymentId + ", Status: " + status + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Updates the processing status of a payment record by associated order ID.
     *
     * @param orderId the order ID
     * @param status  the new status ('SUCCESS', 'FAILED', 'PENDING')
     * @return {@code true} if updated successfully, {@code false} otherwise
     */
    public boolean updateStatusByOrderId(int orderId, String status) {
        if (orderId <= 0 || status == null || status.trim().isEmpty()) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_STATUS_BY_ORDER_ID)) {

            ps.setString(1, status.trim().toUpperCase());
            ps.setInt(2, orderId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[PaymentDAO] Error in updateStatusByOrderId(Order ID: " + orderId + ", Status: " + status + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Generates a unique mock payment gateway transaction identifier in the format:
     * {@code TXN-MOCK-yyyyMMdd-XXXX} (where XXXX is a random 4-digit sequence).
     * Example: {@code TXN-MOCK-20261013-4029}
     *
     * @return formatted transaction tracking identifier string
     */
    public String generateTransactionId() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int randomPart = ThreadLocalRandom.current().nextInt(1000, 10000);
        return String.format("TXN-MOCK-%s-%04d", datePart, randomPart);
    }

    /**
     * Maps a {@link ResultSet} row to a {@link Payment} entity.
     *
     * @param rs the active ResultSet cursor
     * @return mapped {@link Payment} object
     * @throws SQLException if a column reading error occurs
     */
    private Payment mapRow(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setId(rs.getInt("id"));
        payment.setOrderId(rs.getInt("order_id"));
        payment.setPaymentMethod(rs.getString("payment_method"));
        payment.setAmount(rs.getBigDecimal("amount"));
        payment.setTransactionId(rs.getString("transaction_id"));
        payment.setStatus(rs.getString("status"));
        payment.setPaymentDate(rs.getTimestamp("payment_date"));
        return payment;
    }
}
