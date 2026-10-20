package com.shopsphere;

import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit and integrity tests for ShopSphere Domain Models, Serialization, and SQL Schema definitions.
 */
public class ModelAndDAOTest {

    // =========================================================================
    // 1. Product Model Calculation Tests
    // =========================================================================

    @Test
    @DisplayName("Product Model: Calculates discounted price and savings with 20% discount")
    public void testProductDiscountCalculation() {
        Product p = new Product();
        p.setId(101);
        p.setName("Wireless Noise-Canceling Headphones");
        p.setPrice(new BigDecimal("1000.00"));
        p.setDiscountPercent(new BigDecimal("20.00"));
        p.setStockQuantity(15);

        assertEquals(new BigDecimal("800.00"), p.getDiscountedPrice());
        assertEquals(new BigDecimal("200.00"), p.getSavingsAmount());
        assertTrue(p.hasDiscount());
        assertTrue(p.isInStock());
    }

    @Test
    @DisplayName("Product Model: Calculates price correctly when zero or null discount")
    public void testProductZeroDiscount() {
        Product p = new Product();
        p.setPrice(new BigDecimal("499.00"));
        p.setDiscountPercent(BigDecimal.ZERO);
        p.setStockQuantity(5);

        assertEquals(new BigDecimal("499.00"), p.getDiscountedPrice());
        assertEquals(new BigDecimal("0.00"), p.getSavingsAmount());
        assertFalse(p.hasDiscount());
        assertTrue(p.isInStock());

        p.setDiscountPercent(null);
        assertEquals(new BigDecimal("499.00"), p.getDiscountedPrice());
        assertEquals(new BigDecimal("0.00"), p.getSavingsAmount());
        assertFalse(p.hasDiscount());
    }

    @Test
    @DisplayName("Product Model: Stock availability returns false when inventory is zero")
    public void testProductOutOfStock() {
        Product p = new Product();
        p.setPrice(new BigDecimal("199.00"));
        p.setStockQuantity(0);

        assertFalse(p.isInStock());
    }

    // =========================================================================
    // 2. Object Serialization Tests (RTU Syllabus Requirement)
    // =========================================================================

    @Test
    @DisplayName("Serialization: Product model can be deeply serialized and deserialized intact")
    public void testProductSerialization() throws Exception {
        Product original = new Product(1, 2, "Mechanical Keyboard", "Keychron", "RGB Wireless",
                new BigDecimal("4999.00"), new BigDecimal("15.00"), 45,
                "https://images.unsplash.com/photo-keychron.jpg",
                new BigDecimal("4.80"), 120, true,
                new Timestamp(System.currentTimeMillis()));

        byte[] serializedBytes;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(original);
            oos.flush();
            serializedBytes = baos.toByteArray();
        }

        assertTrue(serializedBytes.length > 0, "Serialized byte stream must not be empty");

        Product restored;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(serializedBytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            restored = (Product) ois.readObject();
        }

        assertNotNull(restored);
        assertEquals(original.getId(), restored.getId());
        assertEquals(original.getName(), restored.getName());
        assertEquals(original.getBrand(), restored.getBrand());
        assertEquals(original.getPrice(), restored.getPrice());
        assertEquals(original.getDiscountPercent(), restored.getDiscountPercent());
        assertEquals(original.getStockQuantity(), restored.getStockQuantity());
        assertEquals(original.isStatus(), restored.isStatus());
        assertEquals(original.getDiscountedPrice(), restored.getDiscountedPrice());
    }

    @Test
    @DisplayName("Serialization: Category and User models implement Serializable properly")
    public void testUserAndCategorySerialization() throws Exception {
        Category cat = new Category(1, "Electronics", "Gadgets and tech accessories", "fa-laptop", "/images/electronics.jpg", true);
        User user = new User(10, "Alok Choudhary", "alok@shopsphere.in", "hashPass", "9876543210", "ADMIN", "ACTIVE", new Timestamp(System.currentTimeMillis()));

        // Serialize and deserialize Category
        byte[] catBytes = serialize(cat);
        Category restoredCat = deserialize(catBytes, Category.class);
        assertEquals(cat.getName(), restoredCat.getName());
        assertEquals(cat.getIcon(), restoredCat.getIcon());

        // Serialize and deserialize User
        byte[] userBytes = serialize(user);
        User restoredUser = deserialize(userBytes, User.class);
        assertEquals(user.getEmail(), restoredUser.getEmail());
        assertEquals(user.getRole(), restoredUser.getRole());
    }

    // =========================================================================
    // 3. SQL Database Schema File Integrity Verification
    // =========================================================================

    @Test
    @DisplayName("SQL Integrity: schema_only.sql exists and declares all 11 required tables")
    public void testSchemaFileIntegrity() throws IOException {
        Path schemaPath = Paths.get("database", "schema_only.sql");
        assertTrue(Files.exists(schemaPath), "database/schema_only.sql file must exist in the project root");

        String content = Files.readString(schemaPath);
        assertTrue(content.length() > 500, "Schema file must contain comprehensive DDL commands");

        List<String> expectedTables = List.of(
                "users", "categories", "products", "cart", "orders",
                "order_items", "payments", "coupons", "coupon_usage", "reviews", "addresses"
        );

        for (String table : expectedTables) {
            assertTrue(content.toLowerCase().contains("create table if not exists " + table) ||
                            content.toLowerCase().contains("create table " + table),
                    "Schema must define table: " + table);
        }
    }

    @Test
    @DisplayName("SQL Integrity: shopsphere.sql exists and contains seed records")
    public void testSeedDataFileIntegrity() throws IOException {
        Path seedPath = Paths.get("database", "shopsphere.sql");
        assertTrue(Files.exists(seedPath), "database/shopsphere.sql file must exist in the project root");

        String content = Files.readString(seedPath);
        assertTrue(content.contains("admin@shopsphere.com"), "Seed script must include default administrator account");
        assertTrue(content.contains("INSERT INTO") || content.contains("insert into"),
                "Seed script must contain data population statements");
    }

    // =========================================================================
    // Serialization Helper Methods
    // =========================================================================

    private byte[] serialize(Object obj) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            oos.flush();
            return baos.toByteArray();
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (T) ois.readObject();
        }
    }
}
