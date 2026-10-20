package com.shopsphere.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility for safe HTTP cookie handling.
 * Demonstrates: Non-sensitive cookie tracking (RTU Syllabus Section 10).
 * Stores recently viewed product IDs without sensitive data or passwords.
 */
public class CookieUtil {

    private static final String RECENT_VIEWS_COOKIE = "recent_views";
    private static final int MAX_RECENT_ITEMS = 6;
    private static final int COOKIE_MAX_AGE_SECONDS = 7 * 24 * 60 * 60; // 7 days

    /**
     * Records a product ID in the recently viewed products cookie.
     * Maintains a comma-separated list of the last 6 distinct product IDs.
     */
    public static void addRecentlyViewed(HttpServletRequest request, HttpServletResponse response, int productId) {
        if (productId <= 0) return;

        List<Integer> ids = getRecentlyViewedIds(request);

        // Remove if already present (to bump to front)
        ids.remove(Integer.valueOf(productId));
        // Add to front of list
        ids.add(0, productId);

        // Cap size
        if (ids.size() > MAX_RECENT_ITEMS) {
            ids = ids.subList(0, MAX_RECENT_ITEMS);
        }

        // Format as comma-separated string
        String cookieValue = ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        Cookie cookie = new Cookie(RECENT_VIEWS_COOKIE, cookieValue);
        cookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
        cookie.setMaxAge(COOKIE_MAX_AGE_SECONDS);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    /**
     * Reads the recently viewed product IDs from request cookies.
     */
    public static List<Integer> getRecentlyViewedIds(HttpServletRequest request) {
        List<Integer> result = new ArrayList<>();
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return result;

        for (Cookie c : cookies) {
            if (RECENT_VIEWS_COOKIE.equals(c.getName())) {
                String val = c.getValue();
                if (val != null && !val.trim().isEmpty()) {
                    String[] tokens = val.split(",");
                    for (String token : tokens) {
                        try {
                            result.add(Integer.parseInt(token.trim()));
                        } catch (NumberFormatException ignored) {}
                    }
                }
                break;
            }
        }
        return result;
    }

    /**
     * Clears the recently viewed cookie on logout or request.
     */
    public static void clearRecentlyViewed(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie(RECENT_VIEWS_COOKIE, "");
        cookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
