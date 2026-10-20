package com.shopsphere.filter;

import com.shopsphere.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Intercepts requests to protected customer resources (checkout, orders, profile, wishlist).
 * If user is not authenticated, preserves the requested URI and redirects to /login.
 */
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {
        "/checkout",
        "/checkout/*",
        "/orders",
        "/orders/*",
        "/order-details",
        "/profile",
        "/profile/*",
        "/wishlist",
        "/wishlist/*"
})
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("[AuthenticationFilter] Initialized for protected customer routes.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        if (isLoggedIn) {
            // User is authenticated, proceed to destination
            chain.doFilter(request, response);
        } else {
            // Save requested URL to redirect back after successful login
            HttpSession newSession = httpRequest.getSession(true);
            String requestedUri = httpRequest.getRequestURI();
            String queryString = httpRequest.getQueryString();
            if (queryString != null) {
                requestedUri += "?" + queryString;
            }
            newSession.setAttribute("redirectAfterLogin", requestedUri);

            // Redirect to login page with notice
            newSession.setAttribute("loginNotice", "Please log in to access this page.");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
        }
    }

    @Override
    public void destroy() {
        System.out.println("[AuthenticationFilter] Destroyed.");
    }
}
