package com.shopsphere.service;

import com.shopsphere.dao.CartDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Service layer for shopping cart operations.
 * Enforces stock availability checks and calculates server-side totals.
 * Client-submitted prices are never trusted.
 */
public class CartService {

    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public CartService() {
        this.cartDAO = new CartDAO();
        this.productDAO = new ProductDAO();
    }

    /**
     * Retrieves all cart items for a user, enriched with product details.
     */
    public List<CartItem> getCart(int userId) {
        return cartDAO.findByUserId(userId);
    }

    /**
     * Adds a product to the user's cart after validating stock availability.
     *
     * @param userId    the authenticated user ID
     * @param productId the product to add
     * @param quantity  the number of units to add
     * @return true if the item was added successfully
     * @throws IllegalArgumentException if product is unavailable or stock insufficient
     */
    public boolean addToCart(int userId, int productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }

        Product product = productDAO.findById(productId);
        if (product == null || !product.isStatus()) {
            throw new IllegalArgumentException("This product is currently unavailable.");
        }

        // Check existing cart quantity + new quantity against stock
        CartItem existing = cartDAO.findByUserAndProduct(userId, productId);
        int totalQty = quantity + (existing != null ? existing.getQuantity() : 0);

        if (totalQty > product.getStockQuantity()) {
            throw new IllegalArgumentException(
                    "Only " + product.getStockQuantity() + " units available in stock.");
        }

        return cartDAO.addItem(userId, productId, quantity);
    }

    /**
     * Updates the quantity of an existing cart item.
     *
     * @param cartId   the cart item ID
     * @param quantity the new quantity (must be > 0)
     * @return true if updated successfully
     */
    public boolean updateQuantity(int cartId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }
        return cartDAO.updateQuantity(cartId, quantity);
    }

    /**
     * Removes a single item from the cart.
     */
    public boolean removeFromCart(int cartId) {
        return cartDAO.removeItem(cartId);
    }

    /**
     * Clears all items from the user's cart.
     */
    public boolean clearCart(int userId) {
        return cartDAO.clearCart(userId);
    }

    /**
     * Returns the total number of items in the user's cart.
     */
    public int getCartCount(int userId) {
        return cartDAO.getCartCount(userId);
    }

    /**
     * Calculates the cart subtotal using server-side product prices.
     * Each item's discounted price is computed from the database product data,
     * NOT from any client-submitted value.
     *
     * @param items list of cart items with product details populated
     * @return the total cart amount after applying per-product discounts
     */
    public BigDecimal calculateSubtotal(List<CartItem> items) {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartItem item : items) {
            BigDecimal unitPrice = getEffectivePrice(item);
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
            subtotal = subtotal.add(lineTotal);
        }
        return subtotal.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculates total savings across all cart items based on discount percentages.
     *
     * @param items cart items with product details
     * @return total savings amount
     */
    public BigDecimal calculateTotalSavings(List<CartItem> items) {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalSavings = BigDecimal.ZERO;
        for (CartItem item : items) {
            BigDecimal originalPrice = item.getProductPrice();
            BigDecimal effectivePrice = getEffectivePrice(item);
            if (originalPrice != null && effectivePrice != null) {
                BigDecimal savings = originalPrice.subtract(effectivePrice)
                        .multiply(BigDecimal.valueOf(item.getQuantity()));
                if (savings.compareTo(BigDecimal.ZERO) > 0) {
                    totalSavings = totalSavings.add(savings);
                }
            }
        }
        return totalSavings.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Computes the effective (discounted) price for a cart item
     * using the product's original price and discount percentage.
     */
    private BigDecimal getEffectivePrice(CartItem item) {
        BigDecimal price = item.getProductPrice();
        BigDecimal discount = item.getProductDiscount();

        if (price == null) {
            return BigDecimal.ZERO;
        }
        if (discount == null || discount.compareTo(BigDecimal.ZERO) <= 0) {
            return price;
        }

        // price * (1 - discount/100)
        BigDecimal multiplier = BigDecimal.ONE.subtract(
                discount.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
        return price.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }
}
