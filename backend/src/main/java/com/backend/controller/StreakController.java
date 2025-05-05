package com.backend.controller;

import com.backend.model.Streak;
import com.backend.service.StreakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/Streak")
public class StreakController {

    private final StreakService streakService;

    @Autowired
    public StreakController(StreakService streakService) {
        this.streakService = streakService;
    }

    @GetMapping
    public ResponseEntity<List<Streak>> getAllStreaks() {
        List<Streak> streaks = streakService.getAllStreaks();
        return ResponseEntity.ok(streaks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Streak> getStreakById(@PathVariable Long id) {
        return streakService.getStreakById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Streak>> getStreaksByUserId(@PathVariable Integer userId) {
        List<Streak> streaks = streakService.getStreaksByUserId(userId);
        return ResponseEntity.ok(streaks);
    }

    @GetMapping("/user/{userId}/date/{date}")
    public ResponseEntity<Streak> getStreakByUserIdAndDate(@PathVariable Integer userId, @PathVariable Date date) {
        return streakService.getStreakByUserIdAndDate(userId, date)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/min/{minStreak}")
    public ResponseEntity<List<Streak>> getStreaksByMinimumStreak(@PathVariable int minStreak) {
        List<Streak> streaks = streakService.getStreaksByMinimumStreak(minStreak);
        return ResponseEntity.ok(streaks);
    }

    @GetMapping("/top")
    public ResponseEntity<List<Streak>> getTopStreaks() {
        List<Streak> topStreaks = streakService.getTopStreaks();
        return ResponseEntity.ok(topStreaks);
    }

    @GetMapping("/average")
    public ResponseEntity<Float> getAverageStreak() {
        Float average = streakService.getAverageStreak();
        return ResponseEntity.ok(average);
    }

    @PostMapping
    public ResponseEntity<Streak> createStreak(@RequestBody Streak streak) {
        Streak createdStreak = streakService.createStreak(streak);
        return ResponseEntity.ok(createdStreak);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStreak(@PathVariable Long id) {
        streakService.deleteStreak(id);
        return ResponseEntity.ok().build();
    }
}
