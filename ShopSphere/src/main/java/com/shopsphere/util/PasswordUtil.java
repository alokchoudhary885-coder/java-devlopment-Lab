package com.shopsphere.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class providing salted password hashing and verification using jBCrypt.
 * <p>
 * Implements one-way cryptographic hashing with a work factor (log rounds) of 10
 * and ensures graceful handling of invalid or empty inputs.
 * </p>
 */
public final class PasswordUtil {

    private static final int BCRYPT_LOG_ROUNDS = 10;

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private PasswordUtil() {
        throw new UnsupportedOperationException("PasswordUtil is a utility class and cannot be instantiated");
    }

    /**
     * Hashes a plaintext password using BCrypt with a workload factor of 10.
     *
     * @param plainPassword the plain-text password to hash
     * @return the resulting 60-character BCrypt hashed string, or {@code null} if the input is null or empty
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            return null;
        }
        try {
            return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_LOG_ROUNDS));
        } catch (Exception e) {
            System.err.println("[PasswordUtil] Error generating hash: " + e.getMessage());
            return null;
        }
    }

    /**
     * Verifies whether a plaintext password matches a stored BCrypt password hash.
     *
     * @param plainPassword  the plaintext password to verify
     * @param hashedPassword the stored BCrypt hash
     * @return {@code true} if the plaintext password matches the hash; {@code false} otherwise
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || plainPassword.isEmpty() || hashedPassword == null || hashedPassword.isEmpty()) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            // Catches invalid salt/hash format issues gracefully
            System.err.println("[PasswordUtil] Invalid hash format encountered during password check: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("[PasswordUtil] Unexpected error during password check: " + e.getMessage());
            return false;
        }
    }
}
