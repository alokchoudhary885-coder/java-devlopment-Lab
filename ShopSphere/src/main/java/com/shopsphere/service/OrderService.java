package com.shopsphere.service;

import com.shopsphere.dao.AddressDAO;
import com.shopsphere.dao.OrderDAO;
import com.shopsphere.dao.OrderItemDAO;
import com.shopsphere.dao.PaymentDAO;
import com.shopsphere.model.Address;
import com.shopsphere.model.Order;
import com.shopsphere.model.OrderItem;
import com.shopsphere.model.Payment;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service layer for order history, order details retrieval,
 * admin order management, and dashboard revenue metrics.
 */
public class OrderService {

    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;
    private final PaymentDAO paymentDAO;
    private final AddressDAO addressDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.orderItemDAO = new OrderItemDAO();
        this.paymentDAO = new PaymentDAO();
        this.addressDAO = new AddressDAO();
    }

    // =========================================================================
    // Customer: Order History
    // =========================================================================

    /**
     * Retrieves all orders placed by a customer, sorted by newest first.
     */
    public List<Order> getUserOrders(int userId) {
        return orderDAO.findByUserId(userId);
    }

    /**
     * Retrieves full order details including items, payment, and shipping address.
     * Validates that the order belongs to the requesting user (or user is admin).
     *
     * @param orderId the order ID to retrieve
     * @param userId  the requesting user's ID (0 for admin access)
     * @return fully enriched Order, or null if not found or unauthorized
     */
    public Order getOrderDetails(int orderId, int userId) {
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            return null;
        }

        // Security: verify ownership (userId == 0 means admin access)
        if (userId > 0 && order.getUserId() != userId) {
            return null;
        }

        // Enrich with order items (with product details)
        List<OrderItem> items = orderItemDAO.findByOrderId(orderId);
        order.setOrderItems(items);

        // Enrich with payment info
        Payment payment = paymentDAO.findByOrderId(orderId);
        // Attach payment details (stored on order as transient if needed)

        // Enrich with shipping address
        Address address = addressDAO.findById(order.getAddressId());
        order.setShippingAddress(address);

        return order;
    }

    // =========================================================================
    // Admin: Order Management
    // =========================================================================

    /**
     * Retrieves a single order by ID (admin access, no ownership check).
     */
    public Order getOrderById(int orderId) {
        return getOrderDetails(orderId, 0);
    }

    /**
     * Retrieves an order by its unique order number.
     */
    public Order getOrderByNumber(String orderNumber) {
        Order order = orderDAO.findByOrderNumber(orderNumber);
        if (order != null) {
            order.setOrderItems(orderItemDAO.findByOrderId(order.getId()));
        }
        return order;
    }

    /**
     * Retrieves all orders in the system for admin dashboard.
     */
    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    /**
     * Retrieves orders filtered by fulfillment status.
     */
    public List<Order> getOrdersByStatus(String status) {
        return orderDAO.findByStatus(status);
    }

    /**
     * Updates the fulfillment status of an order.
     * Valid transitions: PENDING -> PROCESSING -> SHIPPED -> DELIVERED
     *                    Any status -> CANCELLED
     */
    public boolean updateOrderStatus(int orderId, String newStatus) {
        if (newStatus == null || newStatus.trim().isEmpty()) {
            return false;
        }

        // Validate allowed status values
        String upper = newStatus.toUpperCase().trim();
        if (!upper.equals("PENDING") && !upper.equals("PROCESSING")
                && !upper.equals("SHIPPED") && !upper.equals("DELIVERED")
                && !upper.equals("CANCELLED")) {
            return false;
        }

        return orderDAO.updateStatus(orderId, upper);
    }

    /**
     * Updates the payment status of an order.
     */
    public boolean updatePaymentStatus(int orderId, String newPaymentStatus) {
        if (newPaymentStatus == null || newPaymentStatus.trim().isEmpty()) {
            return false;
        }

        String upper = newPaymentStatus.toUpperCase().trim();
        if (!upper.equals("UNPAID") && !upper.equals("PAID") && !upper.equals("REFUNDED")) {
            return false;
        }

        return orderDAO.updatePaymentStatus(orderId, upper);
    }

    // =========================================================================
    // Dashboard Metrics
    // =========================================================================

    /**
     * Returns total count of all orders in the system.
     */
    public int getTotalOrderCount() {
        return orderDAO.countAll();
    }

    /**
     * Returns order count filtered by status (e.g., PENDING, PROCESSING).
     */
    public int getOrderCountByStatus(String status) {
        return orderDAO.countByStatus(status);
    }

    /**
     * Returns total revenue from all successfully paid, non-cancelled orders.
     */
    public BigDecimal getTotalRevenue() {
        BigDecimal revenue = orderDAO.getTotalEarnings();
        return revenue != null ? revenue : BigDecimal.ZERO;
    }
}
