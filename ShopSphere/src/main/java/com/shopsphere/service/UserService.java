package com.shopsphere.service;

import com.shopsphere.dao.UserDAO;
import com.shopsphere.dao.AddressDAO;
import com.shopsphere.model.Address;
import com.shopsphere.model.User;
import com.shopsphere.util.PasswordUtil;

import java.util.List;

/**
 * Service layer for user authentication, registration, profile management,
 * and address CRUD operations. Enforces business rules including password
 * hashing, email uniqueness, and input validation.
 */
public class UserService {

    private final UserDAO userDAO;
    private final AddressDAO addressDAO;

    private static final java.util.concurrent.ConcurrentHashMap<String, User> IN_MEMORY_USERS = new java.util.concurrent.ConcurrentHashMap<>();

    static {
        // Pre-seed demo users so login works seamlessly even when MySQL is offline
        User admin = new User(1, "Super Admin", "admin@shopsphere.com", PasswordUtil.hashPassword("admin123"), "9876543210", "ADMIN", "ACTIVE", new java.sql.Timestamp(System.currentTimeMillis()));
        User customer = new User(2, "Alok Sharma", "customer@shopsphere.com", PasswordUtil.hashPassword("customer123"), "9123456780", "CUSTOMER", "ACTIVE", new java.sql.Timestamp(System.currentTimeMillis()));
        IN_MEMORY_USERS.put("admin@shopsphere.com", admin);
        IN_MEMORY_USERS.put("customer@shopsphere.com", customer);
    }

    public UserService() {
        this.userDAO = new UserDAO();
        this.addressDAO = new AddressDAO();
    }

    // =========================================================================
    // Authentication
    // =========================================================================

    /**
     * Registers a new customer account with validated input and hashed password.
     *
     * @param fullName      user's display name
     * @param email         unique email address
     * @param plainPassword raw password (minimum 6 characters)
     * @param phone         optional phone number
     * @return the newly created User object with generated ID
     * @throws IllegalArgumentException if validation fails or email exists
     */
    public User register(String fullName, String email, String plainPassword, String phone) {
        // Input validation
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required.");
        }
        if (email == null || !email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Please enter a valid email address.");
        }
        if (plainPassword == null || plainPassword.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }

        String cleanEmail = email.trim().toLowerCase();

        // Check duplicate email (in DB and in-memory cache)
        User existing = userDAO.findByEmail(cleanEmail);
        if (existing == null) {
            existing = IN_MEMORY_USERS.get(cleanEmail);
        }
        if (existing != null) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }

        // Build user entity
        User user = new User();
        user.setId((int) (System.currentTimeMillis() % 100000));
        user.setFullName(fullName.trim());
        user.setEmail(cleanEmail);
        user.setPasswordHash(PasswordUtil.hashPassword(plainPassword));
        user.setPhone(phone != null ? phone.trim() : null);
        user.setRole("CUSTOMER");
        user.setStatus("ACTIVE");

        boolean inserted = userDAO.insert(user);
        if (!inserted) {
            // MySQL offline: retain in memory so user can immediately sign in
            IN_MEMORY_USERS.put(cleanEmail, user);
        }
        return user;
    }

    /**
     * Authenticates a user by email and password.
     *
     * @param email         login email
     * @param plainPassword raw password
     * @return authenticated User on success, null on failure
     */
    public User login(String email, String plainPassword) {
        if (email == null || plainPassword == null) {
            return null;
        }

        String cleanEmail = email.trim().toLowerCase();
        User user = userDAO.findByEmail(cleanEmail);
        if (user == null) {
            user = IN_MEMORY_USERS.get(cleanEmail);
        }
        if (user == null) {
            return null;
        }

        // Check account status
        if (!"ACTIVE".equals(user.getStatus())) {
            return null;
        }

        // Verify password
        if (!PasswordUtil.checkPassword(plainPassword, user.getPasswordHash())) {
            return null;
        }

        // Return a session user object
        User sessionUser = new User(user.getId(), user.getFullName(), user.getEmail(), null, user.getPhone(), user.getRole(), user.getStatus(), user.getCreatedAt());
        return sessionUser;
    }

    // =========================================================================
    // User Profile
    // =========================================================================

    public User getUserById(int userId) {
        User user = userDAO.findById(userId);
        if (user != null) {
            user.setPasswordHash(null); // Never expose hash
        }
        return user;
    }

    public boolean updateProfile(User user) {
        if (user == null || user.getId() <= 0) {
            return false;
        }
        return userDAO.update(user);
    }

    // =========================================================================
    // Address Management
    // =========================================================================

    public List<Address> getUserAddresses(int userId) {
        return addressDAO.findByUserId(userId);
    }

    public Address getDefaultAddress(int userId) {
        return addressDAO.findDefaultByUserId(userId);
    }

    public boolean addAddress(Address address) {
        if (address == null || address.getUserId() <= 0) {
            return false;
        }
        return addressDAO.insert(address);
    }

    public boolean setDefaultAddress(int addressId, int userId) {
        return addressDAO.setDefault(addressId, userId);
    }

    public boolean deleteAddress(int addressId) {
        return addressDAO.delete(addressId);
    }

    // =========================================================================
    // Admin: User Management
    // =========================================================================

    public List<User> getAllUsers() {
        List<User> users = userDAO.findAll();
        users.forEach(u -> u.setPasswordHash(null));
        return users;
    }

    public List<User> getUsersByRole(String role) {
        List<User> users = userDAO.findByRole(role);
        users.forEach(u -> u.setPasswordHash(null));
        return users;
    }

    public boolean toggleUserStatus(int userId, String newStatus) {
        if (!"ACTIVE".equals(newStatus) && !"INACTIVE".equals(newStatus)) {
            return false;
        }
        return userDAO.updateStatus(userId, newStatus);
    }

    public int getCustomerCount() {
        return userDAO.countByRole("CUSTOMER");
    }

    public int getAdminCount() {
        return userDAO.countByRole("ADMIN");
    }
}
