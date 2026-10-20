package com.shopsphere.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a customer purchase order in the ShopSphere system.
 * Captures financial aggregates, lifecycle fulfillment states, and payment statuses.
 */
public class Order implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // Order Lifecycle Status Constants
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_PROCESSING = "PROCESSING";
    public static final String STATUS_SHIPPED = "SHIPPED";
    public static final String STATUS_DELIVERED = "DELIVERED";
    public static final String STATUS_CANCELLED = "CANCELLED";

    // Payment Status Constants
    public static final String PAYMENT_UNPAID = "UNPAID";
    public static final String PAYMENT_PAID = "PAID";
    public static final String PAYMENT_REFUNDED = "REFUNDED";

    // Database mapped columns
    private int id;
    private String orderNumber;
    private int userId;
    private int addressId;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private String status;        // PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    private String paymentStatus; // UNPAID, PAID, REFUNDED
    private Timestamp createdAt;

    // Transient fields for complex order views and invoices
    private transient List<OrderItem> orderItems = new ArrayList<>();
    private transient Address shippingAddress;
    private transient String customerName;
    private transient String customerEmail;

    /**
     * Default no-argument constructor.
     */
    public Order() {
    }

    /**
     * Constructor without id and createdAt (useful for initial order placement).
     *
     * @param orderNumber    Unique business order reference (e.g. ORD-20261013-XXXX)
     * @param userId         Identifier of customer
     * @param addressId      Identifier of delivery address
     * @param totalAmount    Gross subtotal before coupon discounts
     * @param discountAmount Coupon or campaign discount deducted
     * @param finalAmount    Net payable amount
     * @param status         Initial lifecycle status (e.g. PENDING)
     * @param paymentStatus  Initial payment status (e.g. UNPAID)
     */
    public Order(String orderNumber, int userId, int addressId,
                 BigDecimal totalAmount, BigDecimal discountAmount, BigDecimal finalAmount,
                 String status, String paymentStatus) {
        this.orderNumber = orderNumber;
        this.userId = userId;
        this.addressId = addressId;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.finalAmount = finalAmount;
        this.status = status;
        this.paymentStatus = paymentStatus;
    }

    /**
     * Full parameterized constructor matching all database columns.
     *
     * @param id             Order unique primary key
     * @param orderNumber    Unique business order reference
     * @param userId         Customer user ID
     * @param addressId      Delivery address ID
     * @param totalAmount    Gross total before discount
     * @param discountAmount Discount applied
     * @param finalAmount    Net final amount
     * @param status         Fulfillment status
     * @param paymentStatus  Payment status
     * @param createdAt      Order placement timestamp
     */
    public Order(int id, String orderNumber, int userId, int addressId,
                 BigDecimal totalAmount, BigDecimal discountAmount, BigDecimal finalAmount,
                 String status, String paymentStatus, Timestamp createdAt) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.userId = userId;
        this.addressId = addressId;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.finalAmount = finalAmount;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.createdAt = createdAt;
    }

    // --- Business / Lifecycle Helpers ---

    public boolean isPending() {
        return STATUS_PENDING.equalsIgnoreCase(this.status);
    }

    public boolean isProcessing() {
        return STATUS_PROCESSING.equalsIgnoreCase(this.status);
    }

    public boolean isShipped() {
        return STATUS_SHIPPED.equalsIgnoreCase(this.status);
    }

    public boolean isDelivered() {
        return STATUS_DELIVERED.equalsIgnoreCase(this.status);
    }

    public boolean isCancelled() {
        return STATUS_CANCELLED.equalsIgnoreCase(this.status);
    }

    public boolean isPaid() {
        return PAYMENT_PAID.equalsIgnoreCase(this.paymentStatus);
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && Objects.equals(orderNumber, order.orderNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderNumber);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderNumber='" + orderNumber + '\'' +
                ", userId=" + userId +
                ", addressId=" + addressId +
                ", totalAmount=" + totalAmount +
                ", discountAmount=" + discountAmount +
                ", finalAmount=" + finalAmount +
                ", status='" + status + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
