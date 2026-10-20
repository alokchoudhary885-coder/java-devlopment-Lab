package com.shopsphere.dao;

import com.shopsphere.model.Coupon;
import com.shopsphere.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing promotional {@link Coupon} records and redemption tracking.
 * <p>
 * Handles coupon administration, eligibility validation against cart values, single-use per-user
 * enforcement, and recording transactional redemptions against the {@code coupon_usage} table.
 * </p>
 */
public class CouponDAO {

    private static final String SQL_SELECT_BASE =
            "SELECT id, code, discount_percent, max_discount, min_order_amount, expiry_date, status " +
            "FROM coupons";

    private static final String SQL_FIND_BY_ID =
            SQL_SELECT_BASE + " WHERE id = ?";

    private static final String SQL_FIND_BY_CODE =
            SQL_SELECT_BASE + " WHERE UPPER(code) = UPPER(?)";

    private static final String SQL_FIND_ALL =
            SQL_SELECT_BASE + " ORDER BY id DESC";

    private static final String SQL_FIND_ALL_ACTIVE =
            SQL_SELECT_BASE + " WHERE status = TRUE AND expiry_date >= CURRENT_DATE() ORDER BY id DESC";

    private static final String SQL_INSERT =
            "INSERT INTO coupons (code, discount_percent, max_discount, min_order_amount, expiry_date, status) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE =
            "UPDATE coupons SET code = ?, discount_percent = ?, max_discount = ?, min_order_amount = ?, expiry_date = ?, status = ? " +
            "WHERE id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM coupons WHERE id = ?";

    private static final String SQL_HAS_USER_USED =
            "SELECT COUNT(*) FROM coupon_usage WHERE user_id = ? AND coupon_id = ?";

    private static final String SQL_RECORD_USAGE =
            "INSERT INTO coupon_usage (coupon_id, user_id, order_id) VALUES (?, ?, ?)";

    /**
     * Finds a coupon by its primary key ID.
     *
     * @param id the coupon ID
     * @return matching {@link Coupon}, or {@code null} if not found or on error
     */
    public Coupon findById(int id) {
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
            System.err.println("[CouponDAO] Error in findById(" + id + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return null;
    }

    /**
     * Finds an active or inactive coupon by its promotional code (case-insensitive).
     *
     * @param code the promo code (e.g. 'WELCOME50', 'SAVE20')
     * @return matching {@link Coupon}, or {@code null} if not found or on error
     */
    public Coupon findByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_CODE)) {

            ps.setString(1, code.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[CouponDAO] Error in findByCode('" + code + "'): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return null;
    }

    /**
     * Retrieves all coupons stored in the catalog, ordered by ID descending.
     *
     * @return list of all {@link Coupon} records; never null
     */
    public List<Coupon> findAll() {
        List<Coupon> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[CouponDAO] Error in findAll: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return list;
    }

    /**
     * Retrieves all currently active and unexpired coupons available for customer redemption.
     *
     * @return list of active, valid {@link Coupon} records; never null
     */
    public List<Coupon> findAllActive() {
        List<Coupon> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL_ACTIVE);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[CouponDAO] Error in findAllActive: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return list;
    }

    /**
     * Inserts a new coupon record and assigns the generated primary key to the provided object.
     *
     * @param coupon the coupon entity to persist
     * @return {@code true} if inserted successfully, {@code false} otherwise
     */
    public boolean insert(Coupon coupon) {
        if (coupon == null || coupon.getCode() == null || coupon.getCode().trim().isEmpty()) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, coupon.getCode().trim().toUpperCase());
            ps.setBigDecimal(2, coupon.getDiscountPercent() != null ? coupon.getDiscountPercent() : BigDecimal.ZERO);
            ps.setBigDecimal(3, coupon.getMaxDiscount() != null ? coupon.getMaxDiscount() : BigDecimal.valueOf(500.00));
            ps.setBigDecimal(4, coupon.getMinOrderAmount() != null ? coupon.getMinOrderAmount() : BigDecimal.ZERO);
            ps.setDate(5, coupon.getExpiryDate());
            ps.setBoolean(6, coupon.isStatus());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        coupon.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[CouponDAO] Error in insert('" + coupon.getCode() + "'): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Updates an existing coupon record.
     *
     * @param coupon the coupon entity with updated values
     * @return {@code true} if updated successfully, {@code false} otherwise
     */
    public boolean update(Coupon coupon) {
        if (coupon == null || coupon.getId() <= 0 || coupon.getCode() == null || coupon.getCode().trim().isEmpty()) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, coupon.getCode().trim().toUpperCase());
            ps.setBigDecimal(2, coupon.getDiscountPercent() != null ? coupon.getDiscountPercent() : BigDecimal.ZERO);
            ps.setBigDecimal(3, coupon.getMaxDiscount() != null ? coupon.getMaxDiscount() : BigDecimal.valueOf(500.00));
            ps.setBigDecimal(4, coupon.getMinOrderAmount() != null ? coupon.getMinOrderAmount() : BigDecimal.ZERO);
            ps.setDate(5, coupon.getExpiryDate());
            ps.setBoolean(6, coupon.isStatus());
            ps.setInt(7, coupon.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CouponDAO] Error in update(ID: " + coupon.getId() + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Deletes a coupon by primary key ID.
     *
     * @param id the coupon ID
     * @return {@code true} if deleted successfully, {@code false} otherwise
     */
    public boolean delete(int id) {
        if (id <= 0) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CouponDAO] Error in delete(ID: " + id + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Validates whether a coupon code is currently active, unexpired, and meets the minimum order amount.
     *
     * @param code        the promo code to evaluate
     * @param orderAmount the current total amount of the order
     * @return {@code true} if coupon is eligible for redemption, {@code false} otherwise
     */
    public boolean isValidCoupon(String code, BigDecimal orderAmount) {
        if (code == null || code.trim().isEmpty() || orderAmount == null) {
            return false;
        }

        Coupon coupon = findByCode(code.trim());
        if (coupon == null) {
            return false;
        }

        // Verify active status and expiration date
        if (!coupon.isValid()) {
            return false;
        }

        // Check if order total satisfies the minimum required threshold
        if (coupon.getMinOrderAmount() != null && orderAmount.compareTo(coupon.getMinOrderAmount()) < 0) {
            return false;
        }

        return true;
    }

    /**
     * Checks if a specific customer has previously redeemed a given coupon code.
     *
     * @param userId   the user ID
     * @param couponId the coupon ID
     * @return {@code true} if already used by this customer, {@code false} otherwise
     */
    public boolean hasUserUsedCoupon(int userId, int couponId) {
        if (userId <= 0 || couponId <= 0) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_HAS_USER_USED)) {

            ps.setInt(1, userId);
            ps.setInt(2, couponId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("[CouponDAO] Error in hasUserUsedCoupon(User: " + userId + ", Coupon: " + couponId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Records the redemption of a coupon into the {@code coupon_usage} table using the provided transaction connection.
     * <p>
     * <b>Note:</b> Does NOT close the provided connection.
     * </p>
     *
     * @param couponId the coupon ID
     * @param userId   the user ID
     * @param orderId  the associated order ID
     * @param conn     the active transaction connection
     * @return {@code true} if recorded successfully, {@code false} otherwise
     */
    public boolean recordUsage(int couponId, int userId, int orderId, Connection conn) {
        if (couponId <= 0 || userId <= 0 || orderId <= 0 || conn == null) {
            return false;
        }

        try (PreparedStatement ps = conn.prepareStatement(SQL_RECORD_USAGE)) {
            ps.setInt(1, couponId);
            ps.setInt(2, userId);
            ps.setInt(3, orderId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CouponDAO] Error in recordUsage(Coupon: " + couponId + ", User: " + userId + ", Order: " + orderId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Standalone redemption record helper using an auto-acquired connection (non-transactional).
     *
     * @param couponId the coupon ID
     * @param userId   the user ID
     * @param orderId  the order ID
     * @return {@code true} if recorded successfully, {@code false} otherwise
     */
    public boolean recordUsage(int couponId, int userId, int orderId) {
        try (Connection conn = DBConnection.getConnection()) {
            return recordUsage(couponId, userId, orderId, conn);
        } catch (SQLException e) {
            System.err.println("[CouponDAO] Error in standalone recordUsage: " + e.getMessage());
            e.printStackTrace(System.err);
            return false;
        }
    }

    /**
     * Maps a {@link ResultSet} row to a {@link Coupon} model instance.
     *
     * @param rs the active ResultSet cursor
     * @return mapped {@link Coupon} object
     * @throws SQLException if a column index or reading error occurs
     */
    private Coupon mapRow(ResultSet rs) throws SQLException {
        Coupon coupon = new Coupon();
        coupon.setId(rs.getInt("id"));
        coupon.setCode(rs.getString("code"));
        coupon.setDiscountPercent(rs.getBigDecimal("discount_percent"));
        coupon.setMaxDiscount(rs.getBigDecimal("max_discount"));
        coupon.setMinOrderAmount(rs.getBigDecimal("min_order_amount"));
        coupon.setExpiryDate(rs.getDate("expiry_date"));
        coupon.setStatus(rs.getBoolean("status"));
        return coupon;
    }
}
