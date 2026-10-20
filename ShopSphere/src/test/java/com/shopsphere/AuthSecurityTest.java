package com.shopsphere;

import com.shopsphere.util.CSRFTokenUtil;
import com.shopsphere.util.InputSanitizer;
import com.shopsphere.util.PasswordUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ShopSphere Security Infrastructure.
 * Covers BCrypt salted password hashing, Input Sanitizer against XSS, and CSRF token generation/validation.
 */
public class AuthSecurityTest {

    // =========================================================================
    // 1. PasswordUtil Tests (BCrypt Salting & Verification)
    // =========================================================================

    @Test
    @DisplayName("PasswordUtil: Hashing produces a valid 60-character BCrypt hash")
    public void testPasswordHashing() {
        String plain = "Admin@2026";
        String hash = PasswordUtil.hashPassword(plain);

        assertNotNull(hash, "Hash must not be null");
        assertEquals(60, hash.length(), "BCrypt hash must be exactly 60 characters");
        assertTrue(hash.startsWith("$2a$") || hash.startsWith("$2b$") || hash.startsWith("$2y$"),
                "Hash must start with standard BCrypt version identifier");
    }

    @Test
    @DisplayName("PasswordUtil: Verifies correct password matches hash and wrong password fails")
    public void testPasswordVerification() {
        String plain = "CustomerSecret123";
        String hash = PasswordUtil.hashPassword(plain);

        assertTrue(PasswordUtil.checkPassword(plain, hash), "Password check must return true for correct password");
        assertFalse(PasswordUtil.checkPassword("WrongSecret", hash), "Password check must return false for incorrect password");
    }

    @Test
    @DisplayName("PasswordUtil: Two hashes of the same password differ due to unique salts")
    public void testSaltUniqueness() {
        String plain = "RepeatablePassword123";
        String hash1 = PasswordUtil.hashPassword(plain);
        String hash2 = PasswordUtil.hashPassword(plain);

        assertNotEquals(hash1, hash2, "BCrypt must generate unique salts for separate hash operations");
        assertTrue(PasswordUtil.checkPassword(plain, hash1));
        assertTrue(PasswordUtil.checkPassword(plain, hash2));
    }

    @Test
    @DisplayName("PasswordUtil: Null or blank passwords produce null hash and fail checks safely")
    public void testNullOrBlankPasswords() {
        assertNull(PasswordUtil.hashPassword(null));
        assertNull(PasswordUtil.hashPassword("   "));
        assertFalse(PasswordUtil.checkPassword(null, "$2a$10$abcdefghijklmnopqrstuu"));
        assertFalse(PasswordUtil.checkPassword("pwd", null));
        assertFalse(PasswordUtil.checkPassword("pwd", "invalid-hash-string"));
    }

    // =========================================================================
    // 2. InputSanitizer Tests (XSS Neutralization, Email & Numeric Parsing)
    // =========================================================================

    @Test
    @DisplayName("InputSanitizer: Escapes HTML special characters to prevent Reflected/Stored XSS")
    public void testEscapeHtmlSpecialChars() {
        String raw = "<b>Hello & 'Welcome' \"World\" / test</b>";
        String escaped = InputSanitizer.escapeHtml(raw);

        assertFalse(escaped.contains("<"), "Must not contain raw unescaped <");
        assertFalse(escaped.contains(">"), "Must not contain raw unescaped >");
        assertFalse(escaped.contains("\""), "Must not contain raw unescaped \"");
        assertFalse(escaped.contains("'"), "Must not contain raw unescaped '");
        assertTrue(escaped.contains("&lt;b&gt;"), "Must contain escaped tags");
        assertTrue(escaped.contains("&amp;"), "Must contain escaped ampersand");
    }

    @Test
    @DisplayName("InputSanitizer: Scrubs dangerous script blocks, onerror handlers, and javascript: links")
    public void testScrubDangerousPayloads() {
        String payload1 = "<script>alert('XSS')</script>Hello World";
        String scrubbed1 = InputSanitizer.scrubDangerousPayloads(payload1);
        assertEquals("Hello World", scrubbed1.trim());

        String payload2 = "<img src='x' onerror='alert(1)'>";
        String scrubbed2 = InputSanitizer.scrubDangerousPayloads(payload2);
        assertFalse(scrubbed2.toLowerCase().contains("onerror"), "onerror must be scrubbed");

        String payload3 = "<a href='javascript:stealCookies()'>Click Here</a>";
        String scrubbed3 = InputSanitizer.scrubDangerousPayloads(payload3);
        assertFalse(scrubbed3.toLowerCase().contains("javascript:"), "javascript: pseudo-protocol must be scrubbed");
    }

    @Test
    @DisplayName("InputSanitizer: Validates and normalizes valid email addresses, rejects malformed emails")
    public void testEmailNormalization() {
        assertEquals("user@example.com", InputSanitizer.normalizeEmail("  User@Example.COM  "));
        assertEquals("admin.test@shopsphere.in", InputSanitizer.normalizeEmail("admin.test@shopsphere.in"));

        assertNull(InputSanitizer.normalizeEmail("invalid-email"));
        assertNull(InputSanitizer.normalizeEmail("user@"));
        assertNull(InputSanitizer.normalizeEmail("@domain.com"));
        assertNull(InputSanitizer.normalizeEmail(null));
    }

    @Test
    @DisplayName("InputSanitizer: Parses integers and BigDecimals safely with fallbacks")
    public void testSafeParsing() {
        assertEquals(42, InputSanitizer.parseSafeInt("42", 0));
        assertEquals(10, InputSanitizer.parseSafeInt("not-a-number", 10));
        assertEquals(5, InputSanitizer.parseSafeInt(null, 5));

        assertEquals(new BigDecimal("199.99"), InputSanitizer.parseSafeBigDecimal("199.99", BigDecimal.ZERO));
        assertEquals(BigDecimal.ONE, InputSanitizer.parseSafeBigDecimal("abc", BigDecimal.ONE));
        assertEquals(BigDecimal.ZERO, InputSanitizer.parseSafeBigDecimal(null, BigDecimal.ZERO));
    }

    // =========================================================================
    // 3. CSRFTokenUtil Tests (Token Generation, Rotation & Session Validation)
    // =========================================================================

    @Test
    @DisplayName("CSRFTokenUtil: Generates, retrieves, and rotates session tokens properly")
    public void testCSRFTokenGenerationAndRotation() {
        Map<String, Object> sessionAttributes = new HashMap<>();
        HttpSession session = createMockSession(sessionAttributes);

        // First call should generate and store a token
        String token1 = CSRFTokenUtil.getToken(session);
        assertNotNull(token1, "Generated token must not be null");
        assertFalse(token1.trim().isEmpty(), "Generated token must not be empty");
        assertEquals(token1, sessionAttributes.get(CSRFTokenUtil.CSRF_SESSION_ATTR));

        // Subsequent call must return the cached session token
        String token2 = CSRFTokenUtil.getToken(session);
        assertEquals(token1, token2, "Subsequent getToken calls must return the identical session token");

        // Rotate token
        String token3 = CSRFTokenUtil.rotateToken(session);
        assertNotNull(token3);
        assertNotEquals(token1, token3, "Rotated token must differ from initial token");
        assertEquals(token3, sessionAttributes.get(CSRFTokenUtil.CSRF_SESSION_ATTR));
    }

    @Test
    @DisplayName("CSRFTokenUtil: Validates matching request tokens and rejects mismatched or missing tokens")
    public void testCSRFTokenValidation() {
        Map<String, Object> sessionAttributes = new HashMap<>();
        HttpSession session = createMockSession(sessionAttributes);
        String validToken = CSRFTokenUtil.getToken(session);

        // Matching parameter
        HttpServletRequest validReq = createMockRequest(session, Map.of(CSRFTokenUtil.CSRF_PARAM_NAME, validToken), Map.of());
        assertTrue(CSRFTokenUtil.isValid(validReq), "Request with matching token parameter must validate to true");

        // Matching header (AJAX style)
        HttpServletRequest validHeaderReq = createMockRequest(session, Map.of(), Map.of("X-CSRF-Token", validToken));
        assertTrue(CSRFTokenUtil.isValid(validHeaderReq), "Request with matching X-CSRF-Token header must validate to true");

        // Mismatched token
        HttpServletRequest invalidReq = createMockRequest(session, Map.of(CSRFTokenUtil.CSRF_PARAM_NAME, "bogus-token-value"), Map.of());
        assertFalse(CSRFTokenUtil.isValid(invalidReq), "Request with bogus token must validate to false");

        // Missing token
        HttpServletRequest missingReq = createMockRequest(session, Map.of(), Map.of());
        assertFalse(CSRFTokenUtil.isValid(missingReq), "Request with missing token must validate to false");
    }

    // =========================================================================
    // Dynamic Proxy Helpers for Jakarta Servlet Interfaces
    // =========================================================================

    private HttpSession createMockSession(Map<String, Object> attributes) {
        return (HttpSession) Proxy.newProxyInstance(
                HttpSession.class.getClassLoader(),
                new Class<?>[]{HttpSession.class},
                (proxy, method, args) -> {
                    String name = method.getName();
                    if ("getAttribute".equals(name)) {
                        return attributes.get(args[0]);
                    } else if ("setAttribute".equals(name)) {
                        attributes.put((String) args[0], args[1]);
                        return null;
                    } else if ("removeAttribute".equals(name)) {
                        return attributes.remove(args[0]);
                    }
                    return null;
                }
        );
    }

    private HttpServletRequest createMockRequest(HttpSession session, Map<String, String> params, Map<String, String> headers) {
        return (HttpServletRequest) Proxy.newProxyInstance(
                HttpServletRequest.class.getClassLoader(),
                new Class<?>[]{HttpServletRequest.class},
                (proxy, method, args) -> {
                    String name = method.getName();
                    if ("getSession".equals(name)) {
                        return session;
                    } else if ("getParameter".equals(name)) {
                        return params.get(args[0]);
                    } else if ("getHeader".equals(name)) {
                        return headers.get(args[0]);
                    }
                    return null;
                }
        );
    }
}
