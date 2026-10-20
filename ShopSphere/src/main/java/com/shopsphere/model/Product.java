package com.shopsphere.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Represents a product in the ShopSphere catalog.
 * Encapsulates pricing, discounts, inventory levels, ratings, and category classification.
 */
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private int categoryId;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private BigDecimal discountPercent;
    private int stockQuantity;
    private String imageUrl;
    private BigDecimal rating;
    private int reviewCount;
    private boolean status; // true: active/listed, false: inactive/delisted
    private Timestamp createdAt;

    // Transient display-only field (populated via SQL JOINs)
    private transient String categoryName;

    /**
     * Default no-argument constructor.
     */
    public Product() {
    }

    /**
     * Constructor without id and createdAt (convenient for creating new catalog products).
     *
     * @param categoryId      Associated category ID
     * @param name            Product title
     * @param brand           Brand/Manufacturer
     * @param description     Full product description
     * @param price           Base selling price (MSRP)
     * @param discountPercent Discount percentage (0-100)
     * @param stockQuantity   Available inventory quantity
     * @param imageUrl        Primary product image URL
     * @param rating          Average customer rating (0.00 - 5.00)
     * @param reviewCount     Total number of approved reviews
     * @param status          Product status flag (true = active)
     */
    public Product(int categoryId, String name, String brand, String description,
                   BigDecimal price, BigDecimal discountPercent, int stockQuantity,
                   String imageUrl, BigDecimal rating, int reviewCount, boolean status) {
        this.categoryId = categoryId;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.discountPercent = discountPercent;
        this.stockQuantity = stockQuantity;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.status = status;
    }

    /**
     * Full parameterized constructor matching the database schema.
     *
     * @param id              Unique product identifier
     * @param categoryId      Associated category ID
     * @param name            Product title
     * @param brand           Brand/Manufacturer
     * @param description     Full product description
     * @param price           Base selling price
     * @param discountPercent Discount percentage (0-100)
     * @param stockQuantity   Available inventory quantity
     * @param imageUrl        Primary product image URL
     * @param rating          Average customer rating
     * @param reviewCount     Total number of customer reviews
     * @param status          Product status flag (true = active)
     * @param createdAt       Record creation timestamp
     */
    public Product(int id, int categoryId, String name, String brand, String description,
                   BigDecimal price, BigDecimal discountPercent, int stockQuantity,
                   String imageUrl, BigDecimal rating, int reviewCount, boolean status,
                   Timestamp createdAt) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.discountPercent = discountPercent;
        this.stockQuantity = stockQuantity;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.status = status;
        this.createdAt = createdAt;
    }

    // --- Business / Helper Methods ---

    /**
     * Calculates the effective purchase price after applying the discount percentage.
     * Formula: price - (price * (discountPercent / 100)) rounded to 2 decimal places.
     *
     * @return Discounted price, or base price if no discount applies; never null or negative
     */
    public BigDecimal getDiscountedPrice() {
        if (price == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        if (discountPercent == null || discountPercent.compareTo(BigDecimal.ZERO) <= 0) {
            return price.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal discountMultiplier = discountPercent.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal discountAmount = price.multiply(discountMultiplier).setScale(2, RoundingMode.HALF_UP);
        BigDecimal finalPrice = price.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);

        return finalPrice.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP) : finalPrice;
    }

    /**
     * Computes the currency amount saved through the discount.
     *
     * @return Discount savings amount
     */
    public BigDecimal getSavingsAmount() {
        if (price == null || discountPercent == null || discountPercent.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return price.subtract(getDiscountedPrice()).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Checks if the product currently has an active discount greater than 0%.
     *
     * @return true if discountPercent > 0
     */
    public boolean hasDiscount() {
        return discountPercent != null && discountPercent.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Checks if this item has available inventory in stock.
     *
     * @return true if stockQuantity > 0
     */
    public boolean isInStock() {
        return stockQuantity > 0;
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    // EL-friendly getter
    public boolean getStatus() {
        return status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                ", discountPercent=" + discountPercent +
                ", stockQuantity=" + stockQuantity +
                ", rating=" + rating +
                ", reviewCount=" + reviewCount +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
