package com.shopsphere.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Represents a saved product item in a customer's wishlist.
 * Contains transient fields for displaying product details without redundant queries.
 */
public class WishlistItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // Database mapped fields
    private int id;
    private int userId;
    private int productId;
    private Timestamp addedAt;

    // Transient display-only fields (populated via SQL joins)
    private transient String productName;
    private transient BigDecimal productPrice;
    private transient String productImageUrl;

    /**
     * Default no-argument constructor.
     */
    public WishlistItem() {
    }

    /**
     * Constructor for creating a new wishlist entry.
     *
     * @param userId    Identifier of the user
     * @param productId Identifier of the saved product
     */
    public WishlistItem(int userId, int productId) {
        this.userId = userId;
        this.productId = productId;
    }

    /**
     * Constructor matching all persistent database columns.
     *
     * @param id        Wishlist item ID
     * @param userId    User ID
     * @param productId Product ID
     * @param addedAt   Timestamp when the product was saved to wishlist
     */
    public WishlistItem(int id, int userId, int productId, Timestamp addedAt) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.addedAt = addedAt;
    }

    /**
     * Comprehensive constructor including transient product display attributes.
     *
     * @param id              Wishlist item ID
     * @param userId          User ID
     * @param productId       Product ID
     * @param addedAt         Added timestamp
     * @param productName     Product name
     * @param productPrice    Current product price
     * @param productImageUrl Product image URL
     */
    public WishlistItem(int id, int userId, int productId, Timestamp addedAt,
                        String productName, BigDecimal productPrice, String productImageUrl) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.addedAt = addedAt;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
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
        WishlistItem that = (WishlistItem) o;
        return id == that.id && userId == that.userId && productId == that.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, productId);
    }

    @Override
    public String toString() {
        return "WishlistItem{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", addedAt=" + addedAt +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                '}';
    }
}
