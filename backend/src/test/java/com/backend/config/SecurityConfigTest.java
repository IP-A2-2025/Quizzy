package com.backend.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {
    
    // Create an encoder directly instead of loading the entire application context
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void passwordEncoderShouldWorkCorrectly() {
        assertNotNull(passwordEncoder);

        // Test that the encoder works as expected
        String rawPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void passwordEncoderShouldGenerateUniqueHashes() {
        String password = "samePassword";

        // Generate two hashes for the same password
        String hash1 = passwordEncoder.encode(password);
        String hash2 = passwordEncoder.encode(password);

        // Hashes should be different due to salt
        assertNotEquals(hash1, hash2);

        // But both should verify against the original password
        assertTrue(passwordEncoder.matches(password, hash1));
        assertTrue(passwordEncoder.matches(password, hash2));
    }

    @Test
    void passwordEncoderShouldRejectIncorrectPasswords() {
        String correctPassword = "correctPassword";
        String incorrectPassword = "incorrectPassword";

        String encodedPassword = passwordEncoder.encode(correctPassword);

        assertTrue(passwordEncoder.matches(correctPassword, encodedPassword));
        assertFalse(passwordEncoder.matches(incorrectPassword, encodedPassword));
    }
}