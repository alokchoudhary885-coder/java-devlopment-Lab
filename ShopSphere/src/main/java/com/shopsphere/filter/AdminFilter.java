package com.shopsphere.filter;

import com.shopsphere.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Role-Based Access Control (RBAC) filter protecting all administrative endpoints.
 * Restricts access under /admin/* strictly to authenticated users with role 'ADMIN'.
 * Returns HTTP 403 Forbidden for unauthorized customers.
 */
@WebFilter(filterName = "AdminFilter", urlPatterns = {"/admin/*"})
public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("[AdminFilter] Initialized for /admin/* protection.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            // Unauthenticated: redirect to login with return target
            HttpSession newSession = httpRequest.getSession(true);
            newSession.setAttribute("redirectAfterLogin", httpRequest.getRequestURI());
            newSession.setAttribute("error", "Admin privileges required. Please log in.");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }

        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            // Authenticated as non-admin customer: HTTP 403 Forbidden
            System.err.println("[AdminFilter] Unauthorized access attempt by user: "
                    + user.getEmail() + " [Role: " + user.getRole() + "]");
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpRequest.setAttribute("errorMessage", "Access Denied: You do not have permission to view administrative resources.");
            httpRequest.getRequestDispatcher("/error-403.jsp").forward(request, response);
            return;
        }

        // Authenticated as ADMIN: proceed
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("[AdminFilter] Destroyed.");
    }
}
