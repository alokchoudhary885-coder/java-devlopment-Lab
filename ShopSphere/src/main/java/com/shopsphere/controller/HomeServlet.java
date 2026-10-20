package com.shopsphere.controller;

import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.model.User;
import com.shopsphere.service.CartService;
import com.shopsphere.service.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller serving the Customer Storefront Home Page.
 * Handles GET / and /home
 * Loads categories, featured products, and active cart metrics.
 * Demonstrates: MVC Presentation Controller, ServletContext sharing, and dynamic EL/JSTL rendering.
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"", "/home"}, loadOnStartup = 1)
public class HomeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private transient ProductService productService;
    private transient CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.productService = new ProductService();
        this.cartService = new CartService();

        // Cache active categories in ServletContext for fast application-wide access
        List<Category> categories = productService.getActiveCategories();
        getServletContext().setAttribute("activeCategories", categories);
        System.out.println("[HomeServlet] Initialized. Cached " + categories.size() + " active categories in ServletContext.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Fetch Categories (fall back to service if context empty)
        @SuppressWarnings("unchecked")
        List<Category> categories = (List<Category>) getServletContext().getAttribute("activeCategories");
        if (categories == null || categories.isEmpty()) {
            categories = productService.getActiveCategories();
            getServletContext().setAttribute("activeCategories", categories);
        }
        request.setAttribute("categories", categories);

        // 2. Fetch Featured Products (Top Rated)
        List<Product> featuredProducts = productService.getFeaturedProducts(8);
        request.setAttribute("featuredProducts", featuredProducts);

        // 3. Sync Cart Count for Authenticated Users
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            int cartCount = cartService.getCartCount(user.getId());
            session.setAttribute("cartCount", cartCount);
        }

        // 4. Set Page Title & Forward to View (index.jsp)
        request.setAttribute("pageTitle", "ShopSphere — Redefine Comfort with Timeless Elegance");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
