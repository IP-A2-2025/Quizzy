package com.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Service
public class GeminiService {
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=";
    @Value("${gemini.api.key}")
    private String API_KEY;

    private final RestTemplate restTemplate = new RestTemplate();

    public String processFile(String inputFilePath) throws IOException {
        String prompt = Files.readString(Path.of(inputFilePath));

        String response = getGeminiResponse(prompt);

        return response;
    }

    public String getGeminiResponse(String prompt) {
        System.out.println(API_KEY);
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

            // Check if response contains candidates (response structure might change, verify with actual API response)
            if (response.getBody() != null && response.getBody().containsKey("candidates")) {
                // Assuming the candidates are a list, return the first candidate's text (adjust based on the actual API response structure)
                return response.getBody().get("candidates").toString();
            } else {
                return "Error: No valid response from Gemini API";
            }
        } catch (Exception e) {
            // Handle any exceptions (e.g., network error, JSON parsing error)
            return "Error while contacting Gemini API: " + e.getMessage();
        }
    }
}