package com.shopsphere.dao;

import com.shopsphere.model.Category;
import com.shopsphere.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing product {@link Category} entities.
 * <p>
 * Handles retrieval of active and all categories, category maintenance (CRUD),
 * and dependent product counting.
 * </p>
 */
public class CategoryDAO {

    private static final String SQL_SELECT_BASE =
            "SELECT id, name, description, icon, image_url, status FROM categories";

    private static final String SQL_FIND_BY_ID =
            SQL_SELECT_BASE + " WHERE id = ?";

    private static final String SQL_FIND_ALL =
            SQL_SELECT_BASE + " ORDER BY id ASC";

    private static final String SQL_FIND_ALL_ACTIVE =
            SQL_SELECT_BASE + " WHERE status = TRUE ORDER BY name ASC";

    private static final String SQL_INSERT =
            "INSERT INTO categories (name, description, icon, image_url, status) VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE =
            "UPDATE categories SET name = ?, description = ?, icon = ?, image_url = ?, status = ? WHERE id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM categories WHERE id = ?";

    private static final String SQL_COUNT_PRODUCTS =
            "SELECT COUNT(*) FROM products WHERE category_id = ?";

    /**
     * Finds a category by its primary key ID.
     *
     * @param id the category ID
     * @return the {@link Category} entity, or {@code null} if not found or on error
     */
    public Category findById(int id) {
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
            System.err.println("[CategoryDAO] Error in findById(" + id + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return null;
    }

    /**
     * Retrieves all categories in the system ordered by ID ascending.
     *
     * @return list of all {@link Category} instances; never null
     */
    public List<Category> findAll() {
        List<Category> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[CategoryDAO] Error in findAll: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return list;
    }

    /**
     * Retrieves all active/published categories ordered alphabetically by name.
     *
     * @return list of active {@link Category} instances; never null
     */
    public List<Category> findAllActive() {
        List<Category> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL_ACTIVE);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[CategoryDAO] Error in findAllActive: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return list;
    }

    /**
     * Inserts a new category into the catalog and sets the generated ID.
     *
     * @param category the category entity to persist
     * @return {@code true} if inserted successfully, {@code false} otherwise
     */
    public boolean insert(Category category) {
        if (category == null || category.getName() == null || category.getName().isBlank()) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, category.getName().trim());
            ps.setString(2, category.getDescription());
            ps.setString(3, category.getIcon());
            ps.setString(4, category.getImageUrl());
            ps.setBoolean(5, category.isStatus());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        category.setId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[CategoryDAO] Error in insert('" + category.getName() + "'): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Updates an existing category record.
     *
     * @param category the category entity with updated fields
     * @return {@code true} if updated successfully, {@code false} otherwise
     */
    public boolean update(Category category) {
        if (category == null || category.getId() <= 0 || category.getName() == null || category.getName().isBlank()) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, category.getName().trim());
            ps.setString(2, category.getDescription());
            ps.setString(3, category.getIcon());
            ps.setString(4, category.getImageUrl());
            ps.setBoolean(5, category.isStatus());
            ps.setInt(6, category.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CategoryDAO] Error in update(ID: " + category.getId() + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Deletes a category by primary key ID.
     * Note: May fail with SQL foreign key constraint if products are linked.
     *
     * @param id the category ID
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
            System.err.println("[CategoryDAO] Error in delete(ID: " + id + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Returns the total count of products mapped to a specific category.
     *
     * @param categoryId the category ID
     * @return total count of matching products
     */
    public int countProducts(int categoryId) {
        if (categoryId <= 0) {
            return 0;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_COUNT_PRODUCTS)) {

            ps.setInt(1, categoryId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("[CategoryDAO] Error in countProducts(Category ID: " + categoryId + "): " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return 0;
    }

    /**
     * Helper method to map a {@link ResultSet} row to a {@link Category} instance.
     *
     * @param rs the ResultSet cursor
     * @return mapped {@link Category} instance
     * @throws SQLException if a column index or name error occurs
     */
    private Category mapRow(ResultSet rs) throws SQLException {
        Category cat = new Category();
        cat.setId(rs.getInt("id"));
        cat.setName(rs.getString("name"));
        cat.setDescription(rs.getString("description"));
        cat.setIcon(rs.getString("icon"));
        cat.setImageUrl(rs.getString("image_url"));
        cat.setStatus(rs.getBoolean("status"));
        return cat;
    }
}
