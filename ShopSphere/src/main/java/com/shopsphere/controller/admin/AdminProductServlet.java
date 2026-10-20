package com.shopsphere.controller.admin;

import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.service.ProductService;
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
 * Controller for Administrative Product Catalog Management.
 * Handles CRUD operations, stock adjustments, status toggling, and product searches.
 * Mapped to /admin/products
 */
@WebServlet(name = "AdminProductServlet", urlPatterns = {"/admin/products"})
public class AdminProductServlet extends HttpServlet {

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

        String keyword = request.getParameter("keyword");
        String catParam = request.getParameter("categoryId");

        List<Product> products;
        if (keyword != null && !keyword.trim().isEmpty()) {
            products = productService.searchProducts(keyword.trim());
        } else if (catParam != null && !catParam.trim().isEmpty()) {
            try {
                int catId = Integer.parseInt(catParam.trim());
                products = productService.getProductsByCategory(catId);
            } catch (NumberFormatException e) {
                products = productService.getAllProducts();
            }
        } else {
            products = productService.getAllProducts();
        }

        List<Category> categories = productService.getAllCategories();
        request.setAttribute("products", products);
        request.setAttribute("categories", categories);
        request.setAttribute("activeNav", "items");

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

        request.getRequestDispatcher("/admin/products.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "add";
        }

        HttpSession session = request.getSession(true);

        try {
            switch (action) {
                case "add" -> {
                    Product p = extractProductFromRequest(request);
                    boolean created = productService.addProduct(p);
                    if (created) {
                        session.setAttribute("flashMessage", "Product '" + p.getName() + "' created successfully.");
                        session.setAttribute("flashType", "success");
                    } else {
                        session.setAttribute("flashMessage", "Failed to add product. Please verify fields.");
                        session.setAttribute("flashType", "danger");
                    }
                }
                case "edit" -> {
                    int id = Integer.parseInt(request.getParameter("id"));
                    Product p = extractProductFromRequest(request);
                    p.setId(id);
                    boolean updated = productService.updateProduct(p);
                    if (updated) {
                        session.setAttribute("flashMessage", "Product updated successfully.");
                        session.setAttribute("flashType", "success");
                    } else {
                        session.setAttribute("flashMessage", "Failed to update product.");
                        session.setAttribute("flashType", "danger");
                    }
                }
                case "delete" -> {
                    int id = Integer.parseInt(request.getParameter("id"));
                    boolean deleted = productService.deleteProduct(id);
                    if (deleted) {
                        session.setAttribute("flashMessage", "Product ID #" + id + " deleted.");
                        session.setAttribute("flashType", "success");
                    } else {
                        session.setAttribute("flashMessage", "Could not delete product (active in orders).");
                        session.setAttribute("flashType", "danger");
                    }
                }
                case "updateStock" -> {
                    int id = Integer.parseInt(request.getParameter("id"));
                    int stock = Integer.parseInt(request.getParameter("stockQuantity"));
                    boolean updated = productService.updateStock(id, stock);
                    if (updated) {
                        session.setAttribute("flashMessage", "Stock updated for product #" + id + ".");
                        session.setAttribute("flashType", "success");
                    } else {
                        session.setAttribute("flashMessage", "Failed to update stock quantity.");
                        session.setAttribute("flashType", "danger");
                    }
                }
                default -> {
                    session.setAttribute("flashMessage", "Unknown action: " + action);
                    session.setAttribute("flashType", "warning");
                }
            }
        } catch (Exception e) {
            session.setAttribute("flashMessage", "Error processing request: " + e.getMessage());
            session.setAttribute("flashType", "danger");
        }

        response.sendRedirect(request.getContextPath() + "/admin/products");
    }

    private Product extractProductFromRequest(HttpServletRequest request) {
        Product p = new Product();
        p.setName(com.shopsphere.util.InputSanitizer.escapeHtml(request.getParameter("name")));
        p.setBrand(com.shopsphere.util.InputSanitizer.escapeHtml(request.getParameter("brand")));
        p.setDescription(com.shopsphere.util.InputSanitizer.escapeHtml(request.getParameter("description")));
        p.setImageUrl(request.getParameter("imageUrl") != null ? request.getParameter("imageUrl").trim() : "");

        try {
            p.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
        } catch (NumberFormatException ignored) {}

        try {
            p.setPrice(new BigDecimal(request.getParameter("price")));
        } catch (Exception e) {
            p.setPrice(BigDecimal.ZERO);
        }

        try {
            p.setDiscountPercent(new BigDecimal(request.getParameter("discountPercent")));
        } catch (Exception e) {
            p.setDiscountPercent(BigDecimal.ZERO);
        }

        try {
            p.setStockQuantity(Integer.parseInt(request.getParameter("stockQuantity")));
        } catch (Exception e) {
            p.setStockQuantity(0);
        }

        p.setStatus(Boolean.parseBoolean(request.getParameter("status")));
        return p;
    }
}
