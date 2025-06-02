package com.backend.exception;

import com.backend.dto.LoginResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void shouldHandleGenericException() {
        // Given
        String errorMessage = "Something went wrong";
        Exception exception = new Exception(errorMessage);

        // When
        ResponseEntity<LoginResponse> response = globalExceptionHandler.handleException(exception);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("An unexpected error occurred"));
        assertTrue(response.getBody().getMessage().contains(errorMessage));
        assertEquals("", response.getBody().getToken());
        assertEquals("", response.getBody().getEmail());
        assertEquals("", response.getBody().getRole());
    }

    @Test
    void shouldHandleRuntimeException() {
        // Given
        String errorMessage = "Runtime error occurred";
        RuntimeException runtimeException = new RuntimeException(errorMessage);

        // When
        ResponseEntity<LoginResponse> response = globalExceptionHandler.handleRuntimeException(runtimeException);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("An error occurred"));
        assertTrue(response.getBody().getMessage().contains(errorMessage));
        assertEquals("", response.getBody().getToken());
        assertEquals("", response.getBody().getEmail());
        assertEquals("", response.getBody().getRole());
    }
} 