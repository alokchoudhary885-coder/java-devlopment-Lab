package com.shopsphere.controller;

import com.shopsphere.model.Address;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Order;
import com.shopsphere.model.User;
import com.shopsphere.service.CartService;
import com.shopsphere.service.CheckoutService;
import com.shopsphere.service.UserService;
import com.shopsphere.util.CheckoutException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Controller managing the Checkout Workflow.
 * Handles:
 * GET  /checkout       - Displays checkout page with saved addresses, coupon validation, and cart items.
 * POST /checkout      - Executes atomic ACID transaction for order placement.
 * GET  /order-success  - Displays celebratory invoice confirmation.
 */
@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout", "/order-success"})
public class CheckoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private transient UserService userService;
    private transient CartService cartService;
    private transient CheckoutService checkoutService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = new UserService();
        this.cartService = new CartService();
        this.checkoutService = new CheckoutService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // Check user session
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            session = request.getSession(true);
            session.setAttribute("redirectAfterLogin", request.getContextPath() + "/checkout");
            session.setAttribute("loginNotice", "Please log in to proceed with checkout.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // ----------------------------------------------------------------------
        // Flow 1: /order-success?orderId=XX
        // ----------------------------------------------------------------------
        if ("/order-success".equals(path)) {
            String orderIdParam = request.getParameter("orderId");
            if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/orders");
                return;
            }
            try {
                int orderId = Integer.parseInt(orderIdParam.trim());
                request.setAttribute("orderId", orderId);
                request.setAttribute("pageTitle", "Order Confirmation | ShopSphere");
                request.getRequestDispatcher("/order-success.jsp").forward(request, response);
                return;
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/orders");
                return;
            }
        }

        // ----------------------------------------------------------------------
        // Flow 2: /checkout
        // ----------------------------------------------------------------------
        // Handle "Buy Now" shortcut: add product directly to cart if requested
        String buyNowIdParam = request.getParameter("buyNowId");
        if (buyNowIdParam != null && !buyNowIdParam.trim().isEmpty()) {
            try {
                int buyNowId = Integer.parseInt(buyNowIdParam.trim());
                int qty = 1;
                String qtyParam = request.getParameter("qty");
                if (qtyParam != null && !qtyParam.trim().isEmpty()) {
                    qty = Math.max(1, Integer.parseInt(qtyParam.trim()));
                }
                cartService.addToCart(user.getId(), buyNowId, qty);
                session.setAttribute("cartCount", cartService.getCartCount(user.getId()));
            } catch (Exception ignored) {}
        }

        // 1. Fetch user's cart items
        List<CartItem> cartItems = cartService.getCart(user.getId());
        if (cartItems == null || cartItems.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        // 2. Calculate server-side subtotal
        BigDecimal subtotal = cartService.calculateSubtotal(cartItems);

        // 3. Optional coupon code check via GET param
        String couponCode = request.getParameter("coupon");
        BigDecimal discountAmount = BigDecimal.ZERO;
        String couponMessage = null;

        if (couponCode != null && !couponCode.trim().isEmpty()) {
            couponCode = couponCode.trim().toUpperCase();
            discountAmount = checkoutService.validateAndApplyCoupon(couponCode, subtotal, user.getId());
            if (discountAmount.compareTo(BigDecimal.ZERO) > 0) {
                couponMessage = "Coupon applied! Saved ₹" + discountAmount;
            } else {
                couponMessage = "Invalid or expired coupon code.";
            }
        }

        BigDecimal finalAmount = subtotal.subtract(discountAmount);
        if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
            finalAmount = BigDecimal.ZERO;
        }
        finalAmount = finalAmount.setScale(2, RoundingMode.HALF_UP);

        // 4. Load saved addresses
        List<Address> addresses = userService.getUserAddresses(user.getId());

        // 5. Set attributes for checkout.jsp
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("addresses", addresses);
        request.setAttribute("subtotal", subtotal);
        request.setAttribute("couponCode", couponCode);
        request.setAttribute("discountAmount", discountAmount);
        request.setAttribute("finalAmount", finalAmount);
        request.setAttribute("couponMessage", couponMessage);
        request.setAttribute("pageTitle", "Checkout — Secure Payment | ShopSphere");

        request.getRequestDispatcher("/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 1. Determine Address
        int addressId = 0;
        String addressIdParam = request.getParameter("addressId");

        if ("new".equals(addressIdParam) || addressIdParam == null || addressIdParam.trim().isEmpty()) {
            // Create on-the-fly new address
            String street = request.getParameter("newStreet");
            String city = request.getParameter("newCity");
            String state = request.getParameter("newState");
            String postalCode = request.getParameter("newPostalCode");
            String country = request.getParameter("newCountry");

            if (street == null || street.trim().isEmpty() || city == null || city.trim().isEmpty()
                    || postalCode == null || postalCode.trim().isEmpty()) {
                request.setAttribute("error", "Please provide complete delivery address details.");
                doGet(request, response);
                return;
            }

            Address newAddr = new Address();
            newAddr.setUserId(user.getId());
            newAddr.setStreet(street.trim());
            newAddr.setCity(city.trim());
            newAddr.setState(state != null ? state.trim() : "Rajasthan");
            newAddr.setPostalCode(postalCode.trim());
            newAddr.setCountry(country != null && !country.trim().isEmpty() ? country.trim() : "India");
            newAddr.setDefault(true);

            boolean addrInserted = userService.addAddress(newAddr);
            if (!addrInserted || newAddr.getId() <= 0) {
                request.setAttribute("error", "Failed to save delivery address.");
                doGet(request, response);
                return;
            }
            addressId = newAddr.getId();
        } else {
            try {
                addressId = Integer.parseInt(addressIdParam.trim());
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid address selected.");
                doGet(request, response);
                return;
            }
        }

        // 2. Payment Method
        String paymentMethod = request.getParameter("paymentMethod");
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            paymentMethod = "UPI";
        }
        paymentMethod = paymentMethod.toUpperCase().trim();

        // 3. Coupon Code
        String couponCode = request.getParameter("couponCode");
        if (couponCode != null) {
            couponCode = couponCode.trim();
        }

        // 4. Execute Atomic Checkout Transaction
        try {
            Order placedOrder = checkoutService.processCheckout(user.getId(), addressId, couponCode, paymentMethod);

            // Update cart badge in session to 0
            session.setAttribute("cartCount", 0);

            // Store order in session for instant success confirmation
            session.setAttribute("lastPlacedOrder", placedOrder);

            // Redirect to success invoice page
            response.sendRedirect(request.getContextPath() + "/order-success?orderId=" + placedOrder.getId());

        } catch (CheckoutException ce) {
            request.setAttribute("error", ce.getMessage());
            doGet(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An unexpected error occurred while placing your order. Please try again.");
            doGet(request, response);
        }
    }
}
