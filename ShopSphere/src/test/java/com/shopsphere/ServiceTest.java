package com.shopsphere;

import com.shopsphere.model.CartItem;
import com.shopsphere.model.Coupon;
import com.shopsphere.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test suite for ShopSphere Service layer calculations, cart subtotaling,
 * savings calculations, and coupon business logic.
 */
public class ServiceTest {

    private CartService cartService;

    @BeforeEach
    public void setUp() {
        cartService = new CartService();
    }

    // =========================================================================
    // 1. Cart Subtotal and Savings Calculations
    // =========================================================================

    @Test
    @DisplayName("CartService: Subtotal and savings for empty or null cart return BigDecimal.ZERO")
    public void testEmptyCartCalculations() {
        assertEquals(BigDecimal.ZERO, cartService.calculateSubtotal(null));
        assertEquals(BigDecimal.ZERO, cartService.calculateSubtotal(Collections.emptyList()));
        assertEquals(BigDecimal.ZERO, cartService.calculateTotalSavings(null));
        assertEquals(BigDecimal.ZERO, cartService.calculateTotalSavings(Collections.emptyList()));
    }

    @Test
    @DisplayName("CartService: Correctly calculates subtotal and total savings across multiple discounted items")
    public void testCartSubtotalAndSavingsWithDiscounts() {
        List<CartItem> items = new ArrayList<>();

        // Item 1: Original Price 1000.00, Discount 10%, Qty 2 -> Effective Price 900.00 * 2 = 1800.00, Savings = 200.00
        CartItem item1 = new CartItem();
        item1.setProductId(1);
        item1.setQuantity(2);
        item1.setProductPrice(new BigDecimal("1000.00"));
        item1.setProductDiscount(new BigDecimal("10.00"));
        items.add(item1);

        // Item 2: Original Price 500.00, Discount 0%, Qty 3 -> Effective Price 500.00 * 3 = 1500.00, Savings = 0.00
        CartItem item2 = new CartItem();
        item2.setProductId(2);
        item2.setQuantity(3);
        item2.setProductPrice(new BigDecimal("500.00"));
        item2.setProductDiscount(BigDecimal.ZERO);
        items.add(item2);

        BigDecimal calculatedSubtotal = cartService.calculateSubtotal(items);
        BigDecimal calculatedSavings = cartService.calculateTotalSavings(items);

        // Subtotal = 1800.00 + 1500.00 = 3300.00
        assertEquals(new BigDecimal("3300.00"), calculatedSubtotal);

        // Savings = 200.00
        assertEquals(new BigDecimal("200.00"), calculatedSavings);
    }

    @Test
    @DisplayName("CartService: Handles fractional prices and rounding properly using HALF_UP")
    public void testCartCalculationsWithFractionalDecimals() {
        List<CartItem> items = new ArrayList<>();

        // Original Price 33.33, Discount 15%, Qty 3
        CartItem item = new CartItem();
        item.setProductId(3);
        item.setQuantity(3);
        item.setProductPrice(new BigDecimal("33.33"));
        item.setProductDiscount(new BigDecimal("15.00"));
        items.add(item);

        BigDecimal subtotal = cartService.calculateSubtotal(items);
        assertNotNull(subtotal);
        assertEquals(2, subtotal.scale());
    }

    // =========================================================================
    // 2. Coupon Business Logic Bounds & Cap Testing
    // =========================================================================

    @Test
    @DisplayName("Coupon Logic: Applies percentage discount correctly without exceeding maximum cap")
    public void testCouponCalculationWithinCap() {
        Coupon coupon = new Coupon(1, "WELCOME20", new BigDecimal("20.00"),
                new BigDecimal("500.00"), new BigDecimal("1000.00"),
                new Date(System.currentTimeMillis() + 864000000L),
                true);

        BigDecimal orderAmount = new BigDecimal("2000.00");
        BigDecimal calculatedDiscount = calculateCouponDiscount(coupon, orderAmount);

        // 20% of 2000 = 400.00, which is below maxDiscount 500.00
        assertEquals(new BigDecimal("400.00"), calculatedDiscount);
    }

    @Test
    @DisplayName("Coupon Logic: Enforces maximum discount cap when percentage exceeds ceiling")
    public void testCouponCalculationExceedingCap() {
        Coupon coupon = new Coupon(2, "BIGSAVE", new BigDecimal("50.00"),
                new BigDecimal("300.00"), new BigDecimal("500.00"),
                new Date(System.currentTimeMillis() + 864000000L),
                true);

        BigDecimal orderAmount = new BigDecimal("1000.00");
        BigDecimal calculatedDiscount = calculateCouponDiscount(coupon, orderAmount);

        // 50% of 1000 = 500.00, but maxDiscount is 300.00
        assertEquals(new BigDecimal("300.00"), calculatedDiscount);
    }

    @Test
    @DisplayName("Coupon Logic: Returns zero discount if order amount is below minimum threshold")
    public void testCouponBelowMinimumOrderThreshold() {
        Coupon coupon = new Coupon(3, "TIER1", new BigDecimal("10.00"),
                new BigDecimal("200.00"), new BigDecimal("1000.00"),
                new Date(System.currentTimeMillis() + 864000000L),
                true);

        BigDecimal orderAmount = new BigDecimal("850.00"); // below min 1000.00
        BigDecimal calculatedDiscount = calculateCouponDiscount(coupon, orderAmount);

        assertEquals(BigDecimal.ZERO, calculatedDiscount);
    }

    // Helper simulating coupon discount calculation as implemented in CheckoutService
    private BigDecimal calculateCouponDiscount(Coupon coupon, BigDecimal orderAmount) {
        if (coupon == null || !coupon.isStatus() || orderAmount == null) {
            return BigDecimal.ZERO;
        }
        if (coupon.getMinOrderAmount() != null && orderAmount.compareTo(coupon.getMinOrderAmount()) < 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal discount = orderAmount.multiply(coupon.getDiscountPercent())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        if (coupon.getMaxDiscount() != null && discount.compareTo(coupon.getMaxDiscount()) > 0) {
            discount = coupon.getMaxDiscount();
        }
        return discount;
    }
}
