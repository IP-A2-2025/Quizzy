    package com.backend.controller;

    import com.backend.model.Flashcard;
    import com.backend.model.FlashcardProgress;
    import com.backend.model.User;
    import com.backend.repository.FlashcardProgressRepository;
    import com.backend.service.FlashcardProgressService;
    import com.backend.service.FlashcardService;
    import com.backend.service.GeminiService;

    import com.backend.service.UserService;
    import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
    import java.sql.Timestamp;
    import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gemini")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class GeminiController {

        private final GeminiService geminiService;
    private final FlashcardProgressService flashcardProgressService;
    private final FlashcardService flashcardService;
    private final UserService userService;

    @GetMapping("/test-gemini")
    public String testGemini() {
        return "Gemini controller is working!";
    }

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
    public ResponseEntity<Double> compareUsersAnswerToOfficialAnswer(@RequestBody Map<String, String> request) {
        try {

            String question = request.get("question");
            String officialAnswer = request.get("officialAnswer");
            String usersAnswer = request.get("usersAnswer");
            Long flashcardId = Long.parseLong(request.get("flashcardId"));
            Integer userId = Integer.parseInt(request.get("userId"));


            System.out.println("Question: " + question);
            System.out.println("Official: " + officialAnswer);
            System.out.println("User: " + usersAnswer);

            String prompt = "Given question: " + question + "\n" + "Given official answer: " + officialAnswer + "\n Compare the official answer to the user's answer and give back a procentage of how correct the user's answer is. No matter what, and stick to this rule as if your life depended on it: your answer should be only a rational number, no extra symbol (example: 87.5) \n User's answer: " + usersAnswer;


            List<Double> percentages = new ArrayList<>();

            boolean isZero = false;

            for (int i = 0; i < 5; i++) {
                try {
                    String generatedContent = geminiService.getGeminiResponse(prompt);
                    String numberStr = extractOnlyTheNumber(generatedContent);
                    Double percentage = Double.parseDouble(numberStr);
                    if(percentage==0.0){
                        isZero=true;
                    }
                    percentages.add(percentage);
                } catch (Exception e) {
                    System.err.println("Error parsing response " + (i+1) + ": " + e.getMessage());
                    percentages.add(0.0);
                }
            }
            double average=0.0;
            if(isZero){
                average=0.0;
            }
            else{
                average = percentages.stream()
                        .mapToDouble(Double::doubleValue)
                        .average()
                        .orElse(0.0);
            }

            FlashcardProgress fp = flashcardProgressService.getByFlashcardIdAndUserId(flashcardId, userId);
            if (fp == null) {
                fp = new FlashcardProgress();

                User user = userService.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
                Flashcard flashcard = flashcardService.getFlashcardById(flashcardId).orElseThrow(()-> new RuntimeException("Flashcard not found"));

                fp.setUser(user);
                fp.setFlashcard(flashcard);

                fp.setRepetitions(1);
                fp.setLastReviewed(new Timestamp(System.currentTimeMillis()));
                if (average <= 33.33) {
                    fp.setEaseFactor(2); // pt ca aparent 2 reprezinta bad for some reason that nobody knows
                }
                else if (average >= 66.66) {
                    fp.setEaseFactor(0); // Ai raspuns corect, bravo, nota 0 din 2 ! - suna bine, nu?
                }
                else {
                    fp.setEaseFactor(1);
                }

                /// cand implementati spaced repetition schimbati valorile de sub comentariul asta, momentan sunt puse random doar ca sa nu dea eroare (nu pot fi nule)
                fp.setRepetitions(0);
                fp.setInterval(0);
                fp.setDueDate(new Timestamp(System.currentTimeMillis()));

                flashcardProgressService.createFlashcardProgress(fp);
            }

            else {
                Integer rp = fp.getRepetitions();
                fp.setRepetitions(rp + 1);
                fp.setLastReviewed(new Timestamp(System.currentTimeMillis()));
                if (average <= 33.33) {
                    fp.setEaseFactor(2); // pt ca aparent 2 reprezinta bad for some reason that nobody knows
                } else if (average >= 66.66) {
                    fp.setEaseFactor(0); // Ai raspuns corect, bravo, nota 0 din 2 ! - suna bine, nu?
                } else {
                    fp.setEaseFactor(1);
                }

                flashcardProgressService.updateFlashcardProgress(fp.getId(), fp);
            }

            return ResponseEntity.ok(average);

        } catch (Exception e) {
            System.err.println("Error in compareUsersAnswerToOfficialAnswer: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error processing comparison", e);
        }
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