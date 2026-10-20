package com.shopsphere.controller;

import com.google.gson.JsonObject;
import com.shopsphere.dao.WishlistDAO;
import com.shopsphere.model.User;
import com.shopsphere.model.WishlistItem;
import com.shopsphere.service.CartService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Controller for Customer Wishlist management.
 * Handles GET /wishlist, POST /wishlist (toggle, add, remove, moveToCart).
 */
@WebServlet(name = "WishlistServlet", urlPatterns = {"/wishlist"})
public class WishlistServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private transient WishlistDAO wishlistDAO;
    private transient CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.wishlistDAO = new WishlistDAO();
        this.cartService = new CartService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            session = request.getSession(true);
            session.setAttribute("redirectAfterLogin", request.getContextPath() + "/wishlist");
            session.setAttribute("loginNotice", "Please log in to view your saved wishlist.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<WishlistItem> wishlistItems = wishlistDAO.findByUserId(user.getId());
        session.setAttribute("wishlistCount", wishlistItems.size());

        request.setAttribute("wishlistItems", wishlistItems);
        request.setAttribute("pageTitle", "My Wishlist (" + wishlistItems.size() + ") | ShopSphere");

        request.getRequestDispatcher("/wishlist.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            if (isAjax) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                JsonObject json = new JsonObject();
                json.addProperty("success", false);
                json.addProperty("message", "Please log in to manage your wishlist.");
                try (PrintWriter out = response.getWriter()) {
                    out.print(json.toString());
                }
            } else {
                session = request.getSession(true);
                session.setAttribute("redirectAfterLogin", request.getContextPath() + "/wishlist");
                response.sendRedirect(request.getContextPath() + "/login");
            }
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "toggle";

        boolean success = false;
        boolean isAdded = false;
        String message = "";

        try {
            switch (action.toLowerCase()) {
                case "toggle": {
                    int productId = Integer.parseInt(request.getParameter("productId"));
                    boolean alreadyIn = wishlistDAO.isInWishlist(user.getId(), productId);
                    if (alreadyIn) {
                        success = wishlistDAO.removeByUserAndProduct(user.getId(), productId);
                        isAdded = false;
                        message = "Item removed from wishlist.";
                    } else {
                        success = wishlistDAO.addItem(user.getId(), productId);
                        isAdded = true;
                        message = "Item saved to wishlist!";
                    }
                    break;
                }

                case "remove": {
                    int wishlistId = Integer.parseInt(request.getParameter("wishlistId"));
                    success = wishlistDAO.removeItem(wishlistId);
                    message = "Item removed from wishlist.";
                    break;
                }

                case "movetocart": {
                    int productId = Integer.parseInt(request.getParameter("productId"));
                    int wishlistId = Integer.parseInt(request.getParameter("wishlistId"));

                    // Add to cart
                    cartService.addToCart(user.getId(), productId, 1);
                    // Remove from wishlist
                    wishlistDAO.removeItem(wishlistId);

                    success = true;
                    message = "Item moved to shopping cart!";
                    break;
                }

                default:
                    message = "Unknown wishlist action.";
            }
        } catch (Exception e) {
            success = false;
            message = "Error: " + e.getMessage();
        }

        int updatedWishlistCount = wishlistDAO.getCount(user.getId());
        session.setAttribute("wishlistCount", updatedWishlistCount);

        int updatedCartCount = cartService.getCartCount(user.getId());
        session.setAttribute("cartCount", updatedCartCount);

        if (isAjax) {
            response.setContentType("application/json;charset=UTF-8");
            JsonObject json = new JsonObject();
            json.addProperty("success", success);
            json.addProperty("isAdded", isAdded);
            json.addProperty("message", message);
            json.addProperty("wishlistCount", updatedWishlistCount);
            json.addProperty("cartCount", updatedCartCount);

            try (PrintWriter out = response.getWriter()) {
                out.print(json.toString());
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/wishlist");
        }
    }
}
