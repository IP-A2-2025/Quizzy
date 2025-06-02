package com.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Service
public class GeminiService {
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=";
    @Value("${gemini.api.key}")
    private String API_KEY;

    private final RestTemplate restTemplate = new RestTemplate();

    // In GeminiService.java
    protected String readFile(String relativeFilePath) throws IOException {
        String coursesPath = System.getProperty("user.dir") + File.separator + "courses" + File.separator + relativeFilePath;
        return Files.readString(Path.of(coursesPath));
    }

    public String processFile(String relativeFilePath) throws IOException {
        String fileContent = readFile(relativeFilePath);
        return getGeminiResponse(fileContent);
    }

    public String processFileWithPrompt(String relativeFilePath, String additionalPrompt) throws IOException {
        String fileContent = readFile(relativeFilePath);
        String combinedPrompt = additionalPrompt + "\n\n" + fileContent;
        return getGeminiResponse(combinedPrompt);
    }
    public String getGeminiResponse(String prompt) {
        String url = GEMINI_API_URL + API_KEY;

        // Prepare request payload
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[] {
                        Map.of("parts", new Object[] {
                                Map.of("text", prompt)
                        })
                }
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // Send POST request to Gemini API
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);

            // Check if response contains candidates
            if (response.getBody() != null && response.getBody().containsKey("candidates")) {
                return response.getBody().get("candidates").toString();
            } else {
                return "Error: No valid response from Gemini API";
            }
        } catch (Exception e) {
            return "Error while contacting Gemini API: " + e.getMessage();
        }
    }
}