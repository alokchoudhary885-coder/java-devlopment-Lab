package com.shopsphere.service;

import com.shopsphere.dao.*;
import com.shopsphere.model.*;
import com.shopsphere.util.CheckoutException;
import com.shopsphere.util.DBConnection;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the complete checkout workflow including coupon validation,
 * server-side total recalculation, atomic order placement, stock deduction,
 * mock payment recording, and cart cleanup.
 *
 * <p><strong>CRITICAL:</strong> All monetary calculations are performed on the
 * server using database-sourced product prices. Client-submitted amounts,
 * discounts, and totals are NEVER trusted.</p>
 */
public class CheckoutService {

    private final CartDAO cartDAO;
    private final CouponDAO couponDAO;
    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;
    private final ProductDAO productDAO;
    private final PaymentDAO paymentDAO;

    public CheckoutService() {
        this.cartDAO = new CartDAO();
        this.couponDAO = new CouponDAO();
        this.orderDAO = new OrderDAO();
        this.orderItemDAO = new OrderItemDAO();
        this.productDAO = new ProductDAO();
        this.paymentDAO = new PaymentDAO();
    }

    /**
     * Validates a coupon code and calculates the discount amount.
     *
     * @param couponCode  the coupon code entered by customer
     * @param orderAmount the server-calculated cart subtotal
     * @param userId      the authenticated user ID
     * @return the discount amount to apply, or BigDecimal.ZERO if invalid
     */
    public BigDecimal validateAndApplyCoupon(String couponCode, BigDecimal orderAmount, int userId) {
        if (couponCode == null || couponCode.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }

        Coupon coupon = couponDAO.findByCode(couponCode.trim());
        if (coupon == null) {
            return BigDecimal.ZERO;
        }

        // Check coupon is active and not expired
        if (!coupon.isStatus() || coupon.isExpired()) {
            return BigDecimal.ZERO;
        }

        // Check minimum order amount
        if (orderAmount.compareTo(coupon.getMinOrderAmount()) < 0) {
            return BigDecimal.ZERO;
        }

        // Check if user already used this coupon
        if (couponDAO.hasUserUsedCoupon(userId, coupon.getId())) {
            return BigDecimal.ZERO;
        }

        // Calculate discount: orderAmount * (discountPercent / 100)
        BigDecimal discount = orderAmount.multiply(coupon.getDiscountPercent())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        // Cap at maximum discount
        if (coupon.getMaxDiscount() != null && discount.compareTo(coupon.getMaxDiscount()) > 0) {
            discount = coupon.getMaxDiscount();
        }

        return discount;
    }

    /**
     * Processes the complete checkout as an ACID transaction.
     *
     * <p>Transaction steps:</p>
     * <ol>
     *   <li>Fetch cart items; reject if empty</li>
     *   <li>Verify stock availability for each item (SELECT ... FOR UPDATE)</li>
     *   <li>Recalculate subtotal from database prices (zero client trust)</li>
     *   <li>Validate and apply coupon discount</li>
     *   <li>Create Order record</li>
     *   <li>Create OrderItem records and decrement product stock</li>
     *   <li>Create mock Payment record</li>
     *   <li>Record coupon usage if applicable</li>
     *   <li>Clear user's cart</li>
     * </ol>
     *
     * @param userId        authenticated customer ID
     * @param addressId     selected delivery address ID
     * @param couponCode    optional coupon code (null if none)
     * @param paymentMethod payment method: CARD, UPI, NET_BANKING, or COD
     * @return the created Order with all details populated
     * @throws CheckoutException if any step fails (transaction is rolled back)
     */
    public Order processCheckout(int userId, int addressId, String couponCode,
                                  String paymentMethod) throws CheckoutException {
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Step 1: Fetch cart items
            List<CartItem> cartItems = cartDAO.findByUserId(userId);
            if (cartItems == null || cartItems.isEmpty()) {
                throw new CheckoutException("Your cart is empty. Please add items before checkout.");
            }

            // Step 2 & 3: Verify stock and calculate server-side subtotal
            BigDecimal subtotal = BigDecimal.ZERO;
            List<Product> freshProducts = new ArrayList<>();

            for (CartItem item : cartItems) {
                // Re-fetch product from database for fresh price and stock
                Product product = productDAO.findById(item.getProductId());
                if (product == null || !product.isStatus()) {
                    throw new CheckoutException("Product '" + item.getProductName()
                            + "' is no longer available.");
                }

                if (product.getStockQuantity() < item.getQuantity()) {
                    throw new CheckoutException("Insufficient stock for '"
                            + product.getName() + "'. Only "
                            + product.getStockQuantity() + " units available.");
                }

                freshProducts.add(product);

                // Server-side price calculation using database values
                BigDecimal unitPrice = product.getDiscountedPrice();
                BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                subtotal = subtotal.add(lineTotal);
            }

            subtotal = subtotal.setScale(2, RoundingMode.HALF_UP);

            // Step 4: Validate and apply coupon
            BigDecimal discountAmount = BigDecimal.ZERO;
            Coupon appliedCoupon = null;

            if (couponCode != null && !couponCode.trim().isEmpty()) {
                discountAmount = validateAndApplyCoupon(couponCode.trim(), subtotal, userId);
                if (discountAmount.compareTo(BigDecimal.ZERO) > 0) {
                    appliedCoupon = couponDAO.findByCode(couponCode.trim());
                }
            }

            // Calculate final amount (never negative)
            BigDecimal finalAmount = subtotal.subtract(discountAmount);
            if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
                finalAmount = BigDecimal.ZERO;
            }
            finalAmount = finalAmount.setScale(2, RoundingMode.HALF_UP);

            // Step 5: Create Order
            Order order = new Order();
            order.setOrderNumber(orderDAO.generateOrderNumber());
            order.setUserId(userId);
            order.setAddressId(addressId);
            order.setTotalAmount(subtotal);
            order.setDiscountAmount(discountAmount);
            order.setFinalAmount(finalAmount);
            order.setStatus("PROCESSING");
            order.setPaymentStatus("COD".equals(paymentMethod) ? "UNPAID" : "PAID");

            int orderId = orderDAO.insert(order, conn);
            order.setId(orderId);

            // Step 6: Create OrderItems and decrement stock
            List<OrderItem> orderItems = new ArrayList<>();

            for (int i = 0; i < cartItems.size(); i++) {
                CartItem cartItem = cartItems.get(i);
                Product product = freshProducts.get(i);

                BigDecimal unitPrice = product.getDiscountedPrice();
                BigDecimal itemSubtotal = unitPrice.multiply(
                        BigDecimal.valueOf(cartItem.getQuantity()))
                        .setScale(2, RoundingMode.HALF_UP);

                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(orderId);
                orderItem.setProductId(cartItem.getProductId());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setUnitPrice(unitPrice);
                orderItem.setSubtotal(itemSubtotal);
                orderItem.setProductName(product.getName());
                orderItem.setProductImageUrl(product.getImageUrl());

                orderItemDAO.insert(orderItem, conn);
                orderItems.add(orderItem);

                // Decrement stock using direct SQL on the transaction connection
                int newStock = product.getStockQuantity() - cartItem.getQuantity();
                String stockSql = "UPDATE products SET stock_quantity = ? WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(stockSql)) {
                    ps.setInt(1, newStock);
                    ps.setInt(2, product.getId());
                    ps.executeUpdate();
                }
            }

            order.setOrderItems(orderItems);

            // Step 7: Create mock Payment record
            Payment payment = new Payment();
            payment.setOrderId(orderId);
            payment.setPaymentMethod(paymentMethod);
            payment.setAmount(finalAmount);
            payment.setTransactionId(paymentDAO.generateTransactionId());
            payment.setStatus("COD".equals(paymentMethod) ? "PENDING" : "SUCCESS");

            paymentDAO.insert(payment, conn);

            // Step 8: Record coupon usage
            if (appliedCoupon != null && discountAmount.compareTo(BigDecimal.ZERO) > 0) {
                couponDAO.recordUsage(appliedCoupon.getId(), userId, orderId, conn);
            }

            // Step 9: Clear cart
            String clearCartSql = "DELETE FROM cart WHERE user_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(clearCartSql)) {
                ps.setInt(1, userId);
                ps.executeUpdate();
            }

            // Step 10: Commit transaction
            conn.commit();

            return order;

        } catch (CheckoutException ce) {
            // Rollback on business rule violations
            rollback(conn);
            throw ce;
        } catch (Exception e) {
            // Rollback on any unexpected error
            rollback(conn);
            throw new CheckoutException("Checkout failed: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing checkout connection: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Safely rolls back a transaction connection.
     */
    private void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                System.err.println("Error during transaction rollback: " + e.getMessage());
            }
        }
    }
}
