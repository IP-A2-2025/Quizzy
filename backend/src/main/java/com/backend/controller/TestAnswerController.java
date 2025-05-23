package com.backend.controller;

import com.backend.dto.TestAnswerDTO;
import com.backend.service.TestAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/answers")
public class TestAnswerController {

    private final TestAnswerService testAnswerService;

    @Autowired
    public TestAnswerController(TestAnswerService testAnswerService) {
        this.testAnswerService = testAnswerService;
    }

    @GetMapping
    public ResponseEntity<Collection<TestAnswerDTO>> getAllAnswers() {
        return ResponseEntity.ok(testAnswerService.getAllAnswers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestAnswerDTO> getAnswerById(@PathVariable Long id) {
        return ResponseEntity.ok(testAnswerService.getAnswerById(id));
    }

    @PostMapping
    public ResponseEntity<TestAnswerDTO> createAnswer(@RequestBody TestAnswerDTO answer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(testAnswerService.createAnswer(answer));
    }

    @PostMapping("/save")
    public ResponseEntity<TestAnswerDTO> saveAnswer(@RequestBody TestAnswerDTO answer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(testAnswerService.saveAnswer(answer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestAnswerDTO> updateAnswer(@PathVariable Long id, @RequestBody TestAnswerDTO answer) {
        return ResponseEntity.ok(testAnswerService.updateAnswer(id, answer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long id) {
        testAnswerService.deleteAnswerById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<Collection<TestAnswerDTO>> getAnswersByQuestionId(@PathVariable Long questionId) {
        return ResponseEntity.ok(testAnswerService.getAnswersByQuestionId(questionId));
    }

    @GetMapping("/test/{testId}")
    public ResponseEntity<Collection<TestAnswerDTO>> getAnswersByTestId(@PathVariable Long testId) {
        return ResponseEntity.ok(testAnswerService.getAnswersByTestId(testId));
    }

    @GetMapping("/correct/question/{questionId}")
    public ResponseEntity<Collection<TestAnswerDTO>> getCorrectAnswersByQuestionId(@PathVariable Long questionId) {
        return ResponseEntity.ok(testAnswerService.getCorrectAnswersByQuestionId(questionId));
    }

    @GetMapping("/incorrect/question/{questionId}")
    public ResponseEntity<Collection<TestAnswerDTO>> getIncorrectAnswersByQuestionId(@PathVariable Long questionId) {
        return ResponseEntity.ok(testAnswerService.getIncorrectAnswersByQuestionId(questionId));
    }

    @GetMapping("/correct/test/{testId}")
    public ResponseEntity<Collection<TestAnswerDTO>> getCorrectAnswersByTestId(@PathVariable Long testId) {
        return ResponseEntity.ok(testAnswerService.getCorrectAnswersByTestId(testId));
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<TestAnswerDTO>> searchAnswersByOptionText(@RequestParam String keyword) {
        return ResponseEntity.ok(testAnswerService.findByOptionTextContaining(keyword));
    }

    @GetMapping("/count/question/{questionId}")
    public ResponseEntity<Long> countAnswersByQuestionId(@PathVariable Long questionId) {
        return ResponseEntity.ok(testAnswerService.countAnswersByQuestionId(questionId));
    }

    @GetMapping("/count/correct/question/{questionId}")
    public ResponseEntity<Long> countCorrectAnswersByQuestionId(@PathVariable Long questionId) {
        return ResponseEntity.ok(testAnswerService.countCorrectAnswersByQuestionId(questionId));
    }

    @GetMapping("/count/test/{testId}")
    public ResponseEntity<Long> countAnswersByTestId(@PathVariable Long testId) {
        return ResponseEntity.ok(testAnswerService.countAnswersByTestId(testId));
    }

    @GetMapping("/count/correct/test/{testId}")
    public ResponseEntity<Long> countCorrectAnswersByTestId(@PathVariable Long testId) {
        return ResponseEntity.ok(testAnswerService.countCorrectAnswersByTestId(testId));
    }
    @PostMapping("/bulk")
    public ResponseEntity<Collection<TestAnswerDTO>> createAnswers(@RequestBody Collection<TestAnswerDTO> answers) {
        return ResponseEntity.status(HttpStatus.CREATED).body(testAnswerService.createAnswers(answers));
    }

    @PostMapping("/bulk/save")
    public ResponseEntity<Collection<TestAnswerDTO>> saveAnswers(@RequestBody Collection<TestAnswerDTO> answers) {
        return ResponseEntity.status(HttpStatus.CREATED).body(testAnswerService.saveAnswers(answers));
    }
}