package com.shopsphere.controller.admin;

import com.shopsphere.model.Category;
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
 * Controller for Administrative Category Management.
 * Handles category listing, creation, updates, and deletion with child-product safety checks.
 * Mapped to /admin/categories
 */
@WebServlet(name = "AdminCategoryServlet", urlPatterns = {"/admin/categories"})
public class AdminCategoryServlet extends HttpServlet {

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

        List<Category> categories = productService.getAllCategories();
        request.setAttribute("categories", categories);
        request.setAttribute("activeNav", "categories");

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

        request.getRequestDispatcher("/admin/categories.jsp").forward(request, response);
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
                    Category cat = new Category();
                    cat.setName(com.shopsphere.util.InputSanitizer.escapeHtml(request.getParameter("name")));
                    cat.setDescription(com.shopsphere.util.InputSanitizer.escapeHtml(request.getParameter("description")));
                    cat.setIcon(com.shopsphere.util.InputSanitizer.escapeHtml(request.getParameter("icon")));
                    cat.setImageUrl(request.getParameter("imageUrl") != null ? request.getParameter("imageUrl").trim() : "");
                    cat.setStatus(Boolean.parseBoolean(request.getParameter("status")));

                    boolean added = productService.addCategory(cat);
                    if (added) {
                        session.setAttribute("flashMessage", "Category '" + cat.getName() + "' created.");
                        session.setAttribute("flashType", "success");
                    } else {
                        session.setAttribute("flashMessage", "Failed to add category.");
                        session.setAttribute("flashType", "danger");
                    }
                }
                case "edit" -> {
                    int id = Integer.parseInt(request.getParameter("id"));
                    Category cat = new Category();
                    cat.setId(id);
                    cat.setName(com.shopsphere.util.InputSanitizer.escapeHtml(request.getParameter("name")));
                    cat.setDescription(com.shopsphere.util.InputSanitizer.escapeHtml(request.getParameter("description")));
                    cat.setIcon(com.shopsphere.util.InputSanitizer.escapeHtml(request.getParameter("icon")));
                    cat.setImageUrl(request.getParameter("imageUrl") != null ? request.getParameter("imageUrl").trim() : "");
                    cat.setStatus(Boolean.parseBoolean(request.getParameter("status")));

                    boolean updated = productService.updateCategory(cat);
                    if (updated) {
                        session.setAttribute("flashMessage", "Category updated.");
                        session.setAttribute("flashType", "success");
                    } else {
                        session.setAttribute("flashMessage", "Failed to update category.");
                        session.setAttribute("flashType", "danger");
                    }
                }
                case "delete" -> {
                    int id = Integer.parseInt(request.getParameter("id"));
                    boolean deleted = productService.deleteCategory(id);
                    if (deleted) {
                        session.setAttribute("flashMessage", "Category deleted.");
                        session.setAttribute("flashType", "success");
                    } else {
                        session.setAttribute("flashMessage", "Cannot delete category: products still belong to it.");
                        session.setAttribute("flashType", "danger");
                    }
                }
                default -> {
                    session.setAttribute("flashMessage", "Invalid action: " + action);
                    session.setAttribute("flashType", "warning");
                }
            }
        } catch (Exception e) {
            session.setAttribute("flashMessage", "Error: " + e.getMessage());
            session.setAttribute("flashType", "danger");
        }

        response.sendRedirect(request.getContextPath() + "/admin/categories");
    }
}
