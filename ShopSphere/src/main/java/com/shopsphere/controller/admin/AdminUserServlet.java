package com.shopsphere.controller.admin;

import com.shopsphere.model.User;
import com.shopsphere.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Administrative Customer Directory & Role Management.
 * Manages user accounts, active/suspended status toggling, and role inspections.
 * Mapped to /admin/users
 */
@WebServlet(name = "AdminUserServlet", urlPatterns = {"/admin/users"})
public class AdminUserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private transient UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String roleFilter = request.getParameter("role");
        List<User> users;

        if (roleFilter != null && !roleFilter.trim().isEmpty() && !"ALL".equalsIgnoreCase(roleFilter)) {
            users = userService.getUsersByRole(roleFilter.trim().toUpperCase());
        } else {
            users = userService.getAllUsers();
        }

        request.setAttribute("users", users);
        request.setAttribute("roleFilter", roleFilter != null ? roleFilter.toUpperCase() : "ALL");
        request.setAttribute("activeNav", "users");

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

        request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        HttpSession session = request.getSession(true);

        try {
            int userId = Integer.parseInt(request.getParameter("userId"));

            if ("toggleStatus".equals(action)) {
                String newStatus = request.getParameter("status");
                boolean updated = userService.toggleUserStatus(userId, newStatus);
                if (updated) {
                    session.setAttribute("flashMessage", "User #" + userId + " status updated to " + newStatus + ".");
                    session.setAttribute("flashType", "success");
                } else {
                    session.setAttribute("flashMessage", "Failed to update user status.");
                    session.setAttribute("flashType", "danger");
                }
            }
        } catch (Exception e) {
            session.setAttribute("flashMessage", "Error processing user update: " + e.getMessage());
            session.setAttribute("flashType", "danger");
        }

        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}
