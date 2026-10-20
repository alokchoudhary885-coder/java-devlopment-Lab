package com.shopsphere.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;

import java.io.IOException;

/**
 * Handles user logout by invalidating the session and clearing cookies.
 * GET /logout → Invalidates session, sets no-cache headers, redirects to login
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Invalidate session
        HttpSession session = request.getSession(false);
        if (session != null) {
            System.out.println("[LogoutServlet] User session invalidated: " + session.getId());
            session.invalidate();
        }

        // Clear recent_views cookie
        Cookie recentCookie = new Cookie("recent_views", "");
        recentCookie.setMaxAge(0);
        recentCookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
        response.addCookie(recentCookie);

        // Set no-cache headers to prevent back-button access
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        // Redirect to login page
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
