package com.backend.controller;

import com.backend.utils.FlashcardBatchGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flashcards")
@RequiredArgsConstructor
public class FlashcardGeneratorController {

    private final FlashcardBatchGenerator flashcardBatchGenerator;

    @PostMapping("/generate-all")
    public ResponseEntity<String> generateFlashcards() {
        try {
            flashcardBatchGenerator.generateAllFlashcards();
            return ResponseEntity.ok("✅ Generarea flashcard-urilor s-a încheiat cu succes!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Eroare la generarea flashcard-urilor: " + e.getMessage());
        }
    }
}

