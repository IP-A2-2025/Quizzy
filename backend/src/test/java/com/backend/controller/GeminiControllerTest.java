package com.backend.controller;

import com.backend.config.GeminiTestConfig;
import com.backend.service.GeminiService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Import(GeminiTestConfig.class)
class GeminiControllerTest {

    @Autowired
    private GeminiController geminiController;

    @Autowired
    private FlashcardController flashcardController;

    @Autowired
    private AnswerFCController answerFCController;

    @Autowired
    private GeminiService geminiService;

    @TempDir
    Path tempDir;

    private Path testFilePath;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() throws IOException {
        mockMvc = MockMvcBuilders.standaloneSetup(
                geminiController, flashcardController, answerFCController
        ).build();

        testFilePath = tempDir.resolve("test-content.txt");
        Files.writeString(testFilePath, "This is a test document for generating flashcards.\n\nJava is an object-oriented programming language.");

        String generatedContent = """
            --FlashCardSeparator-- Single [Ce este Java?] --InteriorSeparator-- [Un limbaj OOP] --InteriorSeparator--[Difficulty: easy] --FlashCardSeparator--
            --FlashCardSeparator-- Multiple [Ce face Spring Boot?] --InteriorSeparator--
            1.(right) [Crează aplicații rapid]
            2.(wrong) [Este un server]
            3.(wrong) [Un IDE]
            4.(wrong) [Un limbaj de programare]
            --InteriorSeparator--[Difficulty: medium]
            --FlashCardSeparator--
            """;

        when(geminiService.processFileWithPrompt(eq(testFilePath.toString()), anyString()))
                .thenReturn(generatedContent);
    }

    @Test
    void testGeminiResponseGeneratesValidFlashcardFile() throws Exception {
        String response = geminiController.generateResponseWithPrompt(testFilePath.toString());

        assertTrue(response.contains("Saved to"), "Response does not conține 'Saved to'");

        String outputPath = response.substring(response.indexOf("Saved to") + 9).trim();
        Path savedFilePath = Path.of(outputPath);
        assertTrue(Files.exists(savedFilePath), "Fișierul nu a fost salvat: " + savedFilePath);

        String savedContent = Files.readString(savedFilePath);

        // Verificări de conținut de bază
        assertTrue(savedContent.contains("[Difficulty: easy]"), "Lipsește Difficulty: easy");
        assertTrue(savedContent.contains("[Difficulty: medium]"), "Lipsește Difficulty: medium");
        assertTrue(savedContent.contains("--FlashCardSeparator--"), "Separatorul lipsește");

        // Regex pentru validare strictă
        Pattern flashcardPattern = Pattern.compile(
                "--FlashCardSeparator--\\s*(Single|Multiple)\\s*\\[.*?]((.|\\s)*?)--InteriorSeparator--\\s*\\[Difficulty: (easy|medium|hard)]\\s*--FlashCardSeparator--",
                Pattern.MULTILINE
        );

        Matcher matcher = flashcardPattern.matcher(savedContent);

        int count = 0;
        while (matcher.find()) {
            count++;
            String type = matcher.group(1);
            String difficulty = matcher.group(4);

            // Validări de tip și dificultate
            assertTrue(type.equals("Single") || type.equals("Multiple"), "Tip invalid: " + type);
            assertTrue(difficulty.matches("easy|medium|hard"), "Dificultate invalidă: " + difficulty);

            // Verificare pentru întrebări multiple (opțiuni corecte/greșite)
            if (type.equals("Multiple")) {
                String flashcardContent = matcher.group(2);
                Pattern optionsPattern = Pattern.compile(
                        "(\\d+\\.\\(right\\)|\\d+\\.\\(wrong\\))\\s*\\[.*?\\]"
                );
                Matcher optionsMatcher = optionsPattern.matcher(flashcardContent);
                int rightAnswers = 0;
                int wrongAnswers = 0;
                while (optionsMatcher.find()) {
                    String option = optionsMatcher.group(0);
                    if (option.contains("(right)")) {
                        rightAnswers++;
                    } else if (option.contains("(wrong)")) {
                        wrongAnswers++;
                    }
                }
                // Asigură-te că există cel puțin o opțiune corectă și mai multe opțiuni greșite
                assertTrue(rightAnswers > 0, "Trebuie să existe cel puțin o opțiune corectă.");
                assertTrue(wrongAnswers >= 1, "Trebuie să existe cel puțin o opțiune greșită.");
            }
        }

        // Verifică numărul corect de flashcarduri
        assertEquals(2, count, "Nu au fost găsite exact 2 flashcarduri în conținutul generat.");

        // Verifică prezența întrebărilor
        assertTrue(savedContent.contains("Ce este Java?"), "Întrebarea pentru 'Single' lipsește");
        assertTrue(savedContent.contains("Ce face Spring Boot?"), "Întrebarea pentru 'Multiple' lipsește");

        // Verifică prezența răspunsurilor corecte și greșite
        assertTrue(savedContent.contains("Crează aplicații rapid"), "Răspunsul corect lipsește pentru 'Multiple'");
        assertTrue(savedContent.contains("Este un server"), "Răspunsul greșit lipsește pentru 'Multiple'");
    }
}



