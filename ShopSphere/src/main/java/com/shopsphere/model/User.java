package com.shopsphere.model;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Represents a user account in the ShopSphere platform.
 * Supports authentication, customer profiles, and administrative roles.
 */
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // Role Constants
    public static final String ROLE_CUSTOMER = "CUSTOMER";
    public static final String ROLE_ADMIN = "ADMIN";

    // Status Constants
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";

    private int id;
    private String fullName;
    private String email;
    private String passwordHash;
    private String phone;
    private String role;     // CUSTOMER or ADMIN
    private String status;   // ACTIVE or INACTIVE
    private Timestamp createdAt;

    /**
     * Default no-argument constructor.
     */
    public User() {
    }

    /**
     * Constructor without id and createdAt (convenient for new user registration).
     *
     * @param fullName     Full legal or display name of the user
     * @param email        Unique email address
     * @param passwordHash Salted BCrypt password hash
     * @param phone        Contact phone number
     * @param role         User role (CUSTOMER, ADMIN)
     * @param status       Account status (ACTIVE, INACTIVE)
     */
    public User(String fullName, String email, String passwordHash, String phone, String role, String status) {
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phone = phone;
        this.role = role;
        this.status = status;
    }

    /**
     * Full parameterized constructor matching the database schema.
     *
     * @param id           Unique identifier for the user
     * @param fullName     Full name of the user
     * @param email        Unique email address
     * @param passwordHash Salted BCrypt password hash
     * @param phone        Contact phone number
     * @param role         Role (CUSTOMER, ADMIN)
     * @param status       Status (ACTIVE, INACTIVE)
     * @param createdAt    Account creation timestamp
     */
    public User(int id, String fullName, String email, String passwordHash, String phone, String role, String status, Timestamp createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phone = phone;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
    }

    // --- Business / Convenience Methods ---

    /**
     * Checks if the user has an administrative role.
     *
     * @return true if user is ADMIN, false otherwise
     */
    public boolean isAdmin() {
        return ROLE_ADMIN.equalsIgnoreCase(this.role);
    }

    /**
     * Checks if the user account is active.
     *
     * @return true if user is ACTIVE, false otherwise
     */
    public boolean isActive() {
        return STATUS_ACTIVE.equalsIgnoreCase(this.status);
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
