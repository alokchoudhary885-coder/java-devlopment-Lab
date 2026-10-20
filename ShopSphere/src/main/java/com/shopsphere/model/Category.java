package com.shopsphere.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a product category in the ShopSphere catalog.
 */
public class Category implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String description;
    private String icon;
    private String imageUrl;
    private boolean status; // true: active, false: inactive

    /**
     * Default no-argument constructor.
     */
    public Category() {
    }

    /**
     * Constructor without id (useful for creating new category records).
     *
     * @param name        Category display name
     * @param description Short category overview
     * @param icon        FontAwesome or SVG icon identifier/class
     * @param imageUrl    Image URL for the category banner or tile
     * @param status      Category active status flag
     */
    public Category(String name, String description, String icon, String imageUrl, boolean status) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.imageUrl = imageUrl;
        this.status = status;
    }

    /**
     * Full parameterized constructor matching the database schema.
     *
     * @param id          Unique category identifier
     * @param name        Category display name
     * @param description Category description
     * @param icon        Category icon class or identifier
     * @param imageUrl    Category display image URL
     * @param status      Category status (true: active, false: inactive)
     */
    public Category(int id, String name, String description, String icon, String imageUrl, boolean status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.imageUrl = imageUrl;
        this.status = status;
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    // Alternative getter for JSP/EL evaluation
    public boolean getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id && Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", status=" + status +
                '}';
    }
}
