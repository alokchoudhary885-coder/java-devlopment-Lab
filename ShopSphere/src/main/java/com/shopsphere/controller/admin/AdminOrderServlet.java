package com.shopsphere.controller.admin;

import com.shopsphere.model.Order;
import com.shopsphere.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Controller for Administrative Order Fulfillment & Status Workflow.
 * Manages order lifecycle transitions (PENDING -> PROCESSING -> SHIPPED -> DELIVERED),
 * payment reconciliations, and filtering by order status.
 * Mapped to /admin/orders
 */
@WebServlet(name = "AdminOrderServlet", urlPatterns = {"/admin/orders"})
public class AdminOrderServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private transient OrderService orderService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String statusFilter = request.getParameter("status");
        List<Order> orders;

        if (statusFilter != null && !statusFilter.trim().isEmpty() && !"ALL".equalsIgnoreCase(statusFilter)) {
            orders = orderService.getOrdersByStatus(statusFilter.trim().toUpperCase());
        } else {
            orders = orderService.getAllOrders();
        }

        // Fulfillment status counts for filter tabs
        int pendingCount = orderService.getOrderCountByStatus("PENDING");
        int processingCount = orderService.getOrderCountByStatus("PROCESSING");
        int shippedCount = orderService.getOrderCountByStatus("SHIPPED");
        int deliveredCount = orderService.getOrderCountByStatus("DELIVERED");
        int cancelledCount = orderService.getOrderCountByStatus("CANCELLED");
        int totalCount = orderService.getTotalOrderCount();
        BigDecimal totalRevenue = orderService.getTotalRevenue();

        request.setAttribute("orders", orders);
        request.setAttribute("statusFilter", statusFilter != null ? statusFilter.toUpperCase() : "ALL");
        request.setAttribute("pendingCount", pendingCount);
        request.setAttribute("processingCount", processingCount);
        request.setAttribute("shippedCount", shippedCount);
        request.setAttribute("deliveredCount", deliveredCount);
        request.setAttribute("cancelledCount", cancelledCount);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("activeNav", "orders");

        // Flash message handling
        HttpSession session = request.getSession(false);
        if (session != null) {
            String flashMessage = (String) session.getAttribute("flashMessage");
            String flashType = (String) session.getAttribute("flashType");
            if (flashMessage != null) {
                request.setAttribute("flashMessage", flashMessage);
                request.setAttribute("flashType", flashType != null ? flashType : "success");
                session.removeAttribute("flashMessage");
                session.removeAttribute("flashType");
            }
        }

        request.getRequestDispatcher("/admin/orders.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        HttpSession session = request.getSession(true);

        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));

            if ("updateStatus".equals(action)) {
                String newStatus = request.getParameter("status");
                boolean updated = orderService.updateOrderStatus(orderId, newStatus);
                if (updated) {
                    session.setAttribute("flashMessage", "Order #" + orderId + " updated to status " + newStatus + ".");
                    session.setAttribute("flashType", "success");
                } else {
                    session.setAttribute("flashMessage", "Failed to update order status.");
                    session.setAttribute("flashType", "danger");
                }
            } else if ("updatePayment".equals(action)) {
                String paymentStatus = request.getParameter("paymentStatus");
                boolean updated = orderService.updatePaymentStatus(orderId, paymentStatus);
                if (updated) {
                    session.setAttribute("flashMessage", "Order #" + orderId + " payment updated to " + paymentStatus + ".");
                    session.setAttribute("flashType", "success");
                } else {
                    session.setAttribute("flashMessage", "Failed to update payment status.");
                    session.setAttribute("flashType", "danger");
                }
            }
        } catch (Exception e) {
            session.setAttribute("flashMessage", "Error processing order update: " + e.getMessage());
            session.setAttribute("flashType", "danger");
        }

        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }
}
