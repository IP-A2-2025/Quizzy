package com.backend;

import com.backend.service.GeminiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GeminiServiceTest {
    private final GeminiService geminiService;

    @Mock private RestTemplate restTemplate;
    @TempDir Path tempDir;
    private final String restResponse = "mock content";

    public GeminiServiceTest() {

        MockitoAnnotations.openMocks(this);
        geminiService = new GeminiService();
        ReflectionTestUtils.setField(geminiService, "restTemplate", restTemplate);
    }

    private ResponseEntity<Map> getDefaultResponse() {
        Map<String, Object> responseBody = Map.of("candidates", restResponse);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    private ResponseEntity<Map> getNullBodyResponse() {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    private ResponseEntity<Map> getCorruptResponse() {
        Map<String, Object> responseBody = Map.of("candidotes", restResponse);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @Test
    void getGeminiResponse_success() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Map.class)))
                .thenReturn(getDefaultResponse());

        String result = geminiService.getGeminiResponse("test");
        assertEquals(restResponse, result);
    }

    @Test
    void getGeminiResponse_ResponseEntity_exception() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Map.class)))
                .thenThrow(new RuntimeException("Exception in ResponseEntity"));

        String result = geminiService.getGeminiResponse("test");
        assertTrue(result.contains("Error while contacting Gemini API"));
    }

    @Test
    void processFile_success() throws IOException {
        // Create a subclass of GeminiService for testing
        GeminiService testService = new GeminiService() {
            @Override
            protected String readFile(String path) throws IOException {
                return "test content";
            }
        };

        ReflectionTestUtils.setField(testService, "restTemplate", restTemplate);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Map.class)))
                .thenReturn(getDefaultResponse());

        String result = testService.processFile("test.txt");
        assertEquals(restResponse, result);
    }

    @Test
    void processFileWithPrompt_success() throws IOException {
        // Create a subclass of GeminiService for testing
        GeminiService testService = new GeminiService() {
            @Override
            protected String readFile(String path) throws IOException {
                return "test content";
            }
        };

        ReflectionTestUtils.setField(testService, "restTemplate", restTemplate);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Map.class)))
                .thenReturn(getDefaultResponse());

        String result = testService.processFileWithPrompt("test.txt", "Additional prompt");
        assertEquals(restResponse, result);
    }


    @Test
    void getGeminiResponse_null_response_body() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Map.class)))
                .thenReturn(getNullBodyResponse());

        String result = geminiService.getGeminiResponse("test");
        assertTrue(result.contains("Error: No valid response from Gemini API"));
    }

    @Test
    void getGeminiResponse_corrupted_body() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Map.class)))
                .thenReturn(getCorruptResponse());

        String result = geminiService.getGeminiResponse("test");
        assertTrue(result.contains("Error: No valid response from Gemini API"));
    }
}

