package com.shopsphere.controller;

import com.google.gson.JsonObject;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.User;
import com.shopsphere.service.CartService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

/**
 * Controller for Shopping Cart management.
 * Handles:
 * GET  /cart  - Displays cart items, subtotal, and savings summary.
 * POST /cart  - Actions: 'add', 'update', 'remove', 'clear'.
 * Supports both asynchronous AJAX requests (JSON response) and standard form submissions.
 */
@WebServlet(name = "CartServlet", urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private transient CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.cartService = new CartService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        // If not logged in, redirect to login with return target
        if (user == null) {
            session = request.getSession(true);
            session.setAttribute("redirectAfterLogin", request.getContextPath() + "/cart");
            session.setAttribute("loginNotice", "Please log in to view your shopping cart.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 1. Fetch Cart Items
        List<CartItem> cartItems = cartService.getCart(user.getId());

        // 2. Server-Side Calculations (Zero client trust)
        BigDecimal subtotal = cartService.calculateSubtotal(cartItems);
        BigDecimal savings = cartService.calculateTotalSavings(cartItems);
        int cartCount = cartService.getCartCount(user.getId());

        // 3. Update Session Cart Count
        session.setAttribute("cartCount", cartCount);

        // 4. Set Request Attributes
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("subtotal", subtotal);
        request.setAttribute("savings", savings);
        request.setAttribute("pageTitle", "Shopping Cart (" + cartCount + ") | ShopSphere");

        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            if (isAjax) {
                sendJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, false, "Please log in to add items to your cart.", 0);
            } else {
                session = request.getSession(true);
                session.setAttribute("redirectAfterLogin", request.getContextPath() + "/cart");
                response.sendRedirect(request.getContextPath() + "/login");
            }
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "view";

        boolean success = false;
        String message = "";

        try {
            switch (action.toLowerCase()) {
                case "add": {
                    int productId = Integer.parseInt(request.getParameter("productId"));
                    int quantity = 1;
                    String qtyParam = request.getParameter("quantity");
                    if (qtyParam != null && !qtyParam.trim().isEmpty()) {
                        quantity = Math.max(1, Integer.parseInt(qtyParam.trim()));
                    }

                    success = cartService.addToCart(user.getId(), productId, quantity);
                    message = success ? "Item added to cart!" : "Unable to add item to cart.";
                    break;
                }

                case "update": {
                    int cartId = Integer.parseInt(request.getParameter("cartId"));
                    int quantity = Integer.parseInt(request.getParameter("quantity"));

                    if (quantity <= 0) {
                        success = cartService.removeFromCart(cartId);
                        message = "Item removed from cart.";
                    } else {
                        success = cartService.updateQuantity(cartId, quantity);
                        message = success ? "Quantity updated!" : "Failed to update quantity.";
                    }
                    break;
                }

                case "remove": {
                    int cartId = Integer.parseInt(request.getParameter("cartId"));
                    success = cartService.removeFromCart(cartId);
                    message = success ? "Item removed from cart." : "Failed to remove item.";
                    break;
                }

                case "clear": {
                    success = cartService.clearCart(user.getId());
                    message = success ? "Cart cleared." : "Failed to clear cart.";
                    break;
                }

                default:
                    message = "Unknown cart action.";
            }
        } catch (IllegalArgumentException e) {
            success = false;
            message = e.getMessage();
        } catch (Exception e) {
            success = false;
            message = "An error occurred while updating the cart.";
            e.printStackTrace();
        }

        // Update live cart count in session
        int updatedCount = cartService.getCartCount(user.getId());
        session.setAttribute("cartCount", updatedCount);

        if (isAjax) {
            sendJsonResponse(response, HttpServletResponse.SC_OK, success, message, updatedCount);
        } else {
            if (success) {
                session.setAttribute("cartSuccess", message);
            } else {
                session.setAttribute("cartError", message);
            }
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }

    private void sendJsonResponse(HttpServletResponse response, int statusCode, boolean success, String message, int count)
            throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json;charset=UTF-8");
        JsonObject json = new JsonObject();
        json.addProperty("success", success);
        json.addProperty("message", message);
        json.addProperty("cartCount", count);

        try (PrintWriter out = response.getWriter()) {
            out.print(json.toString());
            out.flush();
        }
    }
}
