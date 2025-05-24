package com.backend.controller;

import com.backend.model.FlashcardProgress;
import com.backend.service.FlashcardProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/FlashcardProgress")
public class FlashcardProgressController {

    private final FlashcardProgressService flashcardProgressService;

    public FlashcardProgressController(FlashcardProgressService flashcardProgressService) {
        this.flashcardProgressService = flashcardProgressService;
    }

    @GetMapping
    public ResponseEntity<List<FlashcardProgress>> getAllFlashcardProgress() {
        return ResponseEntity.ok(flashcardProgressService.getAllFlashcardProgress());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlashcardProgress> getFlashcardProgressById(@PathVariable Long id) {
        Optional<FlashcardProgress> progress = flashcardProgressService.getFlashcardProgressById(id);
        return progress.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FlashcardProgress> createFlashcardProgress(@RequestBody FlashcardProgress flashcardProgress) {
        FlashcardProgress created = flashcardProgressService.createFlashcardProgress(flashcardProgress);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlashcardProgress> updateFlashcardProgress(
            @PathVariable Long id,
            @RequestBody FlashcardProgress flashcardProgress) {
        FlashcardProgress updated = flashcardProgressService.updateFlashcardProgress(id, flashcardProgress);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlashcardProgress(@PathVariable Long id) {
        flashcardProgressService.deleteFlashcardProgress(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FlashcardProgress>> getByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(flashcardProgressService.getByUserId(userId));
    }

    @GetMapping("/flashcard/{flashcardId}")
    public ResponseEntity<List<FlashcardProgress>> getByFlashcardId(@PathVariable Long flashcardId) {
        return ResponseEntity.ok(flashcardProgressService.getByFlashcardId(flashcardId));
    }

    @GetMapping("/due")
    public ResponseEntity<List<FlashcardProgress>> getDueProgress(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date date,
            @RequestParam("userId") Integer userId) {
        return ResponseEntity.ok(flashcardProgressService.getDueProgress(date, userId));
    }

    @GetMapping("/user/{userId}/material/{materialId}")
    public ResponseEntity<List<FlashcardProgress>> getByUserIdAndMaterialId(
            @PathVariable Integer userId,
            @PathVariable Long materialId) {
        return ResponseEntity.ok(flashcardProgressService.getByUserIdAndMaterialId(userId, materialId));
    }

    @GetMapping("/user/{userId}/level/{level}")
    public ResponseEntity<List<FlashcardProgress>> getByUserIdAndLevel(
            @PathVariable Integer userId,
            @PathVariable int level) {
        return ResponseEntity.ok(flashcardProgressService.getByUserIdAndLevel(userId, level));
    }

    @GetMapping("/user/{userId}/page/{pageIndex}")
    public ResponseEntity<List<FlashcardProgress>> getByUserIdAndPageIndex(
            @PathVariable Integer userId,
            @PathVariable Integer pageIndex) {
        return ResponseEntity.ok(flashcardProgressService.getByUserIdAndPageIndex(userId, pageIndex));
    }

    @GetMapping("/user/{userId}/course/{courseId}")
    public ResponseEntity<List<FlashcardProgress>> getByUserIdAndCourseId(
            @PathVariable Integer userId,
            @PathVariable Long courseId) {
        return ResponseEntity.ok(flashcardProgressService.getByUserIdAndCourseId(userId, courseId));
    }

    @GetMapping("/user/{userId}/question-type/{type}")
    public ResponseEntity<List<FlashcardProgress>> getByUserIdAndQuestionType(
            @PathVariable Integer userId,
            @PathVariable String type) {
        return ResponseEntity.ok(flashcardProgressService.getByUserIdAndQuestionType(userId, type));
    }

    @GetMapping("/flashcard/{flashcardId}/user/{userId}")
    public ResponseEntity<FlashcardProgress> getByFlashcardIdAndUserId(
            @PathVariable Long flashcardId,
            @PathVariable Integer userId) {
        FlashcardProgress progress = flashcardProgressService.getByFlashcardIdAndUserId(flashcardId, userId);
        if (progress != null) {
            return ResponseEntity.ok(progress);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

