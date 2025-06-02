package com.backend.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseCheckControllerTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private DatabaseCheckController databaseCheckController;

    @Test
    void shouldReturnSuccessMessageWhenDatabaseConnectionIsWorking() {
        // Given
        String expectedMessage = "Connection successful!";
        when(jdbcTemplate.queryForObject("SELECT 'Connection successful!' as status", String.class))
            .thenReturn(expectedMessage);

        // When
        String result = databaseCheckController.checkDatabaseConnection();

        // Then
        assertEquals(expectedMessage, result);
        verify(jdbcTemplate).queryForObject("SELECT 'Connection successful!' as status", String.class);
    }

    @Test
    void shouldReturnErrorMessageWhenDatabaseConnectionFails() {
        // Given
        String errorMessage = "Connection timeout";
        DataAccessException exception = new DataAccessException(errorMessage) {};
        when(jdbcTemplate.queryForObject("SELECT 'Connection successful!' as status", String.class))
            .thenThrow(exception);

        // When
        String result = databaseCheckController.checkDatabaseConnection();

        // Then
        assertTrue(result.startsWith("Database connection failed:"));
        assertTrue(result.contains(errorMessage));
        verify(jdbcTemplate).queryForObject("SELECT 'Connection successful!' as status", String.class);
    }

    @Test
    void shouldHandleRuntimeException() {
        // Given
        RuntimeException exception = new RuntimeException("Database not available");
        when(jdbcTemplate.queryForObject("SELECT 'Connection successful!' as status", String.class))
            .thenThrow(exception);

        // When
        String result = databaseCheckController.checkDatabaseConnection();

        // Then
        assertTrue(result.startsWith("Database connection failed:"));
        assertTrue(result.contains("Database not available"));
        verify(jdbcTemplate).queryForObject("SELECT 'Connection successful!' as status", String.class);
    }

    @Test
    void shouldHandleNullPointerException() {
        // Given
        NullPointerException exception = new NullPointerException("Null connection");
        when(jdbcTemplate.queryForObject("SELECT 'Connection successful!' as status", String.class))
            .thenThrow(exception);

        // When
        String result = databaseCheckController.checkDatabaseConnection();

        // Then
        assertTrue(result.startsWith("Database connection failed:"));
        assertTrue(result.contains("Null connection"));
        verify(jdbcTemplate).queryForObject("SELECT 'Connection successful!' as status", String.class);
    }

    @Test
    void shouldHandleGenericException() {
        // Given
        RuntimeException exception = new RuntimeException("Generic database error");
        when(jdbcTemplate.queryForObject("SELECT 'Connection successful!' as status", String.class))
            .thenThrow(exception);

        // When
        String result = databaseCheckController.checkDatabaseConnection();

        // Then
        assertTrue(result.startsWith("Database connection failed:"));
        assertTrue(result.contains("Generic database error"));
        verify(jdbcTemplate).queryForObject("SELECT 'Connection successful!' as status", String.class);
    }

    @Test
    void shouldExecuteCorrectSQLQuery() {
        // Given
        String expectedSQL = "SELECT 'Connection successful!' as status";
        when(jdbcTemplate.queryForObject(expectedSQL, String.class))
            .thenReturn("Connection successful!");

        // When
        databaseCheckController.checkDatabaseConnection();

        // Then
        verify(jdbcTemplate).queryForObject(expectedSQL, String.class);
    }

    @Test
    void shouldReturnStringTypeResult() {
        // Given
        when(jdbcTemplate.queryForObject("SELECT 'Connection successful!' as status", String.class))
            .thenReturn("Connection successful!");

        // When
        String result = databaseCheckController.checkDatabaseConnection();

        // Then
        assertInstanceOf(String.class, result);
        assertNotNull(result);
    }

    @Test
    void shouldNotReturnNullOnSuccess() {
        // Given
        when(jdbcTemplate.queryForObject("SELECT 'Connection successful!' as status", String.class))
            .thenReturn("Connection successful!");

        // When
        String result = databaseCheckController.checkDatabaseConnection();

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void shouldCallJdbcTemplateOnlyOnce() {
        // Given
        when(jdbcTemplate.queryForObject("SELECT 'Connection successful!' as status", String.class))
            .thenReturn("Connection successful!");

        // When
        databaseCheckController.checkDatabaseConnection();

        // Then
        verify(jdbcTemplate, times(1)).queryForObject("SELECT 'Connection successful!' as status", String.class);
        verifyNoMoreInteractions(jdbcTemplate);
    }
} 