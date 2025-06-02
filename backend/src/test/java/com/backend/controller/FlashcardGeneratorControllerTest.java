package com.backend.controller;

import com.backend.utils.FlashcardBatchGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlashcardGeneratorControllerTest {

    @Mock
    private FlashcardBatchGenerator flashcardBatchGenerator;

    @InjectMocks
    private FlashcardGeneratorController flashcardGeneratorController;

    @Test
    void shouldGenerateFlashcardsSuccessfully() {
        // Given
        doNothing().when(flashcardBatchGenerator).generateAllFlashcards();

        // When
        ResponseEntity<String> response = flashcardGeneratorController.generateFlashcards();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("✅ Generarea flashcard-urilor s-a încheiat cu succes!", response.getBody());
        verify(flashcardBatchGenerator).generateAllFlashcards();
    }

    @Test
    void shouldReturnErrorWhenGenerationFails() {
        // Given
        String errorMessage = "File not found";
        doThrow(new RuntimeException(errorMessage)).when(flashcardBatchGenerator).generateAllFlashcards();

        // When
        ResponseEntity<String> response = flashcardGeneratorController.generateFlashcards();

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("❌ Eroare la generarea flashcard-urilor:"));
        assertTrue(response.getBody().contains(errorMessage));
        verify(flashcardBatchGenerator).generateAllFlashcards();
    }

    @Test
    void shouldHandleNullPointerException() {
        // Given
        doThrow(new NullPointerException("Null reference")).when(flashcardBatchGenerator).generateAllFlashcards();

        // When
        ResponseEntity<String> response = flashcardGeneratorController.generateFlashcards();

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("❌ Eroare la generarea flashcard-urilor:"));
        assertTrue(response.getBody().contains("Null reference"));
        verify(flashcardBatchGenerator).generateAllFlashcards();
    }

    @Test
    void shouldHandleIllegalArgumentException() {
        // Given
        String errorMessage = "Invalid argument provided";
        doThrow(new IllegalArgumentException(errorMessage)).when(flashcardBatchGenerator).generateAllFlashcards();

        // When
        ResponseEntity<String> response = flashcardGeneratorController.generateFlashcards();

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("❌ Eroare la generarea flashcard-urilor:"));
        assertTrue(response.getBody().contains(errorMessage));
        verify(flashcardBatchGenerator).generateAllFlashcards();
    }

    @Test
    void shouldHandleIOException() {
        // Given
        String errorMessage = "IO error occurred";
        doThrow(new RuntimeException(errorMessage)).when(flashcardBatchGenerator).generateAllFlashcards();

        // When
        ResponseEntity<String> response = flashcardGeneratorController.generateFlashcards();

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("❌ Eroare la generarea flashcard-urilor:"));
        assertTrue(response.getBody().contains(errorMessage));
        verify(flashcardBatchGenerator).generateAllFlashcards();
    }

    @Test
    void shouldCallGenerateAllFlashcardsOnlyOnce() {
        // Given
        doNothing().when(flashcardBatchGenerator).generateAllFlashcards();

        // When
        flashcardGeneratorController.generateFlashcards();

        // Then
        verify(flashcardBatchGenerator, times(1)).generateAllFlashcards();
        verifyNoMoreInteractions(flashcardBatchGenerator);
    }

    @Test
    void shouldReturnResponseEntityWithStringBody() {
        // Given
        doNothing().when(flashcardBatchGenerator).generateAllFlashcards();

        // When
        ResponseEntity<String> response = flashcardGeneratorController.generateFlashcards();

        // Then
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertInstanceOf(String.class, response.getBody());
    }

    @Test
    void shouldHandleExceptionWithNullMessage() {
        // Given
        doThrow(new RuntimeException()).when(flashcardBatchGenerator).generateAllFlashcards();

        // When
        ResponseEntity<String> response = flashcardGeneratorController.generateFlashcards();

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("❌ Eroare la generarea flashcard-urilor:"));
        verify(flashcardBatchGenerator).generateAllFlashcards();
    }

    @Test
    void shouldReturnCorrectErrorStatusCode() {
        // Given
        doThrow(new RuntimeException("Test error")).when(flashcardBatchGenerator).generateAllFlashcards();

        // When
        ResponseEntity<String> response = flashcardGeneratorController.generateFlashcards();

        // Then
        assertEquals(500, response.getStatusCodeValue());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(flashcardBatchGenerator).generateAllFlashcards();
    }

    @Test
    void shouldReturnCorrectSuccessStatusCode() {
        // Given
        doNothing().when(flashcardBatchGenerator).generateAllFlashcards();

        // When
        ResponseEntity<String> response = flashcardGeneratorController.generateFlashcards();

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(flashcardBatchGenerator).generateAllFlashcards();
    }
} 