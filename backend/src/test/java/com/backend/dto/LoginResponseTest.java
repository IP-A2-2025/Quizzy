package com.backend.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class LoginResponseTest {

    private LoginResponse loginResponse;

    @BeforeEach
    void setUp() {
        loginResponse = new LoginResponse();
    }

    @Test
    void shouldCreateEmptyLoginResponse() {
        // When
        LoginResponse response = new LoginResponse();

        // Then
        assertNotNull(response);
        assertEquals("", response.getToken());
        assertEquals("", response.getEmail());
        assertEquals("", response.getRole());
        assertEquals("", response.getMessage());
        assertFalse(response.isSuccess());
        assertNull(response.getUserId());
    }

    @Test
    void shouldCreateLoginResponseWithParametersIncludingUserId() {
        // Given
        String token = "jwt-token-123";
        String email = "test@example.com";
        String role = "student";
        String message = "Login successful";
        boolean success = true;
        Integer userId = 123;

        // When
        LoginResponse response = new LoginResponse(token, email, role, message, success, userId);

        // Then
        assertEquals(token, response.getToken());
        assertEquals(email, response.getEmail());
        assertEquals(role, response.getRole());
        assertEquals(message, response.getMessage());
        assertEquals(success, response.isSuccess());
        assertEquals(userId, response.getUserId());
    }

    @Test
    void shouldCreateLoginResponseWithParametersWithoutUserId() {
        // Given
        String token = "jwt-token-123";
        String email = "test@example.com";
        String role = "student";
        String message = "Login successful";
        boolean success = true;

        // When
        LoginResponse response = new LoginResponse(token, email, role, message, success);

        // Then
        assertEquals(token, response.getToken());
        assertEquals(email, response.getEmail());
        assertEquals(role, response.getRole());
        assertEquals(message, response.getMessage());
        assertEquals(success, response.isSuccess());
        assertNull(response.getUserId());
    }

    @Test
    void shouldSetAndGetToken() {
        // Given
        String expectedToken = "jwt-token-xyz";

        // When
        loginResponse.setToken(expectedToken);

        // Then
        assertEquals(expectedToken, loginResponse.getToken());
    }

    @Test
    void shouldSetAndGetEmail() {
        // Given
        String expectedEmail = "user@domain.com";

        // When
        loginResponse.setEmail(expectedEmail);

        // Then
        assertEquals(expectedEmail, loginResponse.getEmail());
    }

    @Test
    void shouldSetAndGetRole() {
        // Given
        String expectedRole = "profesor";

        // When
        loginResponse.setRole(expectedRole);

        // Then
        assertEquals(expectedRole, loginResponse.getRole());
    }

    @Test
    void shouldSetAndGetMessage() {
        // Given
        String expectedMessage = "Authentication successful";

        // When
        loginResponse.setMessage(expectedMessage);

        // Then
        assertEquals(expectedMessage, loginResponse.getMessage());
    }

    @Test
    void shouldSetAndGetSuccess() {
        // When
        loginResponse.setSuccess(true);

        // Then
        assertTrue(loginResponse.isSuccess());

        // When
        loginResponse.setSuccess(false);

        // Then
        assertFalse(loginResponse.isSuccess());
    }

    @Test
    void shouldSetAndGetUserId() {
        // Given
        Integer expectedUserId = 456;

        // When
        loginResponse.setUserId(expectedUserId);

        // Then
        assertEquals(expectedUserId, loginResponse.getUserId());
    }

    @Test
    void shouldHandleNullValues() {
        // When
        loginResponse.setToken(null);
        loginResponse.setEmail(null);
        loginResponse.setRole(null);
        loginResponse.setMessage(null);
        loginResponse.setUserId(null);

        // Then
        assertEquals("", loginResponse.getToken());
        assertEquals("", loginResponse.getEmail());
        assertEquals("", loginResponse.getRole());
        assertEquals("", loginResponse.getMessage());
        assertNull(loginResponse.getUserId());
    }

    @Test
    void shouldHandleEmptyStringValues() {
        // Given
        String emptyString = "";

        // When
        loginResponse.setToken(emptyString);
        loginResponse.setEmail(emptyString);
        loginResponse.setRole(emptyString);
        loginResponse.setMessage(emptyString);

        // Then
        assertEquals(emptyString, loginResponse.getToken());
        assertEquals(emptyString, loginResponse.getEmail());
        assertEquals(emptyString, loginResponse.getRole());
        assertEquals(emptyString, loginResponse.getMessage());
    }

    @Test
    void shouldCreateSuccessfulLoginResponse() {
        // Given
        String token = "success-token";
        String email = "success@test.com";
        String role = "student";
        String message = "Login successful as student";
        Integer userId = 789;

        // When
        LoginResponse response = new LoginResponse(token, email, role, message, true, userId);

        // Then
        assertTrue(response.isSuccess());
        assertEquals(token, response.getToken());
        assertEquals(email, response.getEmail());
        assertEquals(role, response.getRole());
        assertEquals(message, response.getMessage());
        assertEquals(userId, response.getUserId());
    }

    @Test
    void shouldCreateFailedLoginResponse() {
        // Given
        String message = "Invalid credentials";

        // When
        LoginResponse response = new LoginResponse(null, null, null, message, false);

        // Then
        assertFalse(response.isSuccess());
        assertEquals("", response.getToken());
        assertEquals("", response.getEmail());
        assertEquals("", response.getRole());
        assertEquals(message, response.getMessage());
        assertNull(response.getUserId());
    }

    @Test
    void shouldHandleStudentRole() {
        // Given
        String role = "student";

        // When
        loginResponse.setRole(role);

        // Then
        assertEquals(role, loginResponse.getRole());
    }

    @Test
    void shouldHandleProfesorRole() {
        // Given
        String role = "profesor";

        // When
        loginResponse.setRole(role);

        // Then
        assertEquals(role, loginResponse.getRole());
    }

    @Test
    void shouldHandleLongToken() {
        // Given
        String longToken = "a".repeat(1000); // Very long token

        // When
        loginResponse.setToken(longToken);

        // Then
        assertEquals(longToken, loginResponse.getToken());
    }

    @Test
    void shouldHandleSpecialCharactersInFields() {
        // Given
        String specialEmail = "test+tag@sub.domain.com";
        String specialMessage = "Message with special chars: @#$%^&*()";

        // When
        loginResponse.setEmail(specialEmail);
        loginResponse.setMessage(specialMessage);

        // Then
        assertEquals(specialEmail, loginResponse.getEmail());
        assertEquals(specialMessage, loginResponse.getMessage());
    }

    @Test
    void shouldHandleZeroUserId() {
        // Given
        Integer zeroUserId = 0;

        // When
        loginResponse.setUserId(zeroUserId);

        // Then
        assertEquals(zeroUserId, loginResponse.getUserId());
    }

    @Test
    void shouldHandleNegativeUserId() {
        // Given
        Integer negativeUserId = -1;

        // When
        loginResponse.setUserId(negativeUserId);

        // Then
        assertEquals(negativeUserId, loginResponse.getUserId());
    }

    @Test
    void shouldHandleLargeUserId() {
        // Given
        Integer largeUserId = Integer.MAX_VALUE;

        // When
        loginResponse.setUserId(largeUserId);

        // Then
        assertEquals(largeUserId, loginResponse.getUserId());
    }

    @Test
    void shouldMaintainAllFieldsIndependently() {
        // Given
        String token = "test-token";
        String email = "test@example.com";
        String role = "student";
        String message = "Success";
        boolean success = true;
        Integer userId = 123;

        // When
        loginResponse.setToken(token);
        loginResponse.setEmail(email);
        loginResponse.setRole(role);
        loginResponse.setMessage(message);
        loginResponse.setSuccess(success);
        loginResponse.setUserId(userId);

        // Then
        assertEquals(token, loginResponse.getToken());
        assertEquals(email, loginResponse.getEmail());
        assertEquals(role, loginResponse.getRole());
        assertEquals(message, loginResponse.getMessage());
        assertEquals(success, loginResponse.isSuccess());
        assertEquals(userId, loginResponse.getUserId());
    }
} 