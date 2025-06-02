package com.backend.controller;

import com.backend.model.FlashcardSession;
import com.backend.service.FlashcardSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/FlashcardSession")
public class FlashcardSessionController {

    private final FlashcardSessionService sessionService;

    @Autowired
    public FlashcardSessionController(FlashcardSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public ResponseEntity<List<FlashcardSession>> getAllSessions() {
        return ResponseEntity.ok(sessionService.getAllSessions());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FlashcardSession>> getByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(sessionService.getSessionsByUserId(userId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<FlashcardSession>> getByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(sessionService.getSessionsByCourseId(courseId));
    }

    @GetMapping("/user/{userId}/course/{courseId}")
    public ResponseEntity<List<FlashcardSession>> getByUserAndCourse(@PathVariable Integer userId, @PathVariable Long courseId) {
        return ResponseEntity.ok(sessionService.getSessionsByUserAndCourse(userId, courseId));
    }

    @GetMapping("/user/{userId}/range")
    public ResponseEntity<List<FlashcardSession>> getByDateRangeAndUser(
            @PathVariable Integer userId,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end) {
        return ResponseEntity.ok(sessionService.getSessionsByDateRangeAndUserId(start, end, userId));
    }

    @GetMapping("/user/{userId}/total")
    public ResponseEntity<Integer> getTotalFlashcardsStudiedSince(
            @PathVariable Integer userId,
            @RequestParam("since") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date since) {
        return ResponseEntity.ok(sessionService.getTotalFlashcardsStudiedSince(userId, since));
    }

    @PostMapping
    public ResponseEntity<FlashcardSession> createSession(@RequestBody FlashcardSession session) {
        FlashcardSession created = sessionService.createSession(session);
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);//Cannot resolve method 'deleteSession' in 'FlashcardSessionService'
        return ResponseEntity.ok().build();
    }
}
