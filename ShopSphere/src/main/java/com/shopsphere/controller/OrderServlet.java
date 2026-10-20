package com.shopsphere.controller;

import com.shopsphere.model.Order;
import com.shopsphere.model.User;
import com.shopsphere.service.OrderService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Customer Order History and Order Receipt Details.
 * Handles:
 * GET /orders        - List of past orders placed by the authenticated customer.
 * GET /order-details - Full invoice breakdown with line items, address, and status.
 */
@WebServlet(name = "OrderServlet", urlPatterns = {"/orders", "/order-details"})
public class OrderServlet extends HttpServlet {

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

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            session = request.getSession(true);
            session.setAttribute("redirectAfterLogin", request.getRequestURI());
            session.setAttribute("loginNotice", "Please log in to view your orders.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String servletPath = request.getServletPath();

        if ("/order-details".equals(servletPath)) {
            // View single order details
            String orderIdParam = request.getParameter("id");
            if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/orders");
                return;
            }

            try {
                int orderId = Integer.parseInt(orderIdParam.trim());
                // Admin can view any order (pass userId 0), customer can only view own order
                int ownerCheckId = "ADMIN".equalsIgnoreCase(user.getRole()) ? 0 : user.getId();
                Order order = orderService.getOrderDetails(orderId, ownerCheckId);

                if (order == null) {
                    response.sendRedirect(request.getContextPath() + "/orders");
                    return;
                }

                request.setAttribute("order", order);
                request.setAttribute("pageTitle", "Order #" + order.getOrderNumber() + " | ShopSphere");
                request.getRequestDispatcher("/order-details.jsp").forward(request, response);

            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/orders");
            }

        } else {
            // List all orders for the current customer
            List<Order> orders = orderService.getUserOrders(user.getId());
            request.setAttribute("orders", orders);
            request.setAttribute("pageTitle", "My Orders (" + orders.size() + ") | ShopSphere");
            request.getRequestDispatcher("/orders.jsp").forward(request, response);
        }
    }
}
