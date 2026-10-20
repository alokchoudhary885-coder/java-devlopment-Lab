package com.shopsphere.dao;

import com.shopsphere.model.Product;
import com.shopsphere.util.DBConnection;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing {@link Product} records in the catalog.
 * <p>
 * Supports advanced multi-faceted search and filtering (category, brand, price range,
 * minimum rating, sorting, pagination), stock inventory updates, review aggregation,
 * and admin metric queries.
 * </p>
 */
public class ProductDAO {

    private static final String SQL_SELECT_BASE =
            "SELECT p.id, p.category_id, p.name, p.brand, p.description, p.price, p.discount_percent, " +
            "p.stock_quantity, p.image_url, p.rating, p.review_count, p.status, p.created_at, " +
            "c.name AS category_name " +
            "FROM products p " +
            "LEFT JOIN categories c ON p.category_id = c.id";

    private static final String SQL_FIND_BY_ID =
            SQL_SELECT_BASE + " WHERE p.id = ?";

    private static final String SQL_FIND_ALL =
            SQL_SELECT_BASE + " ORDER BY p.id DESC";

    private static final String SQL_FIND_BY_CATEGORY =
            SQL_SELECT_BASE + " WHERE p.category_id = ? AND p.status = TRUE ORDER BY p.id DESC";

    private static final String SQL_FIND_BY_BRAND =
            SQL_SELECT_BASE + " WHERE p.brand = ? AND p.status = TRUE ORDER BY p.id DESC";

    private static final String SQL_SEARCH =
            SQL_SELECT_BASE + " WHERE p.status = TRUE AND (p.name LIKE ? OR p.brand LIKE ? OR p.description LIKE ?) ORDER BY p.id DESC";

    private static final String SQL_FIND_FEATURED =
            SQL_SELECT_BASE + " WHERE p.status = TRUE ORDER BY p.rating DESC, p.review_count DESC LIMIT ?";

    private static final String SQL_INSERT =
            "INSERT INTO products (category_id, name, brand, description, price, discount_percent, stock_quantity, image_url, rating, review_count, status) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE =
            "UPDATE products SET category_id = ?, name = ?, brand = ?, description = ?, price = ?, discount_percent = ?, " +
            "stock_quantity = ?, image_url = ?, rating = ?, review_count = ?, status = ? WHERE id = ?";

    private static final String SQL_UPDATE_STOCK =
            "UPDATE products SET stock_quantity = ? WHERE id = ?";

    private static final String SQL_CALC_RATING_STATS =
            "SELECT ROUND(COALESCE(AVG(rating), 0.0), 1) AS avg_rating, COUNT(*) AS total_reviews FROM reviews WHERE product_id = ?";

    private static final String SQL_APPLY_RATING_STATS =
            "UPDATE products SET rating = ?, review_count = ? WHERE id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM products WHERE id = ?";

    private static final String SQL_COUNT_ALL =
            "SELECT COUNT(*) FROM products";

    private static final String SQL_COUNT_OUT_OF_STOCK =
            "SELECT COUNT(*) FROM products WHERE stock_quantity <= 0";

    private static final String SQL_FIND_ALL_BRANDS =
            "SELECT DISTINCT brand FROM products WHERE brand IS NOT NULL AND TRIM(brand) != '' ORDER BY brand ASC";

    /**
     * Retrieves a single product by its unique primary key ID, joined with category name.
     *
     * @param id the product ID
     * @return the {@link Product} entity, or {@code null} if not found or on error
     */
    public Product findById(int id) {
        if (id <= 0) {
            return null;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in findById(" + id + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return null;
    }

    /**
     * Retrieves all products in the database ordered by ID descending.
     *
     * @return list of all {@link Product} entities; never null
     */
    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in findAll: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return list;
    }

    /**
     * Retrieves all active products belonging to a given category ID.
     *
     * @param categoryId the category identifier
     * @return list of matching active products
     */
    public List<Product> findByCategory(int categoryId) {
        List<Product> list = new ArrayList<>();
        if (categoryId <= 0) {
            return list;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_CATEGORY)) {

            ps.setInt(1, categoryId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in findByCategory(" + categoryId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return list;
    }

    /**
     * Retrieves all active products belonging to a specific brand.
     *
     * @param brand the brand name
     * @return list of matching active products
     */
    public List<Product> findByBrand(String brand) {
        List<Product> list = new ArrayList<>();
        if (brand == null || brand.trim().isEmpty()) {
            return list;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_BRAND)) {

            ps.setString(1, brand.trim());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in findByBrand('" + brand + "'): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return list;
    }

    /**
     * Performs a text search across product name, brand, and description for active products.
     *
     * @param keyword search keyword
     * @return list of matching products
     */
    public List<Product> search(String keyword) {
        List<Product> list = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return list;
        }

        String searchPattern = "%" + keyword.trim() + "%";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SEARCH)) {

            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in search('" + keyword + "'): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return list;
    }

    /**
     * Retrieves top-rated featured active products sorted by rating and review count.
     *
     * @param limit maximum number of products to return
     * @return list of featured products
     */
    public List<Product> findFeatured(int limit) {
        List<Product> list = new ArrayList<>();
        int effectiveLimit = limit > 0 ? limit : 8;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_FEATURED)) {

            ps.setInt(1, effectiveLimit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in findFeatured(" + limit + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return list;
    }

    /**
     * Dynamically filters active catalog products using parameterized SQL conditions.
     *
     * @param categoryId optional category filter (null or <= 0 ignores)
     * @param brand      optional brand filter (null or "all" ignores)
     * @param minPrice   optional minimum price filter
     * @param maxPrice   optional maximum price filter
     * @param minRating  optional minimum star rating (1-5)
     * @param sortBy     sorting criteria ('price_asc', 'price_desc', 'rating_desc', 'newest', 'name_asc')
     * @param offset     pagination starting offset
     * @param limit      pagination maximum page size
     * @return list of filtered products
     */
    public List<Product> findWithFilters(Integer categoryId, String brand, BigDecimal minPrice,
                                         BigDecimal maxPrice, Integer minRating, String sortBy,
                                         int offset, int limit) {
        List<Product> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT p.id, p.category_id, p.name, p.brand, p.description, p.price, p.discount_percent, " +
                "p.stock_quantity, p.image_url, p.rating, p.review_count, p.status, p.created_at, " +
                "c.name AS category_name " +
                "FROM products p " +
                "LEFT JOIN categories c ON p.category_id = c.id " +
                "WHERE p.status = TRUE"
        );

        List<Object> params = new ArrayList<>();

        if (categoryId != null && categoryId > 0) {
            sql.append(" AND p.category_id = ?");
            params.add(categoryId);
        }

        if (brand != null && !brand.trim().isEmpty() && !"all".equalsIgnoreCase(brand.trim())) {
            sql.append(" AND p.brand = ?");
            params.add(brand.trim());
        }

        if (minPrice != null && minPrice.compareTo(BigDecimal.ZERO) >= 0) {
            sql.append(" AND p.price >= ?");
            params.add(minPrice);
        }

        if (maxPrice != null && maxPrice.compareTo(BigDecimal.ZERO) >= 0) {
            sql.append(" AND p.price <= ?");
            params.add(maxPrice);
        }

        if (minRating != null && minRating > 0) {
            sql.append(" AND p.rating >= ?");
            params.add(BigDecimal.valueOf(minRating));
        }

        // Apply validated sorting order
        if (sortBy != null) {
            switch (sortBy.toLowerCase().trim()) {
                case "price_asc", "price-low-high", "price_low_high" -> sql.append(" ORDER BY p.price ASC");
                case "price_desc", "price-high-low", "price_high_low" -> sql.append(" ORDER BY p.price DESC");
                case "rating_desc", "rating", "top_rated" -> sql.append(" ORDER BY p.rating DESC, p.review_count DESC");
                case "newest", "created_desc" -> sql.append(" ORDER BY p.created_at DESC");
                case "name_asc", "name" -> sql.append(" ORDER BY p.name ASC");
                default -> sql.append(" ORDER BY p.id DESC");
            }
        } else {
            sql.append(" ORDER BY p.id DESC");
        }

        // Apply pagination
        sql.append(" LIMIT ? OFFSET ?");
        params.add(limit > 0 ? limit : 12);
        params.add(Math.max(offset, 0));

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            bindParameters(ps, params);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in findWithFilters: " + e.getMessage());
            e.printStackTrace(System.err);
        }

        return list;
    }

    /**
     * Counts the total number of products matching the dynamic filtering criteria.
     *
     * @param categoryId optional category filter
     * @param brand      optional brand filter
     * @param minPrice   optional minimum price
     * @param maxPrice   optional maximum price
     * @param minRating  optional minimum star rating
     * @return total matching products count
     */
    public int countWithFilters(Integer categoryId, String brand, BigDecimal minPrice,
                                BigDecimal maxPrice, Integer minRating) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM products p WHERE p.status = TRUE");
        List<Object> params = new ArrayList<>();

        if (categoryId != null && categoryId > 0) {
            sql.append(" AND p.category_id = ?");
            params.add(categoryId);
        }

        if (brand != null && !brand.trim().isEmpty() && !"all".equalsIgnoreCase(brand.trim())) {
            sql.append(" AND p.brand = ?");
            params.add(brand.trim());
        }

        if (minPrice != null && minPrice.compareTo(BigDecimal.ZERO) >= 0) {
            sql.append(" AND p.price >= ?");
            params.add(minPrice);
        }

        if (maxPrice != null && maxPrice.compareTo(BigDecimal.ZERO) >= 0) {
            sql.append(" AND p.price <= ?");
            params.add(maxPrice);
        }

        if (minRating != null && minRating > 0) {
            sql.append(" AND p.rating >= ?");
            params.add(BigDecimal.valueOf(minRating));
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            bindParameters(ps, params);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in countWithFilters: " + e.getMessage());
            e.printStackTrace(System.err);
        }

        return 0;
    }

    /**
     * Inserts a new product into the database and populates its generated primary key.
     *
     * @param product the product entity to persist
     * @return {@code true} if inserted successfully, {@code false} otherwise
     */
    public boolean insert(Product product) {
        if (product == null) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getBrand());
            ps.setString(4, product.getDescription());
            ps.setBigDecimal(5, product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO);
            ps.setBigDecimal(6, product.getDiscountPercent() != null ? product.getDiscountPercent() : BigDecimal.ZERO);
            ps.setInt(7, Math.max(product.getStockQuantity(), 0));
            ps.setString(8, product.getImageUrl());
            ps.setBigDecimal(9, product.getRating() != null ? product.getRating() : BigDecimal.ZERO);
            ps.setInt(10, Math.max(product.getReviewCount(), 0));
            ps.setBoolean(11, product.isStatus());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        product.setId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in insert('" + product.getName() + "'): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Updates all editable fields of an existing product.
     *
     * @param product the product entity containing updated values
     * @return {@code true} if updated successfully, {@code false} otherwise
     */
    public boolean update(Product product) {
        if (product == null || product.getId() <= 0) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getBrand());
            ps.setString(4, product.getDescription());
            ps.setBigDecimal(5, product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO);
            ps.setBigDecimal(6, product.getDiscountPercent() != null ? product.getDiscountPercent() : BigDecimal.ZERO);
            ps.setInt(7, Math.max(product.getStockQuantity(), 0));
            ps.setString(8, product.getImageUrl());
            ps.setBigDecimal(9, product.getRating() != null ? product.getRating() : BigDecimal.ZERO);
            ps.setInt(10, Math.max(product.getReviewCount(), 0));
            ps.setBoolean(11, product.isStatus());
            ps.setInt(12, product.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in update(ID: " + product.getId() + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Updates the available stock quantity for a product using an externally managed {@link Connection}.
     * Crucial for executing within atomic checkout transactions.
     * <p>
     * <b>Note:</b> Does NOT close the provided connection.
     * </p>
     *
     * @param productId the product ID
     * @param quantity  new stock quantity (must be >= 0)
     * @param conn      the active transaction connection
     * @return {@code true} if updated successfully, {@code false} otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean updateStock(int productId, int quantity, Connection conn) throws SQLException {
        if (productId <= 0 || conn == null) {
            return false;
        }

        try (PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_STOCK)) {
            ps.setInt(1, Math.max(quantity, 0));
            ps.setInt(2, productId);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Updates the available stock quantity for a product.
     *
     * @param productId the product ID
     * @param quantity  new stock quantity (must be >= 0)
     * @return {@code true} if updated successfully, {@code false} otherwise
     */
    public boolean updateStock(int productId, int quantity) {
        if (productId <= 0) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection()) {
            return updateStock(productId, quantity, conn);
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in updateStock(ID: " + productId + ", qty: " + quantity + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Recalculates and updates a product's average rating and total review count
     * directly from the {@code reviews} table in an atomic operation.
     *
     * @param productId the product ID
     * @return {@code true} if rating statistics were updated successfully, {@code false} otherwise
     */
    public boolean updateRatingStats(int productId) {
        if (productId <= 0) {
            return false;
        }

        BigDecimal avgRating = BigDecimal.ZERO.setScale(1, RoundingMode.HALF_UP);
        int totalReviews = 0;

        try (Connection conn = DBConnection.getConnection()) {
            // Step 1: Calculate aggregate statistics from reviews table
            try (PreparedStatement psCalc = conn.prepareStatement(SQL_CALC_RATING_STATS)) {
                psCalc.setInt(1, productId);
                try (ResultSet rs = psCalc.executeQuery()) {
                    if (rs.next()) {
                        BigDecimal calculatedAvg = rs.getBigDecimal("avg_rating");
                        if (calculatedAvg != null) {
                            avgRating = calculatedAvg.setScale(1, RoundingMode.HALF_UP);
                        }
                        totalReviews = rs.getInt("total_reviews");
                    }
                }
            }

            // Step 2: Apply calculated statistics to the product record
            try (PreparedStatement psApply = conn.prepareStatement(SQL_APPLY_RATING_STATS)) {
                psApply.setBigDecimal(1, avgRating);
                psApply.setInt(2, totalReviews);
                psApply.setInt(3, productId);
                return psApply.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in updateRatingStats(ID: " + productId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }

        return false;
    }

    /**
     * Deletes a product record by primary key ID.
     *
     * @param productId the product ID
     * @return {@code true} if deleted successfully, {@code false} otherwise
     */
    public boolean delete(int productId) {
        if (productId <= 0) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in delete(ID: " + productId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Returns the total count of all products in the catalog.
     *
     * @return total count of products
     */
    public int countAll() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_COUNT_ALL);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in countAll: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return 0;
    }

    /**
     * Returns the total count of products that are currently out of stock (stock_quantity <= 0).
     *
     * @return count of out-of-stock products
     */
    public int countOutOfStock() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_COUNT_OUT_OF_STOCK);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in countOutOfStock: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return 0;
    }

    /**
     * Retrieves all distinct brand names currently present in the catalog.
     *
     * @return sorted list of brand names; never null
     */
    public List<String> findAllBrands() {
        List<String> brands = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL_BRANDS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String brand = rs.getString("brand");
                if (brand != null && !brand.isBlank()) {
                    brands.add(brand.trim());
                }
            }
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in findAllBrands: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return brands;
    }

    /**
     * Binds a list of parameters to a PreparedStatement with appropriate types.
     *
     * @param ps     the prepared statement
     * @param params list of parameter objects
     * @throws SQLException if a database access error occurs
     */
    private void bindParameters(PreparedStatement ps, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            Object param = params.get(i);
            int paramIndex = i + 1;

            if (param instanceof Integer val) {
                ps.setInt(paramIndex, val);
            } else if (param instanceof String val) {
                ps.setString(paramIndex, val);
            } else if (param instanceof BigDecimal val) {
                ps.setBigDecimal(paramIndex, val);
            } else if (param instanceof Boolean val) {
                ps.setBoolean(paramIndex, val);
            } else {
                ps.setObject(paramIndex, param);
            }
        }
    }

    /**
     * Helper method to map a single {@link ResultSet} row to a {@link Product} entity.
     *
     * @param rs the ResultSet cursor
     * @return mapped {@link Product} instance
     * @throws SQLException if a database column access error occurs
     */
    private Product mapRow(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setCategoryId(rs.getInt("category_id"));
        product.setName(rs.getString("name"));
        product.setBrand(rs.getString("brand"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setDiscountPercent(rs.getBigDecimal("discount_percent"));
        product.setStockQuantity(rs.getInt("stock_quantity"));
        product.setImageUrl(rs.getString("image_url"));
        product.setRating(rs.getBigDecimal("rating"));
        product.setReviewCount(rs.getInt("review_count"));
        product.setStatus(rs.getBoolean("status"));
        product.setCreatedAt(rs.getTimestamp("created_at"));

        // Set joined category name if present in result set
        try {
            String categoryName = rs.getString("category_name");
            if (categoryName != null) {
                product.setCategoryName(categoryName);
            }
        } catch (SQLException ignored) {
            // Column category_name was not included in this query projection
        }

        return product;
    }
}
