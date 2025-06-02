package com.backend.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
    }

    @Test
    void shouldCreateEmptyLoginRequest() {
        // When
        LoginRequest request = new LoginRequest();

        // Then
        assertNotNull(request);
        assertNull(request.getEmail());
        assertNull(request.getPassword());
    }

    @Test
    void shouldCreateLoginRequestWithParameters() {
        // Given
        String email = "test@example.com";
        String password = "password123";

        // When
        LoginRequest request = new LoginRequest(email, password);

        // Then
        assertNotNull(request);
        assertEquals(email, request.getEmail());
        assertEquals(password, request.getPassword());
    }

    @Test
    void shouldSetAndGetEmail() {
        // Given
        String expectedEmail = "user@domain.com";

        // When
        loginRequest.setEmail(expectedEmail);

        // Then
        assertEquals(expectedEmail, loginRequest.getEmail());
    }

    @Test
    void shouldSetAndGetPassword() {
        // Given
        String expectedPassword = "secretPassword";

        // When
        loginRequest.setPassword(expectedPassword);

        // Then
        assertEquals(expectedPassword, loginRequest.getPassword());
    }

    @Test
    void shouldHandleNullEmail() {
        // When
        loginRequest.setEmail(null);

        // Then
        assertNull(loginRequest.getEmail());
    }

    @Test
    void shouldHandleNullPassword() {
        // When
        loginRequest.setPassword(null);

        // Then
        assertNull(loginRequest.getPassword());
    }

    @Test
    void shouldHandleEmptyEmail() {
        // Given
        String emptyEmail = "";

        // When
        loginRequest.setEmail(emptyEmail);

        // Then
        assertEquals(emptyEmail, loginRequest.getEmail());
    }

    @Test
    void shouldHandleEmptyPassword() {
        // Given
        String emptyPassword = "";

        // When
        loginRequest.setPassword(emptyPassword);

        // Then
        assertEquals(emptyPassword, loginRequest.getPassword());
    }

    @Test
    void shouldHandleValidEmailFormat() {
        // Given
        String validEmail = "test.user+tag@example.com";

        // When
        loginRequest.setEmail(validEmail);

        // Then
        assertEquals(validEmail, loginRequest.getEmail());
    }

    @Test
    void shouldHandleComplexPassword() {
        // Given
        String complexPassword = "P@ssw0rd123!@#$%^&*()";

        // When
        loginRequest.setPassword(complexPassword);

        // Then
        assertEquals(complexPassword, loginRequest.getPassword());
    }

    @Test
    void shouldMaintainBothFieldsIndependently() {
        // Given
        String email = "user@test.com";
        String password = "myPassword";

        // When
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        // Then
        assertEquals(email, loginRequest.getEmail());
        assertEquals(password, loginRequest.getPassword());
    }

    @Test
    void shouldAllowEmailUpdate() {
        // Given
        String originalEmail = "old@example.com";
        String newEmail = "new@example.com";
        loginRequest.setEmail(originalEmail);

        // When
        loginRequest.setEmail(newEmail);

        // Then
        assertEquals(newEmail, loginRequest.getEmail());
        assertNotEquals(originalEmail, loginRequest.getEmail());
    }

    @Test
    void shouldAllowPasswordUpdate() {
        // Given
        String originalPassword = "oldPassword";
        String newPassword = "newPassword";
        loginRequest.setPassword(originalPassword);

        // When
        loginRequest.setPassword(newPassword);

        // Then
        assertEquals(newPassword, loginRequest.getPassword());
        assertNotEquals(originalPassword, loginRequest.getPassword());
    }

    @Test
    void shouldWorkWithParameterizedConstructor() {
        // Given
        String email = "constructor@test.com";
        String password = "constructorPassword";

        // When
        LoginRequest request = new LoginRequest(email, password);

        // Then
        assertEquals(email, request.getEmail());
        assertEquals(password, request.getPassword());
    }

    @Test
    void shouldWorkWithParameterizedConstructorNullValues() {
        // When
        LoginRequest request = new LoginRequest(null, null);

        // Then
        assertNull(request.getEmail());
        assertNull(request.getPassword());
    }

    @Test
    void shouldWorkWithParameterizedConstructorEmptyValues() {
        // Given
        String emptyEmail = "";
        String emptyPassword = "";

        // When
        LoginRequest request = new LoginRequest(emptyEmail, emptyPassword);

        // Then
        assertEquals(emptyEmail, request.getEmail());
        assertEquals(emptyPassword, request.getPassword());
    }

    @Test
    void shouldHandleWhitespaceInFields() {
        // Given
        String emailWithSpaces = "  user@domain.com  ";
        String passwordWithSpaces = "  password123  ";

        // When
        loginRequest.setEmail(emailWithSpaces);
        loginRequest.setPassword(passwordWithSpaces);

        // Then
        assertEquals(emailWithSpaces, loginRequest.getEmail());
        assertEquals(passwordWithSpaces, loginRequest.getPassword());
    }

    @Test
    void shouldMaintainFieldsAfterMultipleUpdates() {
        // Given
        String finalEmail = "final@example.com";
        String finalPassword = "finalPassword";

        // When
        loginRequest.setEmail("first@example.com");
        loginRequest.setPassword("firstPassword");
        loginRequest.setEmail("second@example.com");
        loginRequest.setPassword("secondPassword");
        loginRequest.setEmail(finalEmail);
        loginRequest.setPassword(finalPassword);

        // Then
        assertEquals(finalEmail, loginRequest.getEmail());
        assertEquals(finalPassword, loginRequest.getPassword());
    }
} 