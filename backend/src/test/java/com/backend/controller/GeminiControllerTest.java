package com.backend.controller;

import com.backend.service.GeminiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeminiControllerTest {

    @Mock
    private GeminiService geminiService;

    @InjectMocks
    private GeminiController geminiController;

    @Test
    void shouldReturnTestMessage() {
        // When
        String result = geminiController.testGemini();

        // Then
        assertEquals("Gemini controller is working!", result);
    }

    @Test
    void shouldGenerateResponseSuccessfully() throws IOException {
        // Given
        String inputFilePath = "test/path/file.txt";
        String expectedResponse = "Generated content";
        when(geminiService.processFile(inputFilePath)).thenReturn(expectedResponse);

        // When
        String result = geminiController.generateResponse(inputFilePath);

        // Then
        assertEquals(expectedResponse, result);
        verify(geminiService).processFile(inputFilePath);
    }

    @Test
    void shouldThrowIOExceptionWhenProcessFileFails() throws IOException {
        // Given
        String inputFilePath = "invalid/path/file.txt";
        when(geminiService.processFile(inputFilePath)).thenThrow(new IOException("File not found"));

        // When & Then
        assertThrows(IOException.class, () -> geminiController.generateResponse(inputFilePath));
        verify(geminiService).processFile(inputFilePath);
    }

    @Test
    void shouldGenerateResponseWithPromptSuccessfully() throws IOException {
        // Given
        String inputFilePath = "course/test.txt";
        String expectedContent = "Generated flashcards content";
        when(geminiService.processFileWithPrompt(eq(inputFilePath), anyString())).thenReturn(expectedContent);

        // When
        String result = geminiController.generateResponseWithPrompt(inputFilePath);

        // Then
        assertTrue(result.startsWith("ok - Saved to"));
        verify(geminiService).processFileWithPrompt(eq(inputFilePath), anyString());
    }

    @Test
    void shouldHandleErrorInGenerateWithPrompt() throws IOException {
        // Given
        String inputFilePath = "course/test.txt";
        when(geminiService.processFileWithPrompt(eq(inputFilePath), anyString()))
            .thenThrow(new IOException("Service error"));

        // When & Then
        assertThrows(IOException.class, () -> geminiController.generateResponseWithPrompt(inputFilePath));
        verify(geminiService).processFileWithPrompt(eq(inputFilePath), anyString());
    }

    @Test
    void shouldCompareUsersAnswerSuccessfully() {
        // Given
        Map<String, String> request = new HashMap<>();
        request.put("question", "What is 2+2?");
        request.put("officialAnswer", "4");
        request.put("usersAnswer", "4");

        when(geminiService.getGeminiResponse(anyString()))
            .thenReturn("text=90.0\\n");

        // When
        ResponseEntity<Double> response = geminiController.compareUsersAnswerToOfficialAnswer(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() >= 0 && response.getBody() <= 100);
    }

    @Test
    void shouldReturnZeroWhenUserAnswerIsCompletelyWrong() {
        // Given
        Map<String, String> request = new HashMap<>();
        request.put("question", "What is 2+2?");
        request.put("officialAnswer", "4");
        request.put("usersAnswer", "wrong answer");

        when(geminiService.getGeminiResponse(anyString()))
            .thenReturn("text=0.0\\n");

        // When
        ResponseEntity<Double> response = geminiController.compareUsersAnswerToOfficialAnswer(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0.0, response.getBody());
    }

    @Test
    void shouldHandlePartiallyCorrectAnswer() {
        // Given
        Map<String, String> request = new HashMap<>();
        request.put("question", "Explain photosynthesis");
        request.put("officialAnswer", "Process where plants convert light into energy using chlorophyll");
        request.put("usersAnswer", "Plants use light to make energy");

        when(geminiService.getGeminiResponse(anyString()))
            .thenReturn("text=75.5\\n");

        // When
        ResponseEntity<Double> response = geminiController.compareUsersAnswerToOfficialAnswer(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() > 0 && response.getBody() < 100);
    }

    @Test
    void shouldHandleInvalidRequestGracefully() {
        // Given
        Map<String, String> invalidRequest = new HashMap<>();
        // Missing required fields
        
        when(geminiService.getGeminiResponse(anyString()))
            .thenReturn("text=0.0\\n");

        // When
        ResponseEntity<Double> response = geminiController.compareUsersAnswerToOfficialAnswer(invalidRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0.0, response.getBody());
    }

    @Test
    void shouldHandleGeminiServiceErrorGracefully() {
        // Given
        Map<String, String> request = new HashMap<>();
        request.put("question", "Test question");
        request.put("officialAnswer", "Test answer");
        request.put("usersAnswer", "Test user answer");

        when(geminiService.getGeminiResponse(anyString()))
            .thenThrow(new RuntimeException("Gemini API error"));

        // When
        ResponseEntity<Double> response = geminiController.compareUsersAnswerToOfficialAnswer(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0.0, response.getBody()); // Should return 0.0 when all calls fail
    }

    @Test
    void shouldHandleMultipleGeminiCallsForAveraging() {
        // Given
        Map<String, String> request = new HashMap<>();
        request.put("question", "What is the capital of France?");
        request.put("officialAnswer", "Paris");
        request.put("usersAnswer", "Paris");

        when(geminiService.getGeminiResponse(anyString()))
            .thenReturn("text=95.0\\n")
            .thenReturn("text=93.0\\n")
            .thenReturn("text=97.0\\n")
            .thenReturn("text=94.0\\n")
            .thenReturn("text=96.0\\n");

        // When
        ResponseEntity<Double> response = geminiController.compareUsersAnswerToOfficialAnswer(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(geminiService, times(5)).getGeminiResponse(anyString());
        // Average of 95, 93, 97, 94, 96 should be 95.0
        assertEquals(95.0, response.getBody(), 0.1);
    }

    @Test
    void shouldHandleEmptyRequestGracefully() {
        // Given
        Map<String, String> emptyRequest = new HashMap<>();
        
        when(geminiService.getGeminiResponse(anyString()))
            .thenReturn("text=0.0\\n");

        // When
        ResponseEntity<Double> response = geminiController.compareUsersAnswerToOfficialAnswer(emptyRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0.0, response.getBody());
    }

    @Test
    void shouldThrowExceptionForNullRequest() {
        // When & Then - This should actually throw NPE when trying to access null map
        assertThrows(RuntimeException.class, () -> 
            geminiController.compareUsersAnswerToOfficialAnswer(null));
    }

    @Test
    void shouldExtractNumberFromValidResponse() {
        // Given
        Map<String, String> request = new HashMap<>();
        request.put("question", "Test");
        request.put("officialAnswer", "Test");
        request.put("usersAnswer", "Test");

        when(geminiService.getGeminiResponse(anyString()))
            .thenReturn("text=85.5\\n");

        // When
        ResponseEntity<Double> response = geminiController.compareUsersAnswerToOfficialAnswer(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldHandleInvalidNumberFormat() {
        // Given
        Map<String, String> request = new HashMap<>();
        request.put("question", "Test");
        request.put("officialAnswer", "Test");
        request.put("usersAnswer", "Test");

        when(geminiService.getGeminiResponse(anyString()))
            .thenReturn("text=invalid\\n");

        // When
        ResponseEntity<Double> response = geminiController.compareUsersAnswerToOfficialAnswer(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0.0, response.getBody()); // Should return 0.0 when parsing fails
    }

    @Test
    void shouldHandleNullGeminiResponse() {
        // Given
        Map<String, String> request = new HashMap<>();
        request.put("question", "Test");
        request.put("officialAnswer", "Test");
        request.put("usersAnswer", "Test");

        when(geminiService.getGeminiResponse(anyString()))
            .thenReturn(null);

        // When
        ResponseEntity<Double> response = geminiController.compareUsersAnswerToOfficialAnswer(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0.0, response.getBody()); // Should return 0.0 when response is null
    }
} 