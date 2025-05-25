package com.backend.utils;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
public class FlashcardBatchGenerator {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String GENERATE_URL = "http://localhost:3000/api/gemini/generate-with-prompt";
    private final String BASE_DIRECTORY = "courses";

    //@PostConstruct
    public void generateAllFlashcards() {
        File coursesFolder = new File(BASE_DIRECTORY);

        if (!coursesFolder.exists() || !coursesFolder.isDirectory()) {
            System.out.println("❌ Directorul '" + BASE_DIRECTORY + "' nu există!");
            return;
        }

        System.out.println("🚀 Încep procesarea din directorul: " + BASE_DIRECTORY);
        processDirectory(coursesFolder, "");
        System.out.println("✨ Procesarea completă!");
    }

    private void processDirectory(File directory, String relativePath) {
        File[] files = directory.listFiles();

        if (files == null) {
            System.out.println("⚠️ Nu pot accesa conținutul din: " + directory.getPath());
            return;
        }

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".txt") && !file.getName().contains("_flashcards")) {
                String fileRelativePath = relativePath.isEmpty() ?
                        file.getName() :
                        relativePath + "/" + file.getName();

                processTextFile(fileRelativePath);
            }
        }

        for (File file : files) {
            if (file.isDirectory()) {
                String newRelativePath = relativePath.isEmpty() ?
                        file.getName() :
                        relativePath + "/" + file.getName();

                System.out.println("📁 Intru în directorul: " + newRelativePath);
                processDirectory(file, newRelativePath);
            }
        }
    }

    private void processTextFile(String relativePath) {
        try {
            System.out.println("📄 Procesez fișierul: " + relativePath);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("inputFilePath", relativePath);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(GENERATE_URL, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("✅ Flashcard-uri generate pentru: " + relativePath);
                System.out.println("📝 Răspuns: " + response.getBody());
            } else {
                System.out.println("❌ Eroare HTTP " + response.getStatusCode() + " pentru " + relativePath);
            }

            Thread.sleep(1000);

        } catch (Exception e) {
            System.out.println("❌ Eroare la procesarea fișierului " + relativePath + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void generateFlashcardsForSpecificCourse(String courseName) {
        String courseDirectory = BASE_DIRECTORY + "/" + courseName;
        File courseFolder = new File(courseDirectory);

        if (!courseFolder.exists() || !courseFolder.isDirectory()) {
            System.out.println("❌ Cursul '" + courseName + "' nu există în " + BASE_DIRECTORY);
            return;
        }

        System.out.println("🚀 Procesez cursul: " + courseName);
        processDirectory(courseFolder, courseName);
        System.out.println("✨ Procesarea cursului " + courseName + " completă!");
    }
}