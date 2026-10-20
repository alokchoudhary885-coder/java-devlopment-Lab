package com.shopsphere.model;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Represents a customer product review and rating in the ShopSphere store.
 */
public class Review implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // Database mapped columns
    private int id;
    private int userId;
    private int productId;
    private int rating; // 1 to 5 stars
    private String comment;
    private Timestamp createdAt;

    // Transient display field for customer name (populated via user join)
    private transient String userName;

    /**
     * Default no-argument constructor.
     */
    public Review() {
    }

    /**
     * Constructor for submitting a new customer review.
     *
     * @param userId    Author customer ID
     * @param productId Rated product ID
     * @param rating    Numerical star rating (1-5)
     * @param comment   Customer text feedback
     */
    public Review(int userId, int productId, int rating, String comment) {
        this.userId = userId;
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
    }

    /**
     * Full parameterized constructor matching all database columns.
     *
     * @param id        Unique review ID
     * @param userId    Author customer ID
     * @param productId Rated product ID
     * @param rating    Numerical star rating (1-5)
     * @param comment   Customer text feedback
     * @param createdAt Review submission timestamp
     */
    public Review(int id, int userId, int productId, int rating, String comment, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    /**
     * Comprehensive constructor including author's display name.
     *
     * @param id        Review ID
     * @param userId    Author user ID
     * @param productId Product ID
     * @param rating    Star rating
     * @param comment   Review text
     * @param createdAt Submission timestamp
     * @param userName  Author display name
     */
    public Review(int id, int userId, int productId, int rating, String comment, Timestamp createdAt, String userName) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.userName = userName;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return id == review.id && userId == review.userId && productId == review.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, productId);
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", createdAt=" + createdAt +
                ", userName='" + userName + '\'' +
                '}';
    }
}
