package com.backend.controller;

import com.backend.dto.TestAnswerDTO;
import com.backend.model.TestAnswer;
import com.backend.service.TestAnswerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/answers")
public class TestAnswerController {

    private final TestAnswerService testAnswerService;
    private static final Logger logger = LoggerFactory.getLogger(TestAnswerController.class);

    @Autowired
    public TestAnswerController(TestAnswerService testAnswerService) {
        this.testAnswerService = testAnswerService;
    }

    @GetMapping
    public ResponseEntity<Collection<TestAnswer>> getAllAnswers() {
        return ResponseEntity.ok(testAnswerService.getAllAnswers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestAnswer> getAnswerById(@PathVariable Long id) {
        return ResponseEntity.ok(testAnswerService.getAnswerById(id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<TestAnswer>> createAnswers(@Valid @RequestBody Collection<TestAnswerDTO> answerDTOs) {
        logger.info("Attempting to create {} answers", answerDTOs.size());
        var answers = testAnswerService.createAnswers(answerDTOs);
        try{
            return ResponseEntity.ok(answers);
        } catch (Exception e) {
            logger.error("Failed to create answers: {}", e.getMessage());
            throw e;
        } finally{
            for(var a : answers){
                logger.info("Test created successfully with ID: {}", a.getId());
            }
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestAnswer> updateAnswer(@PathVariable Long id, @RequestBody TestAnswerDTO testAnswerDTO) {
        logger.info("Attempting to update answer");
        try{
            return ResponseEntity.ok(testAnswerService.updateAnswer(id, testAnswerDTO));
        } catch (Exception e) {
            logger.error("Failed to update answer: {}", e.getMessage());
            throw e;
        } finally {
            logger.info("Answer updated successfully with ID: {}", id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAnswer(@PathVariable Long id) {
        logger.info("Attempting to delete answer with ID: {}", id);
        try {
            return ResponseEntity.ok(testAnswerService.deleteAnswerById(id));
        } catch (Exception e) {
            logger.error("Failed to delete answer: {}", e.getMessage());
            throw e;
        } finally {
            logger.info("Answer deleted successfully with ID: {}", id);
        }
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<Collection<TestAnswer>> getAnswersByQuestionId(@PathVariable Long questionId) {
        return ResponseEntity.ok(testAnswerService.getAnswersByQuestionId(questionId));
    }

    @GetMapping("/test/{testId}")
    public ResponseEntity<Collection<TestAnswer>> getAnswersByTestId(@PathVariable Long testId) {
        return ResponseEntity.ok(testAnswerService.getAnswersByTestId(testId));
    }

    @GetMapping("/correct/question/{questionId}")
    public ResponseEntity<Collection<TestAnswer>> getCorrectAnswersByQuestionId(@PathVariable Long questionId) {
        return ResponseEntity.ok(testAnswerService.getCorrectAnswersByQuestionId(questionId));
    }

    @GetMapping("/incorrect/question/{questionId}")
    public ResponseEntity<Collection<TestAnswer>> getIncorrectAnswersByQuestionId(@PathVariable Long questionId) {
        return ResponseEntity.ok(testAnswerService.getIncorrectAnswersByQuestionId(questionId));
    }

    @GetMapping("/correct/test/{testId}")
    public ResponseEntity<Collection<TestAnswer>> getCorrectAnswersByTestId(@PathVariable Long testId) {
        return ResponseEntity.ok(testAnswerService.getCorrectAnswersByTestId(testId));
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<TestAnswer>> searchAnswersByOptionText(@RequestParam String keyword) {
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
    @DeleteMapping("/multiple")
    public ResponseEntity<Integer> deleteMultipleAnswers(@RequestBody Collection<Long> answerIds) {
        int deletedCount = testAnswerService.deleteMultipleAnswers(answerIds);
        return ResponseEntity.ok(deletedCount);
    }

    @DeleteMapping("/question/{questionId}")
    public ResponseEntity<Integer> deleteAllAnswersForQuestion(@PathVariable Long questionId) {
        int deletedCount = testAnswerService.deleteAllAnswersForQuestion(questionId);
        return ResponseEntity.ok(deletedCount);
    }

    @DeleteMapping("/test/{testId}")
    public ResponseEntity<Integer> deleteAllAnswersForTest(@PathVariable Long testId) {
        int deletedCount = testAnswerService.deleteAllAnswersForTest(testId);
        return ResponseEntity.ok(deletedCount);
    }
}
