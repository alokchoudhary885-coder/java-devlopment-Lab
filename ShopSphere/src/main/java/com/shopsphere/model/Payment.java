package com.shopsphere.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Represents a payment transaction executed for an order.
 * Tracks payment gateway transaction identifiers, payment methods, amounts, and settlement statuses.
 */
public class Payment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // Supported Payment Methods
    public static final String METHOD_CARD = "CARD";
    public static final String METHOD_UPI = "UPI";
    public static final String METHOD_NET_BANKING = "NET_BANKING";
    public static final String METHOD_COD = "COD";

    // Payment Transaction Statuses
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_FAILED = "FAILED";
    public static final String STATUS_PENDING = "PENDING";

    // Database mapped columns
    private int id;
    private int orderId;
    private String paymentMethod; // CARD, UPI, NET_BANKING, COD
    private BigDecimal amount;
    private String transactionId;
    private String status;        // SUCCESS, FAILED, PENDING
    private Timestamp paymentDate;

    /**
     * Default no-argument constructor.
     */
    public Payment() {
    }

    /**
     * Constructor without id and paymentDate (useful when initiating a transaction).
     *
     * @param orderId       Target order ID
     * @param paymentMethod Payment gateway or payment method used
     * @param amount        Transaction amount
     * @param transactionId External gateway reference or UUID
     * @param status        Payment processing status
     */
    public Payment(int orderId, String paymentMethod, BigDecimal amount, String transactionId, String status) {
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.transactionId = transactionId;
        this.status = status;
    }

    /**
     * Full parameterized constructor matching all database columns.
     *
     * @param id            Unique payment transaction ID
     * @param orderId       Associated order ID
     * @param paymentMethod Payment method (CARD, UPI, NET_BANKING, COD)
     * @param amount        Payment amount
     * @param transactionId Unique gateway transaction or tracking reference
     * @param status        Payment status (SUCCESS, FAILED, PENDING)
     * @param paymentDate   Timestamp when the payment occurred
     */
    public Payment(int id, int orderId, String paymentMethod, BigDecimal amount,
                   String transactionId, String status, Timestamp paymentDate) {
        this.id = id;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.transactionId = transactionId;
        this.status = status;
        this.paymentDate = paymentDate;
    }

    // --- Business / Status Helpers ---

    public boolean isSuccessful() {
        return STATUS_SUCCESS.equalsIgnoreCase(this.status);
    }

    public boolean isFailed() {
        return STATUS_FAILED.equalsIgnoreCase(this.status);
    }

    public boolean isPending() {
        return STATUS_PENDING.equalsIgnoreCase(this.status);
    }

    public boolean isCashOnDelivery() {
        return METHOD_COD.equalsIgnoreCase(this.paymentMethod);
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return id == payment.id && orderId == payment.orderId && Objects.equals(transactionId, payment.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, transactionId);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", amount=" + amount +
                ", transactionId='" + transactionId + '\'' +
                ", status='" + status + '\'' +
                ", paymentDate=" + paymentDate +
                '}';
    }
}
