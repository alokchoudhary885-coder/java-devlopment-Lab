package com.shopsphere.dao;

import com.shopsphere.model.Review;
import com.shopsphere.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing customer {@link Review} entities.
 * <p>
 * Handles customer ratings and feedback, joins with users for author names,
 * calculates average ratings and counts, and maintains synchronized aggregate
 * review metrics on the products table.
 * </p>
 */
public class ReviewDAO {

    private static final String SQL_SELECT_JOIN =
            "SELECT r.id, r.user_id, r.product_id, r.rating, r.comment, r.created_at, " +
            "       u.full_name AS user_name " +
            "FROM reviews r " +
            "LEFT JOIN users u ON r.user_id = u.id";

    private static final String SQL_FIND_BY_PRODUCT_ID =
            SQL_SELECT_JOIN + " WHERE r.product_id = ? ORDER BY r.created_at DESC";

    private static final String SQL_FIND_BY_USER_ID =
            SQL_SELECT_JOIN + " WHERE r.user_id = ? ORDER BY r.created_at DESC";

    private static final String SQL_INSERT =
            "INSERT INTO reviews (user_id, product_id, rating, comment) " +
            "VALUES (?, ?, ?, ?)";

    private static final String SQL_DELETE =
            "DELETE FROM reviews WHERE id = ?";

    private static final String SQL_FIND_PRODUCT_ID_BY_REVIEW_ID =
            "SELECT product_id FROM reviews WHERE id = ?";

    private static final String SQL_AVERAGE_RATING =
            "SELECT COALESCE(AVG(rating), 0.0) FROM reviews WHERE product_id = ?";

    private static final String SQL_REVIEW_COUNT =
            "SELECT COUNT(*) FROM reviews WHERE product_id = ?";

    private static final String SQL_UPDATE_PRODUCT_METRICS =
            "UPDATE products " +
            "SET rating = (SELECT COALESCE(ROUND(AVG(rating), 1), 0.0) FROM reviews WHERE product_id = ?), " +
            "    review_count = (SELECT COUNT(*) FROM reviews WHERE product_id = ?) " +
            "WHERE id = ?";

    /**
     * Retrieves all approved customer reviews for a given product ID, joining with users
     * to populate author transient display names.
     *
     * @param productId the product ID
     * @return list of populated {@link Review} entities; never null
     */
    public List<Review> findByProductId(int productId) {
        List<Review> list = new ArrayList<>();
        if (productId <= 0) {
            return list;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_PRODUCT_ID)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ReviewDAO] Error in findByProductId(" + productId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return list;
    }

    /**
     * Retrieves all reviews written by a specific customer.
     *
     * @param userId the user ID
     * @return list of {@link Review} entities; never null
     */
    public List<Review> findByUserId(int userId) {
        List<Review> list = new ArrayList<>();
        if (userId <= 0) {
            return list;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_USER_ID)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ReviewDAO] Error in findByUserId(" + userId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return list;
    }

    /**
     * Inserts a new customer review and updates the parent product's cached rating and review count.
     *
     * @param review the review entity to insert
     * @return {@code true} if inserted successfully, {@code false} otherwise
     */
    public boolean insert(Review review) {
        if (review == null || review.getUserId() <= 0 || review.getProductId() <= 0) {
            return false;
        }

        // Clamp rating between 1 and 5
        int rating = Math.max(1, Math.min(5, review.getRating()));

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, review.getUserId());
            ps.setInt(2, review.getProductId());
            ps.setInt(3, rating);
            ps.setString(4, review.getComment());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        review.setId(generatedKeys.getInt(1));
                    }
                }

                // Synchronize product aggregate ratings in the products table
                syncProductMetrics(review.getProductId(), conn);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[ReviewDAO] Error in insert(User: " + review.getUserId() + ", Product: " + review.getProductId() + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Deletes a review by its primary key ID and updates the corresponding product's rating metrics.
     *
     * @param reviewId the review ID
     * @return {@code true} if deleted successfully, {@code false} otherwise
     */
    public boolean delete(int reviewId) {
        if (reviewId <= 0) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection()) {
            int productId = -1;

            // Fetch target product ID prior to deletion to resynchronize metrics
            try (PreparedStatement psFind = conn.prepareStatement(SQL_FIND_PRODUCT_ID_BY_REVIEW_ID)) {
                psFind.setInt(1, reviewId);
                try (ResultSet rs = psFind.executeQuery()) {
                    if (rs.next()) {
                        productId = rs.getInt(1);
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
                ps.setInt(1, reviewId);
                int affectedRows = ps.executeUpdate();
                if (affectedRows > 0) {
                    if (productId > 0) {
                        syncProductMetrics(productId, conn);
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("[ReviewDAO] Error in delete(Review ID: " + reviewId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Calculates the average customer star rating for a product (scale 0.0 - 5.0).
     *
     * @param productId the product ID
     * @return average rating; 0.0 if no reviews exist or on error
     */
    public double getAverageRating(int productId) {
        if (productId <= 0) {
            return 0.0;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_AVERAGE_RATING)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Math.round(rs.getDouble(1) * 10.0) / 10.0;
                }
            }
        } catch (SQLException e) {
            System.err.println("[ReviewDAO] Error in getAverageRating(" + productId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return 0.0;
    }

    /**
     * Returns the total count of customer reviews submitted for a specific product.
     *
     * @param productId the product ID
     * @return total review count
     */
    public int getReviewCount(int productId) {
        if (productId <= 0) {
            return 0;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_REVIEW_COUNT)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("[ReviewDAO] Error in getReviewCount(" + productId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return 0;
    }

    /**
     * Synchronizes denormalized rating and review count aggregates in the products table.
     *
     * @param productId the product ID to update
     * @param conn      the active connection
     */
    private void syncProductMetrics(int productId, Connection conn) {
        try (PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_PRODUCT_METRICS)) {
            ps.setInt(1, productId);
            ps.setInt(2, productId);
            ps.setInt(3, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[ReviewDAO] Warning: Unable to sync product metrics for Product ID: " + productId + " - " + e.getMessage());
        }
    }

    /**
     * Maps a {@link ResultSet} row to a {@link Review} entity, including transient author name.
     *
     * @param rs the active ResultSet cursor
     * @return populated {@link Review} object
     * @throws SQLException if a column reading error occurs
     */
    private Review mapRow(ResultSet rs) throws SQLException {
        Review review = new Review();
        review.setId(rs.getInt("id"));
        review.setUserId(rs.getInt("user_id"));
        review.setProductId(rs.getInt("product_id"));
        review.setRating(rs.getInt("rating"));
        review.setComment(rs.getString("comment"));
        review.setCreatedAt(rs.getTimestamp("created_at"));

        // Transient field populated via users JOIN
        try {
            review.setUserName(rs.getString("user_name"));
        } catch (SQLException ignored) {
            // Suppressed if user_name was not queried
        }

        return review;
    }
}
