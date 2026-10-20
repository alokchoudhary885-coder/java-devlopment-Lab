package com.shopsphere.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a promotional discount coupon in the ShopSphere store.
 * Supports percentage discounts with threshold rules (minimum order value) and maximum deduction caps.
 */
public class Coupon implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String code;
    private BigDecimal discountPercent;
    private BigDecimal maxDiscount;
    private BigDecimal minOrderAmount;
    private Date expiryDate;
    private boolean status; // true: active, false: inactive/disabled

    /**
     * Default no-argument constructor.
     */
    public Coupon() {
    }

    /**
     * Constructor without id (useful for creating and inserting new coupons).
     *
     * @param code            Unique promotional coupon code (e.g. FESTIVE20)
     * @param discountPercent Percentage reduction (0.00 - 100.00)
     * @param maxDiscount     Maximum ceiling discount amount (0.00 if unlimited)
     * @param minOrderAmount  Minimum cart total required to qualify for discount
     * @param expiryDate      Expiration calendar date
     * @param status          Active status flag
     */
    public Coupon(String code, BigDecimal discountPercent, BigDecimal maxDiscount,
                  BigDecimal minOrderAmount, Date expiryDate, boolean status) {
        this.code = code;
        this.discountPercent = discountPercent;
        this.maxDiscount = maxDiscount;
        this.minOrderAmount = minOrderAmount;
        this.expiryDate = expiryDate;
        this.status = status;
    }

    /**
     * Full parameterized constructor matching all database columns.
     *
     * @param id              Coupon unique identifier
     * @param code            Coupon code
     * @param discountPercent Percentage reduction
     * @param maxDiscount     Maximum discount cap
     * @param minOrderAmount  Minimum qualifying order amount
     * @param expiryDate      Expiry date
     * @param status          Active status flag
     */
    public Coupon(int id, String code, BigDecimal discountPercent, BigDecimal maxDiscount,
                  BigDecimal minOrderAmount, Date expiryDate, boolean status) {
        this.id = id;
        this.code = code;
        this.discountPercent = discountPercent;
        this.maxDiscount = maxDiscount;
        this.minOrderAmount = minOrderAmount;
        this.expiryDate = expiryDate;
        this.status = status;
    }

    // --- Business / Discount Calculation Helpers ---

    /**
     * Checks if the coupon date has expired against the current date.
     *
     * @return true if expired, false otherwise
     */
    public boolean isExpired() {
        if (expiryDate == null) {
            return false;
        }
        Date today = Date.valueOf(LocalDate.now());
        return expiryDate.before(today);
    }

    /**
     * Verifies if this coupon is active and valid for redemption today.
     *
     * @return true if active and not expired
     */
    public boolean isValid() {
        return status && !isExpired();
    }

    /**
     * Calculates the monetary discount amount applicable to a given order amount.
     *
     * @param orderAmount Subtotal of the cart/order
     * @return Calculated discount amount (bounded by maxDiscount if specified), or 0.00 if not eligible
     */
    public BigDecimal calculateDiscount(BigDecimal orderAmount) {
        if (!isValid() || orderAmount == null || orderAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        // Check minimum order amount requirement
        if (minOrderAmount != null && orderAmount.compareTo(minOrderAmount) < 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        if (discountPercent == null || discountPercent.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal percentageFraction = discountPercent.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal discount = orderAmount.multiply(percentageFraction).setScale(2, RoundingMode.HALF_UP);

        // Cap at maxDiscount if configured (> 0)
        if (maxDiscount != null && maxDiscount.compareTo(BigDecimal.ZERO) > 0 && discount.compareTo(maxDiscount) > 0) {
            discount = maxDiscount.setScale(2, RoundingMode.HALF_UP);
        }

        // Discount cannot exceed orderAmount
        if (discount.compareTo(orderAmount) > 0) {
            discount = orderAmount.setScale(2, RoundingMode.HALF_UP);
        }

        return discount;
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(BigDecimal maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public BigDecimal getMinOrderAmount() {
        return minOrderAmount;
    }

    public void setMinOrderAmount(BigDecimal minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coupon coupon = (Coupon) o;
        return id == coupon.id && Objects.equals(code, coupon.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code);
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", discountPercent=" + discountPercent +
                ", maxDiscount=" + maxDiscount +
                ", minOrderAmount=" + minOrderAmount +
                ", expiryDate=" + expiryDate +
                ", status=" + status +
                '}';
    }
}
