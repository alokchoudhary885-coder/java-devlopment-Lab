package com.shopsphere.controller;

import com.shopsphere.model.User;
import com.shopsphere.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Handles new customer account registration.
 * GET  /register → Displays register.jsp
 * POST /register → Validates input, creates account, redirects to login
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

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
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String phone = request.getParameter("phone");

        // Server-side validation
        if (fullName == null || fullName.trim().isEmpty()) {
            setErrorAndForward(request, response, "Full name is required.", fullName, email, phone);
            return;
        }
        if (email == null || email.trim().isEmpty()) {
            setErrorAndForward(request, response, "Email is required.", fullName, email, phone);
            return;
        }
        if (password == null || password.length() < 6) {
            setErrorAndForward(request, response, "Password must be at least 6 characters.", fullName, email, phone);
            return;
        }
        if (!password.equals(confirmPassword)) {
            setErrorAndForward(request, response, "Passwords do not match.", fullName, email, phone);
            return;
        }

        try {
            User user = userService.register(fullName.trim(), email.trim(), password, phone);
            if (user != null) {
                request.getSession().setAttribute("success",
                        "Registration successful! Please login with your credentials.");
                response.sendRedirect(request.getContextPath() + "/login");
            } else {
                setErrorAndForward(request, response, "Registration failed. Please try again.",
                        fullName, email, phone);
            }
        } catch (IllegalArgumentException e) {
            setErrorAndForward(request, response, e.getMessage(), fullName, email, phone);
        } catch (Exception e) {
            setErrorAndForward(request, response, "An error occurred during registration: " + e.getMessage(), fullName, email, phone);
        }
    }

    /**
     * Preserves form input and forwards back to register.jsp with error message.
     */
    private void setErrorAndForward(HttpServletRequest request, HttpServletResponse response,
                                     String errorMsg, String fullName, String email, String phone)
            throws ServletException, IOException {
        request.setAttribute("error", errorMsg);
        request.setAttribute("fullName", fullName);
        request.setAttribute("email", email);
        request.setAttribute("phone", phone);
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
}
