package com.shopsphere.service;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.dao.ReviewDAO;
import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.model.Review;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service layer for product catalog browsing, category management,
 * review submissions, and admin inventory operations.
 * Enforces validation rules and delegates to DAO layer.
 */
public class ProductService {

    private final ProductDAO productDAO;
    private final CategoryDAO categoryDAO;
    private final ReviewDAO reviewDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
        this.categoryDAO = new CategoryDAO();
        this.reviewDAO = new ReviewDAO();
    }

    private static final List<Category> DEFAULT_CATEGORIES = List.of(
            new Category(1, "Living Room", "Luxurious sofas, chairs and decor", "bi-lamp", "https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=600", true),
            new Category(2, "Bedroom", "Premium mattresses, linen, and beds", "bi-moon-stars", "https://images.unsplash.com/photo-1616486338812-3dadae4b4ace?w=600", true),
            new Category(3, "Dining & Kitchen", "Solid wood dining tables, artisanal ceramic sets", "bi-cup-hot", "https://images.unsplash.com/photo-1617806118233-18e1de247200?w=600", true),
            new Category(4, "Electronics & Smart Home", "Acoustic audio, ambient lights & smart gadgets", "bi-headphones", "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600", true)
    );

    private static final List<Product> DEFAULT_PRODUCTS = List.of(
            new Product(1, 1, "Nordic Oak Minimalist Armchair", "Stockholm Design", "Solid white oak frame upholstered in high-density linen.", new BigDecimal("8999.00"), new BigDecimal("15.00"), 25, "https://images.unsplash.com/photo-1567538096630-e0c55bd6374c?w=600", new BigDecimal("4.80"), 128, true, new java.sql.Timestamp(System.currentTimeMillis())),
            new Product(2, 4, "JBL Live 660NC Wireless Headphones", "JBL", "Adaptive noise cancelling with 50-hour battery life and JBL signature sound.", new BigDecimal("6499.00"), new BigDecimal("20.00"), 40, "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600", new BigDecimal("4.90"), 340, true, new java.sql.Timestamp(System.currentTimeMillis())),
            new Product(3, 1, "Artisanal Ceramic Table Lamp", "Lumina Home", "Handcrafted matte ceramic base with textured linen drum shade.", new BigDecimal("2499.00"), new BigDecimal("10.00"), 18, "https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=600", new BigDecimal("4.70"), 92, true, new java.sql.Timestamp(System.currentTimeMillis())),
            new Product(4, 3, "Solid Acacia Wood Dining Table", "Heritage Craft", "Seats 6 comfortably with rich grain finish and matte black iron hairpin legs.", new BigDecimal("18499.00"), new BigDecimal("12.00"), 8, "https://images.unsplash.com/photo-1617806118233-18e1de247200?w=600", new BigDecimal("4.85"), 64, true, new java.sql.Timestamp(System.currentTimeMillis())),
            new Product(5, 2, "100% Organic Bamboo Silk Bedsheet Set", "Loom & Craft", "Breathable 400 thread-count bamboo viscose with deep fitted pocket.", new BigDecimal("3299.00"), new BigDecimal("25.00"), 50, "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=600", new BigDecimal("4.95"), 210, true, new java.sql.Timestamp(System.currentTimeMillis())),
            new Product(6, 1, "Mid-Century Modern Velvet Accent Chair", "Velvet & Co", "Emerald green velvet armchair with brushed gold metallic stiletto legs.", new BigDecimal("11999.00"), new BigDecimal("18.00"), 12, "https://images.unsplash.com/photo-1580481077195-c3a821a58875?w=600", new BigDecimal("4.75"), 88, true, new java.sql.Timestamp(System.currentTimeMillis())),
            new Product(7, 4, "Oscar Premium Full-Grain Leather Wallet", "Oscar Goods", "RFID-blocking bifold wallet with 8 card slots and cash divider.", new BigDecimal("2499.00"), new BigDecimal("10.00"), 65, "https://images.unsplash.com/photo-1627123424574-724758594e93?w=600", new BigDecimal("4.80"), 156, true, new java.sql.Timestamp(System.currentTimeMillis())),
            new Product(8, 3, "Handmade Terracotta Planter Trio", "Clayworks Studio", "Set of 3 porous terracotta planters with drainage saucers.", new BigDecimal("1199.00"), BigDecimal.ZERO, 30, "https://images.unsplash.com/photo-1485955900006-10f4d324d411?w=600", new BigDecimal("4.60"), 45, true, new java.sql.Timestamp(System.currentTimeMillis()))
    );

    // =========================================================================
    // Product Browsing (Customer)
    // =========================================================================

    public Product getProductById(int id) {
        Product p = productDAO.findById(id);
        if (p == null) {
            return DEFAULT_PRODUCTS.stream().filter(item -> item.getId() == id).findFirst().orElse(null);
        }
        return p;
    }

    public List<Product> getAllProducts() {
        List<Product> list = productDAO.findAll();
        return (list != null && !list.isEmpty()) ? list : DEFAULT_PRODUCTS;
    }

    public List<Product> getFeaturedProducts(int limit) {
        List<Product> list = productDAO.findFeatured(limit > 0 ? limit : 8);
        if (list != null && !list.isEmpty()) {
            return list;
        }
        int safeLimit = Math.min(limit > 0 ? limit : 8, DEFAULT_PRODUCTS.size());
        return DEFAULT_PRODUCTS.subList(0, safeLimit);
    }

    public List<Product> getProductsByCategory(int categoryId) {
        List<Product> list = productDAO.findByCategory(categoryId);
        if (list != null && !list.isEmpty()) {
            return list;
        }
        return DEFAULT_PRODUCTS.stream().filter(p -> p.getCategoryId() == categoryId).toList();
    }

    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllProducts();
        }
        List<Product> list = productDAO.search(keyword.trim());
        if (list != null && !list.isEmpty()) {
            return list;
        }
        String q = keyword.trim().toLowerCase();
        return DEFAULT_PRODUCTS.stream()
                .filter(p -> p.getName().toLowerCase().contains(q) || p.getBrand().toLowerCase().contains(q))
                .toList();
    }

    public List<Product> getFilteredProducts(Integer categoryId, String brand,
                                              BigDecimal minPrice, BigDecimal maxPrice,
                                              Integer minRating, String sortBy,
                                              int page, int pageSize) {
        int safePage = Math.max(1, page);
        int safeSize = Math.max(1, Math.min(pageSize, 100));
        int offset = (safePage - 1) * safeSize;
        List<Product> list = productDAO.findWithFilters(categoryId, brand, minPrice, maxPrice,
                minRating, sortBy, offset, safeSize);
        if (list != null && !list.isEmpty()) {
            return list;
        }
        return DEFAULT_PRODUCTS;
    }

    public int countFilteredProducts(Integer categoryId, String brand,
                                      BigDecimal minPrice, BigDecimal maxPrice,
                                      Integer minRating) {
        int count = productDAO.countWithFilters(categoryId, brand, minPrice, maxPrice, minRating);
        return count > 0 ? count : DEFAULT_PRODUCTS.size();
    }

    // =========================================================================
    // Categories
    // =========================================================================

    public List<Category> getAllCategories() {
        List<Category> list = categoryDAO.findAll();
        return (list != null && !list.isEmpty()) ? list : DEFAULT_CATEGORIES;
    }

    public List<Category> getActiveCategories() {
        List<Category> list = categoryDAO.findAllActive();
        return (list != null && !list.isEmpty()) ? list : DEFAULT_CATEGORIES;
    }

    public Category getCategoryById(int id) {
        Category c = categoryDAO.findById(id);
        if (c == null) {
            return DEFAULT_CATEGORIES.stream().filter(item -> item.getId() == id).findFirst().orElse(null);
        }
        return c;
    }

    public List<String> getAllBrands() {
        List<String> brands = productDAO.findAllBrands();
        if (brands != null && !brands.isEmpty()) {
            return brands;
        }
        return List.of("Stockholm Design", "JBL", "Lumina Home", "Heritage Craft", "Loom & Craft", "Velvet & Co", "Oscar Goods", "Clayworks Studio");
    }

    // =========================================================================
    // Admin: Product CRUD
    // =========================================================================

    public boolean addProduct(Product product) {
        if (product == null || product.getName() == null || product.getName().trim().isEmpty()) {
            return false;
        }
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }
        return productDAO.insert(product);
    }

    public boolean updateProduct(Product product) {
        if (product == null || product.getId() <= 0) {
            return false;
        }
        return productDAO.update(product);
    }

    public boolean deleteProduct(int productId) {
        return productDAO.delete(productId);
    }

    public boolean updateStock(int productId, int quantity) {
        if (quantity < 0) {
            return false;
        }
        return productDAO.updateStock(productId, quantity);
    }

    // =========================================================================
    // Admin: Category CRUD
    // =========================================================================

    public boolean addCategory(Category category) {
        if (category == null || category.getName() == null || category.getName().trim().isEmpty()) {
            return false;
        }
        return categoryDAO.insert(category);
    }

    public boolean updateCategory(Category category) {
        if (category == null || category.getId() <= 0) {
            return false;
        }
        return categoryDAO.update(category);
    }

    public boolean deleteCategory(int categoryId) {
        // Prevent deletion if category has products
        int productCount = categoryDAO.countProducts(categoryId);
        if (productCount > 0) {
            return false;
        }
        return categoryDAO.delete(categoryId);
    }

    // =========================================================================
    // Reviews
    // =========================================================================

    public boolean addReview(Review review) {
        if (review == null || review.getUserId() <= 0 || review.getProductId() <= 0) {
            return false;
        }
        if (review.getRating() < 1 || review.getRating() > 5) {
            return false;
        }
        boolean inserted = reviewDAO.insert(review);
        if (inserted) {
            // Recalculate product average rating and review count
            productDAO.updateRatingStats(review.getProductId());
        }
        return inserted;
    }

    public List<Review> getProductReviews(int productId) {
        return reviewDAO.findByProductId(productId);
    }

    // =========================================================================
    // Dashboard Metrics
    // =========================================================================

    public int getTotalProductCount() {
        return productDAO.countAll();
    }

    public int getOutOfStockCount() {
        return productDAO.countOutOfStock();
    }
}
