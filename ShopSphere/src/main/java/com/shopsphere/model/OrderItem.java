package com.shopsphere.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents an individual line item associated with an order.
 * Captures historical snapshot of unitPrice and subtotal at the moment of order placement.
 */
public class OrderItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // Database mapped columns
    private int id;
    private int orderId;
    private int productId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    // Transient fields for order item rendering (populated via SQL joins)
    private transient String productName;
    private transient String productImageUrl;

    /**
     * Default no-argument constructor.
     */
    public OrderItem() {
    }

    /**
     * Constructor without id (useful for persisting new order line items).
     *
     * @param orderId   ID of the parent order
     * @param productId ID of the purchased product
     * @param quantity  Purchased unit count
     * @param unitPrice Unit price locked in at time of order
     * @param subtotal  Line total (unitPrice * quantity)
     */
    public OrderItem(int orderId, int productId, int quantity, BigDecimal unitPrice, BigDecimal subtotal) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }

    /**
     * Full parameterized constructor matching all database columns.
     *
     * @param id        Unique order item ID
     * @param orderId   ID of the parent order
     * @param productId ID of the purchased product
     * @param quantity  Purchased unit count
     * @param unitPrice Unit price
     * @param subtotal  Line total
     */
    public OrderItem(int id, int orderId, int productId, int quantity, BigDecimal unitPrice, BigDecimal subtotal) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }

    /**
     * Comprehensive constructor including transient display details.
     *
     * @param id              Order item ID
     * @param orderId         Parent order ID
     * @param productId       Product ID
     * @param quantity        Quantity
     * @param unitPrice       Unit price
     * @param subtotal        Line item total
     * @param productName     Product name
     * @param productImageUrl Product image URL
     */
    public OrderItem(int id, int orderId, int productId, int quantity, BigDecimal unitPrice, BigDecimal subtotal,
                     String productName, String productImageUrl) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
        this.productName = productName;
        this.productImageUrl = productImageUrl;
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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return id == orderItem.id && orderId == orderItem.orderId && productId == orderItem.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, productId);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", subtotal=" + subtotal +
                ", productName='" + productName + '\'' +
                '}';
    }
}
