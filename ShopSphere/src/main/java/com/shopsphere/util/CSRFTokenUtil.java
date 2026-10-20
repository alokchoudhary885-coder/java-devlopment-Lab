package com.shopsphere.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility for Cross-Site Request Forgery (CSRF) Protection.
 * <p>
 * Generates cryptographically secure anti-CSRF tokens using {@link SecureRandom},
 * binds them to active HTTP sessions, and validates state-changing requests.
 * </p>
 */
public final class CSRFTokenUtil {

    public static final String CSRF_PARAM_NAME = "csrfToken";
    public static final String CSRF_SESSION_ATTR = "csrfToken";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private CSRFTokenUtil() {}

    /**
     * Retrieves the existing CSRF token from the session or generates a new 32-byte Base64 token.
     *
     * @param session the current HttpSession
     * @return active CSRF token string
     */
    public static String getToken(HttpSession session) {
        if (session == null) {
            return null;
        }
        String token = (String) session.getAttribute(CSRF_SESSION_ATTR);
        if (token == null || token.trim().isEmpty()) {
            token = generateNewToken();
            session.setAttribute(CSRF_SESSION_ATTR, token);
        }
        return token;
    }

    /**
     * Rotates (regenerates) the CSRF token on sensitive state changes (e.g., successful login)
     * to prevent session fixation and token harvesting attacks.
     *
     * @param session the authenticated HttpSession
     * @return newly generated CSRF token
     */
    public static String rotateToken(HttpSession session) {
        if (session == null) {
            return null;
        }
        String newToken = generateNewToken();
        session.setAttribute(CSRF_SESSION_ATTR, newToken);
        return newToken;
    }

    /**
     * Validates that the submitted request contains a CSRF token matching the session token.
     *
     * @param request the incoming HttpServletRequest
     * @return true if token matches, false otherwise
     */
    public static boolean isValid(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }

        String sessionToken = (String) session.getAttribute(CSRF_SESSION_ATTR);
        if (sessionToken == null || sessionToken.isEmpty()) {
            return false;
        }

        // Check request parameter first, then request header (for AJAX calls)
        String requestToken = request.getParameter(CSRF_PARAM_NAME);
        if (requestToken == null || requestToken.isEmpty()) {
            requestToken = request.getHeader("X-CSRF-Token");
        }

        return sessionToken.equals(requestToken);
    }

    private static String generateNewToken() {
        byte[] randomBytes = new byte[32];
        SECURE_RANDOM.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
