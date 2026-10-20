package com.shopsphere.controller;

import com.shopsphere.model.User;
import com.shopsphere.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Handles user authentication via email and password.
 * GET  /login  → Displays login.jsp
 * POST /login  → Validates credentials, creates session, redirects based on role
 *
 * Demonstrates: Servlet lifecycle, ServletConfig, HttpSession, doGet/doPost
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private transient UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = new UserService();
        // ServletConfig demonstration: read init parameters if configured
        String configInfo = getServletConfig().getServletName();
        System.out.println("[LoginServlet] Initialized. Servlet name: " + configInfo);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // If already logged in, redirect to appropriate page
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if ("ADMIN".equals(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }
            return;
        }

        // Forward to login page
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Server-side validation
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Please enter both email and password.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // Authenticate via service layer
        User user = userService.login(email.trim(), password);

        if (user == null) {
            request.setAttribute("error", "Invalid email or password, or account is inactive.");
            request.setAttribute("email", email); // Preserve email input
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // Session fixation protection: invalidate old session and create new one
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        HttpSession newSession = request.getSession(true);
        newSession.setAttribute("user", user);
        newSession.setAttribute("userId", user.getId());
        newSession.setAttribute("userRole", user.getRole());
        newSession.setAttribute("userName", user.getFullName());

        // Generate cryptographically secure CSRF token for state-changing forms
        com.shopsphere.util.CSRFTokenUtil.rotateToken(newSession);

        // ServletContext demonstration: access application-wide parameters
        String appName = getServletContext().getInitParameter("appName");
        System.out.println("[LoginServlet] User '" + user.getEmail()
                + "' logged in to " + appName);

        // Redirect based on role
        if ("ADMIN".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else {
            // Check if there was a redirect target before login
            String redirectUrl = (String) newSession.getAttribute("redirectAfterLogin");
            if (redirectUrl != null) {
                newSession.removeAttribute("redirectAfterLogin");
                response.sendRedirect(redirectUrl);
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }
        }
    }

    @Override
    public void destroy() {
        System.out.println("[LoginServlet] Destroyed.");
        super.destroy();
    }
}
