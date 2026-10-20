package com.shopsphere.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Represents an item in a user's active shopping cart.
 * Persists the association between a customer and a product with a designated quantity.
 * Contains transient fields for enriched cart view rendering.
 */
public class CartItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // Database mapped fields
    private int id;
    private int userId;
    private int productId;
    private int quantity;
    private Timestamp addedAt;

    // Transient fields for cart display (populated via SQL joins with products)
    private transient String productName;
    private transient BigDecimal productPrice;
    private transient BigDecimal productDiscount;
    private transient String productImageUrl;

    /**
     * Default no-argument constructor.
     */
    public CartItem() {
    }

    /**
     * Constructor for adding items to the cart.
     *
     * @param userId    Identifier of the user
     * @param productId Identifier of the product
     * @param quantity  Number of units
     */
    public CartItem(int userId, int productId, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    /**
     * Constructor matching all persistent database columns.
     *
     * @param id        Cart item ID
     * @param userId    User ID
     * @param productId Product ID
     * @param quantity  Quantity
     * @param addedAt   Timestamp when the item was added
     */
    public CartItem(int id, int userId, int productId, int quantity, Timestamp addedAt) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.addedAt = addedAt;
    }

    /**
     * Comprehensive constructor including transient product display attributes.
     *
     * @param id              Cart item ID
     * @param userId          User ID
     * @param productId       Product ID
     * @param quantity        Quantity
     * @param addedAt         Added timestamp
     * @param productName     Product title
     * @param productPrice    Original retail price
     * @param productDiscount Discount percentage applied
     * @param productImageUrl Product thumbnail image URL
     */
    public CartItem(int id, int userId, int productId, int quantity, Timestamp addedAt,
                    String productName, BigDecimal productPrice, BigDecimal productDiscount,
                    String productImageUrl) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.addedAt = addedAt;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDiscount = productDiscount;
        this.productImageUrl = productImageUrl;
    }

    // --- Business / Display Helper Methods ---

    /**
     * Computes the effective unit price after taking product discount into account.
     *
     * @return Discounted unit price, or productPrice if no discount is present
     */
    public BigDecimal getEffectiveUnitPrice() {
        if (productPrice == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        if (productDiscount == null || productDiscount.compareTo(BigDecimal.ZERO) <= 0) {
            return productPrice.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal discountFraction = productDiscount.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal discountAmount = productPrice.multiply(discountFraction).setScale(2, RoundingMode.HALF_UP);
        BigDecimal discounted = productPrice.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);

        return discounted.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP) : discounted;
    }

    /**
     * Computes the line item subtotal (effective unit price multiplied by quantity).
     *
     * @return Item line subtotal
     */
    public BigDecimal getSubtotal() {
        return getEffectiveUnitPrice().multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public Timestamp getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Timestamp addedAt) {
        this.addedAt = addedAt;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public BigDecimal getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(BigDecimal productDiscount) {
        this.productDiscount = productDiscount;
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
        CartItem cartItem = (CartItem) o;
        return id == cartItem.id && userId == cartItem.userId && productId == cartItem.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, productId);
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", addedAt=" + addedAt +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productDiscount=" + productDiscount +
                '}';
    }
}
