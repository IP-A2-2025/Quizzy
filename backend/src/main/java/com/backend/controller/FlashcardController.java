package com.backend.controller;

import com.backend.model.Flashcard;
import com.backend.service.FlashcardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/flashcards")
public class FlashcardController {

    private final FlashcardService flashcardService;

    @Autowired
    public FlashcardController(FlashcardService flashcardService) {
        this.flashcardService = flashcardService;
    }

    @GetMapping
    public ResponseEntity<List<Flashcard>> getAllFlashcards() {
        return ResponseEntity.ok(flashcardService.getAllFlashcards());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flashcard> getFlashcardById(@PathVariable Long id) {
        return flashcardService.getFlashcardById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Flashcard> createFlashcard(@RequestBody Flashcard flashcard) {
        return ResponseEntity.ok(flashcardService.createFlashcard(flashcard));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlashcard(@PathVariable Long id) {
        boolean deleted = flashcardService.deleteFlashcard(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Flashcard>> getByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(flashcardService.getFlashcardsByUserId(userId));
    }

    @GetMapping("/material/{materialId}")
    public ResponseEntity<List<Flashcard>> getByMaterialId(@PathVariable Long materialId) {
        return ResponseEntity.ok(flashcardService.getFlashcardsByMaterialId(materialId));
    }

    @GetMapping("/due")
    public ResponseEntity<List<Flashcard>> getDueFlashcards(@RequestParam Integer userId, @RequestParam Date date) {
        return ResponseEntity.ok(flashcardService.getDueFlashcards(userId, date));
    }

    @GetMapping("/level")
    public ResponseEntity<List<Flashcard>> getByLevel(@RequestParam Integer userId, @RequestParam int level) {
        return ResponseEntity.ok(flashcardService.getFlashcardsByLevel(userId, level));
    }

    @GetMapping("/course")
    public ResponseEntity<List<Flashcard>> getByCourse(@RequestParam Integer userId, @RequestParam Long courseId) {
        return ResponseEntity.ok(flashcardService.getFlashcardsByCourseId(userId, courseId));
    }

    @GetMapping("/type")
    public ResponseEntity<List<Flashcard>> getByType(@RequestParam Integer userId, @RequestParam String type) {
        return ResponseEntity.ok(flashcardService.getFlashcardsByQuestionType(userId, type));
    }
}
