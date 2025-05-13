package com.backend.controller;

import com.backend.service.GeminiService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/gemini")
@RequiredArgsConstructor
public class GeminiController {

    private final GeminiService geminiService;

    @PostMapping("/generate")
    public String generateResponse(@RequestParam String inputFilePath) throws IOException {
        return geminiService.processFile(inputFilePath);
    }

    @PostMapping("/generate-with-prompt")
    public String generateResponseWithPrompt(
            @RequestParam String inputFilePath) throws IOException {

        String prompt = "Create 35 flashcards with essential information from the material provided. Format single choice answers as:\n\n--FlashCardSeparator--\nSingle\n--InteriorSeparator--\ndetailed question\n--InteriorSeparator--\nanswer\n--InteriorSeparator--\ndifficulty\n--InteriorSeparator--\npageIndex\n--FlashCardSeparator--\n\nFormat multiple choice answers as:\n\n--FlashCardSeparator--\nMultiple\n--InteriorSeparator--\ndetailed question\n--InteriorSeparator--\n(right) answer option\n(right/wrong) answer option\n(wrong) answer option\n(wrong) answer option\n--InteriorSeparator--\ndifficulty\n--InteriorSeparator--\npageIndex\n--FlashCardSeparator--\n\nEnsure:\n- Mix of single and multiple choice questions\n- Difficulties: easy, medium, hard\n- Minimum 10 hard questions\n- 1-2 correct answers for multiple choice\n- No emojis or additional commentary";

        // Get the generated content from Gemini API
        String generatedContent = geminiService.processFileWithPrompt(inputFilePath, prompt);

        // Get the base path to the courses directory
        String coursesBasePath = System.getProperty("user.dir") + File.separator + "courses";

        // Parse the relative file path to determine output directory and filename
        Path relativePath = Paths.get(inputFilePath);
        String filename = relativePath.getFileName().toString();
        String filenameWithoutExtension = filename.substring(0, filename.lastIndexOf('.'));
        String outputFilename = filenameWithoutExtension + "_flashcards.txt";

        // Create the output path in the same directory as the input file
        Path outputPath = Paths.get(coursesBasePath).resolve(relativePath.getParent()).resolve(outputFilename);

        try {
            // Ensure the directory exists
            Files.createDirectories(outputPath.getParent());

            // Write the content to the file
            Files.writeString(outputPath, generatedContent);

            return "ok - Saved to " + outputPath;
        } catch (Exception e) {
            return "Error saving flashcards: " + e.getMessage();
        }
    }

    @PostMapping("/compare-users-answer-to-the-official-answer")
    public Double compareUsersAnswerToOfficialAnswer(@RequestParam String question, @RequestParam String officialAnswer, @RequestParam String usersAnswer) throws IOException {
        String prompt = "Given question: " + question + "\n" + "Given official answer: " + officialAnswer + "\n Compare the official answer to the user's answer and give back a procentage of how correct the user's answer is. Your answer should be only a rational number, no extra symbol (example: 87.5) \n User's answer: " + usersAnswer;

        // Get the generated content from Gemini API
        String generatedContent1 = geminiService.getGeminiResponse(prompt);
        String generatedContent2 = geminiService.getGeminiResponse(prompt);
        String generatedContent3 = geminiService.getGeminiResponse(prompt);
        String generatedContent4 = geminiService.getGeminiResponse(prompt);
        String generatedContent5 = geminiService.getGeminiResponse(prompt);

        Double percentage1 = Double.parseDouble(extractOnlyTheNumber(generatedContent1));
        Double percentage2 = Double.parseDouble(extractOnlyTheNumber(generatedContent2));
        Double percentage3 = Double.parseDouble(extractOnlyTheNumber(generatedContent3));
        Double percentage4 = Double.parseDouble(extractOnlyTheNumber(generatedContent4));
        Double percentage5 = Double.parseDouble(extractOnlyTheNumber(generatedContent5));

        if(percentage1 == 0.0 || percentage2 == 0.0 || percentage3 == 0.0 || percentage4 == 0.0 || percentage5 == 0.0) {
            return 0.0;
        }

        return (percentage1 + percentage2 + percentage3 + percentage4 + percentage5) / 5.0;
    }

    private String extractOnlyTheNumber(String json) {
        int start = json.indexOf("text=");
        if (start == -1) {
            throw new IllegalArgumentException("Could not find 'text=' in the response: " + json);
        }
        start += 5;

        int end = json.indexOf("\\n", start);
        if (end == -1) end = json.indexOf("\n", start);
        if (end == -1) end = json.indexOf("}", start);
        if (end == -1) end = json.length(); // fallback

        return json.substring(start, end).trim();
    }
}