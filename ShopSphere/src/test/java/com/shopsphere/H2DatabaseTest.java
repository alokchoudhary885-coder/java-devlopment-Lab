package com.shopsphere;

import com.shopsphere.util.PasswordUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class H2DatabaseTest {

    @Test
    @DisplayName("Verify seed credentials match BCrypt hashes")
    public void testSeedCredentials() {
        assertTrue(PasswordUtil.checkPassword("admin123", "$2a$10$20xYyaiClGDhAigzXvhSCeDy3qVbpmxE/BpouR5WBryzeSGOa/ORW"));
        assertTrue(PasswordUtil.checkPassword("customer123", "$2a$10$OWEAPugXrnSkaxqvQI8FOO7NXMKxLqxhnPlMseB9BqDyJVPbB1RP2"));
    }
}
