package com.shopsphere.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Defensive Security Utility for Cross-Site Scripting (XSS) Sanitization and Input Normalization.
 * <p>
 * Ensures that all customer and administrator inputs (reviews, comments, search terms,
 * names, addresses, and catalog metadata) are safely sanitized and canonicalized before
 * persistence or HTML rendering.
 * </p>
 */
public final class InputSanitizer {

    private static final Pattern SCRIPT_TAG_PATTERN = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
    private static final Pattern SCRIPT_SRC_PATTERN = Pattern.compile("src[\r\n]*=[\r\n]*\\'(.*?)\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern JAVASCRIPT_PATTERN = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
    private static final Pattern ON_EVENT_PATTERN = Pattern.compile("on\\w+[\r\n]*=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private InputSanitizer() {}

    /**
     * Sanitizes raw text by escaping HTML special characters to prevent Stored & Reflected XSS.
     *
     * @param input the raw user string
     * @return HTML-safe escaped string, or empty string if input is null
     */
    public static String escapeHtml(String input) {
        if (input == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder(input.length() + 16);
        for (char c : input.toCharArray()) {
            switch (c) {
                case '<' -> sb.append("&lt;");
                case '>' -> sb.append("&gt;");
                case '&' -> sb.append("&amp;");
                case '"' -> sb.append("&quot;");
                case '\'' -> sb.append("&#x27;");
                case '/' -> sb.append("&#x2F;");
                default -> {
                    // Filter out non-printable ASCII control characters except newline and tab
                    if (c >= 32 || c == '\n' || c == '\r' || c == '\t') {
                        sb.append(c);
                    }
                }
            }
        }
        return sb.toString().trim();
    }

    /**
     * Aggressively scrubs dangerous scripting signatures (script blocks, event handlers, javascript: pseudo-protocols).
     *
     * @param input the raw input string
     * @return scrubbed string
     */
    public static String scrubDangerousPayloads(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        String clean = input.replaceAll("\0", ""); // Strip null bytes
        clean = SCRIPT_TAG_PATTERN.matcher(clean).replaceAll("");
        clean = SCRIPT_SRC_PATTERN.matcher(clean).replaceAll("");
        clean = JAVASCRIPT_PATTERN.matcher(clean).replaceAll("");
        clean = ON_EVENT_PATTERN.matcher(clean).replaceAll("");
        return clean.trim();
    }

    /**
     * Normalizes and validates an email address.
     *
     * @param email input email string
     * @return trimmed, lowercase email, or null if invalid format
     */
    public static String normalizeEmail(String email) {
        if (email == null) {
            return null;
        }
        String trimmed = email.trim().toLowerCase();
        if (EMAIL_PATTERN.matcher(trimmed).matches()) {
            return trimmed;
        }
        return null;
    }

    /**
     * Safely parses an integer with a fallback default.
     */
    public static int parseSafeInt(String value, int defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Safely parses a BigDecimal with a fallback default.
     */
    public static BigDecimal parseSafeBigDecimal(String value, BigDecimal defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
