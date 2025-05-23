// TestQuestionController.java
package com.backend.controller;

import com.backend.dto.TestQuestionDTO;
import com.backend.service.TestQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/questions")
public class TestQuestionController {

    private final TestQuestionService testQuestionService;

    @Autowired
    public TestQuestionController(TestQuestionService testQuestionService) {
        this.testQuestionService = testQuestionService;
    }

    @GetMapping
    public ResponseEntity<Collection<TestQuestionDTO>> getAllTestQuestions() {
        return ResponseEntity.ok(testQuestionService.getAllQuestions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestQuestionDTO> getTestQuestionById(@PathVariable Long id) {
        return ResponseEntity.ok(testQuestionService.getQuestionById(id));
    }

    @PostMapping
    public ResponseEntity<TestQuestionDTO> createTestQuestion(@RequestBody TestQuestionDTO testQuestion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(testQuestionService.createQuestion(testQuestion));
    }

    @PostMapping("/save")
    public ResponseEntity<TestQuestionDTO> saveQuestion(@RequestBody TestQuestionDTO testQuestion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(testQuestionService.saveQuestion(testQuestion));
    }
    @PostMapping("/bulk")
    public ResponseEntity<List<TestQuestionDTO>> createMultipleQuestions(@RequestBody List<TestQuestionDTO> questions) {
        return ResponseEntity.status(HttpStatus.CREATED).body(testQuestionService.createMultipleQuestions(questions));
    }

    @PostMapping("/bulk/save")
    public ResponseEntity<List<TestQuestionDTO>> saveMultipleQuestions(@RequestBody List<TestQuestionDTO> questions) {
        return ResponseEntity.status(HttpStatus.CREATED).body(testQuestionService.saveMultipleQuestions(questions));
    }

    @PutMapping("/bulk")
    public ResponseEntity<List<TestQuestionDTO>> updateMultipleQuestions(@RequestBody List<TestQuestionDTO> questions) {
        return ResponseEntity.ok(testQuestionService.updateMultipleQuestions(questions));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestQuestionDTO> updateTestQuestion(@PathVariable Long id, @RequestBody TestQuestionDTO testQuestion) {
        return ResponseEntity.ok(testQuestionService.updateQuestion(id, testQuestion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestQuestion(@PathVariable Long id) {
        testQuestionService.deleteQuestionById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/test/{testId}")
    public ResponseEntity<Collection<TestQuestionDTO>> getTestQuestionsByTestId(@PathVariable Long testId) {
        return ResponseEntity.ok(testQuestionService.getQuestionsByTestId(testId));
    }

    @GetMapping("/count/test/{testId}")
    public ResponseEntity<Long> countQuestionsByTestId(@PathVariable Long testId) {
        return ResponseEntity.ok(testQuestionService.countQuestionsByTest(testId));
    }

    @GetMapping("/total-points/test/{testId}")
    public ResponseEntity<Float> getTotalPointsByTest(@PathVariable Long testId) {
        return ResponseEntity.ok(testQuestionService.getTotalPointsByTest(testId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<TestQuestionDTO>> getQuestionsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(testQuestionService.getQuestionsByCourse(courseId));
    }

    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<TestQuestionDTO>> getQuestionsByProfessor(@PathVariable Integer professorId) {
        return ResponseEntity.ok(testQuestionService.getQuestionsByProfessor(professorId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TestQuestionDTO>> searchQuestionsByText(@RequestParam String keyword) {
        return ResponseEntity.ok(testQuestionService.searchQuestionsByText(keyword));
    }

    @GetMapping("/points/{points}")
    public ResponseEntity<List<TestQuestionDTO>> getQuestionsByPoints(@PathVariable Float points) {
        return ResponseEntity.ok(testQuestionService.getQuestionsByPoints(points));
    }

    @GetMapping("/min-points/{minPoints}")
    public ResponseEntity<List<TestQuestionDTO>> getQuestionsByMinPoints(@PathVariable Float minPoints) {
        return ResponseEntity.ok(testQuestionService.getQuestionsByMinPoints(minPoints));
    }

    @GetMapping("/count/course/{courseId}")
    public ResponseEntity<Long> countQuestionsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(testQuestionService.countQuestionsByCourse(courseId));
    }

    @GetMapping("/count/professor/{professorId}")
    public ResponseEntity<Long> countQuestionsByProfessor(@PathVariable Integer professorId) {
        return ResponseEntity.ok(testQuestionService.countQuestionsByProfessor(professorId));
    }

    @GetMapping("/count/points/{points}")
    public ResponseEntity<Long> countQuestionsByPoints(@PathVariable Float points) {
        return ResponseEntity.ok(testQuestionService.countQuestionsByPoints(points));
    }

    @GetMapping("/count/min-points/{minPoints}")
    public ResponseEntity<Long> countQuestionsByMinPoints(@PathVariable Float minPoints) {
        return ResponseEntity.ok(testQuestionService.countQuestionsByMinPoints(minPoints));
    }

    @GetMapping("/average-points/test/{testId}")
    public ResponseEntity<Float> getAveragePointsPerQuestion(@PathVariable Long testId) {
        return ResponseEntity.ok(testQuestionService.getAveragePointsPerQuestion(testId));
    }

    @GetMapping("/sorted/test/{testId}")
    public ResponseEntity<List<TestQuestionDTO>> getQuestionsByTestIdSortedByPoints(@PathVariable Long testId) {
        return ResponseEntity.ok(testQuestionService.getQuestionsByTestIdSortedByPoints(testId));
    }

    @GetMapping("/test/{testId}/min-points/{minPoints}")
    public ResponseEntity<List<TestQuestionDTO>> getQuestionsByTestAndMinPoints(
            @PathVariable Long testId,
            @PathVariable Float minPoints) {
        return ResponseEntity.ok(testQuestionService.getQuestionsByTestAndMinPoints(testId, minPoints));
    }
}
