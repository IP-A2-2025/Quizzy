package com.backend.controller;

import com.backend.model.Grade;
import com.backend.service.GradeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/grades")
public class GradeController {



    private final GradeService gradeService;

    @GetMapping
    public ResponseEntity<List<Grade>> getAllGrades() {
        return ResponseEntity.ok(gradeService.getAllGrades());
    }
    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PostMapping
    public ResponseEntity<Grade> addGrade(@RequestBody Grade grade) {
        Grade savedGrade = gradeService.addGrade(grade);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGrade);
    }

    @GetMapping("/{testId}/{userId}")
    public ResponseEntity<Grade> getGrade(@PathVariable Long testId, @PathVariable Integer userId) {
        return gradeService.getGrade(testId, userId).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{testId}/{userId}")
    public ResponseEntity<Grade> updateGrade(@PathVariable Long testId, @PathVariable Integer userId, @RequestBody Grade gradeDetails) {
        Optional<Grade> updatedGrade = gradeService.updateGrade(testId, userId, gradeDetails);
        return updatedGrade.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{testId}/{userId}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long testId, @PathVariable Integer userId) {
        gradeService.deleteGrade(testId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Grade>> getGradesForUser(@PathVariable Integer userId) {
        List<Grade> grades = gradeService.getGradesByUserId(userId);
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/test/{testId}/average")
    public ResponseEntity<Double> getAverageGradeForTest(@PathVariable Long testId) {
        double average = gradeService.calculateAverageForTest(testId);
        return ResponseEntity.ok(average);
    }

}