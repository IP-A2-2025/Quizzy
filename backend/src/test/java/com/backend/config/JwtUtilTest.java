package com.backend.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_SECRET = "test-secret-key-that-is-long-enough-for-hmac-sha-256";
    private static final String TEST_ROLE = "STUDENT";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtil.setSecret(TEST_SECRET);
    }

    @Test
    void generateToken_WithValidEmail_ShouldGenerateValidToken() {
        String token = jwtUtil.generateToken(TEST_EMAIL);

        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
        assertEquals(TEST_EMAIL, jwtUtil.getSubjectFromToken(token));
    }

    @Test
    void getSubjectFromToken_WithValidToken_ShouldReturnEmail() {
        String token = jwtUtil.generateToken(TEST_EMAIL);

        String subject = jwtUtil.getSubjectFromToken(token);

        assertEquals(TEST_EMAIL, subject);
    }

    @Test
    void generateToken_WithCustomExpiration_ShouldExpireAfterSpecifiedTime() {
        // Set custom expiration time to 1 second
        long customExpiration = 1000; // 1 second
        jwtUtil.setExpirationTime(customExpiration);

        String token = jwtUtil.generateToken(TEST_EMAIL);

        // Token should be valid initially
        assertTrue(jwtUtil.validateToken(token));

        // Wait for token to expire
        try {
            Thread.sleep(1100); // Wait slightly more than expiration time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Token should be invalid after expiration
        assertFalse(jwtUtil.validateToken(token));
    }

    @Test
    void generateToken_WithCustomClaims_ShouldContainClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", TEST_ROLE);
        claims.put("customField", "customValue");

        String token = jwtUtil.generateToken(TEST_EMAIL, claims);

        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
        assertEquals(TEST_EMAIL, jwtUtil.getSubjectFromToken(token));
    }

    @Test
    void validateToken_WithInvalidSecret_ShouldReturnFalse() {
        String token = jwtUtil.generateToken(TEST_EMAIL);

        // Change secret key
        jwtUtil.setSecret("different-secret-key-that-is-long-enough-for-hmac-sha-256");

        assertFalse(jwtUtil.validateToken(token));
    }

    @Test
    void validateToken_WithMalformedToken_ShouldReturnFalse() {
        assertFalse(jwtUtil.validateToken("invalid.token.format"));
    }

    @Test
    void getSubjectFromToken_WithExpiredToken_ShouldThrowException() {
        // Set very short expiration
        jwtUtil.setExpirationTime(1);
        String token = jwtUtil.generateToken(TEST_EMAIL);

        // Wait for token to expire
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertThrows(ExpiredJwtException.class, () -> jwtUtil.getSubjectFromToken(token));
    }
}