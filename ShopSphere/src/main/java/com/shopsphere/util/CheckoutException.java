package com.shopsphere.util;

/**
 * Custom exception thrown during checkout processing failures.
 * Wraps transaction rollback scenarios such as insufficient stock,
 * invalid coupon, or database errors during order placement.
 */
public class CheckoutException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CheckoutException(String message) {
        super(message);
    }

    public CheckoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
