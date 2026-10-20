package com.shopsphere.controller;

import com.shopsphere.model.Product;
import com.shopsphere.model.Review;
import com.shopsphere.service.ProductService;
import com.shopsphere.util.CookieUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for Individual Product Details, Reviews, and Related Items.
 * Handles GET /product-details?id=XX
 * Demonstrates: Non-sensitive cookie tracking (recent_views), review loading, and relational queries.
 */
@WebServlet(name = "ProductDetailServlet", urlPatterns = {"/product-details"})
public class ProductDetailServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private transient ProductService productService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(idParam.trim());
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        // 1. Fetch Product
        Product product = productService.getProductById(productId);
        if (product == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found.");
            return;
        }

        // 2. Track Recently Viewed in Cookie (RTU Syllabus Section 10)
        CookieUtil.addRecentlyViewed(request, response, productId);

        // 3. Fetch Product Reviews
        List<Review> reviews = productService.getProductReviews(productId);

        // 4. Fetch Related Products (same category, excluding current product)
        List<Product> categoryProducts = productService.getProductsByCategory(product.getCategoryId());
        List<Product> relatedProducts = new ArrayList<>();
        if (categoryProducts != null) {
            for (Product p : categoryProducts) {
                if (p.getId() != productId) {
                    relatedProducts.add(p);
                    if (relatedProducts.size() >= 4) break;
                }
            }
        }

        // 5. Set Attributes & Forward
        request.setAttribute("product", product);
        request.setAttribute("reviews", reviews);
        request.setAttribute("relatedProducts", relatedProducts);
        request.setAttribute("pageTitle", product.getName() + " | ShopSphere");

        request.getRequestDispatcher("/product-details.jsp").forward(request, response);
    }
}
