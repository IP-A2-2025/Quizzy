package com.backend.controller;

import com.backend.model.TestAnswer;
import com.backend.model.TestEntity;
import com.backend.model.TestQuestion;
import com.backend.service.TestAnswerService;
import com.backend.service.TestQuestionService;
import com.backend.service.TestService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test-operations")
public class TestOperationsControllerTest {

    @Autowired
    private TestService testService;

    @Autowired
    private TestQuestionService testQuestionService;

    @Autowired
    private TestAnswerService testAnswerService;

    @PostMapping("/create-test-scenario")
    public ResponseEntity<Map<String, Object>> createTestScenario() {
        // 1. Creează un test
        TestEntity test = new TestEntity();
        test.setTitle("Test Automat");
        test.setDescription("Test creat pentru verificarea operațiilor de ștergere");
        // Setează alte câmpuri necesare
        test = testService.createTest(test);

        // 2. Creează întrebări pentru test
        TestQuestion question1 = new TestQuestion();
        question1.setTest(test);
        question1.setQuestionText("Întrebare 1");
        question1.setPointValue(1.0f);
        question1 = testQuestionService.createQuestion(question1);

        // 3. Creează răspunsuri pentru întrebări
        TestAnswer answer1 = new TestAnswer();
        answer1.setTestQuestion(question1);
        answer1.setOptionText("Răspuns 1");
        answer1.setCorrect(true);
        answer1 = testAnswerService.createAnswer(answer1);

        // 4. Returnează IDs pentru verificare ulterioară
        Map<String, Object> result = new HashMap<>();
        result.put("testId", test.getId());
        result.put("questionId", question1.getId());
        result.put("answerId", answer1.getId());

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete-test-scenario/{testId}")
    public ResponseEntity<Map<String, Object>> deleteTestScenario(@PathVariable Long testId) {
        // Șterge testul și verifică dacă ștergerea a fost completă
        testService.deleteTestAndRelatedEntities(testId);

        // Verifică dacă testul a fost șters
        boolean testExists = false;
        try {
            testService.getTestById(testId);
            testExists = true;
        } catch (EntityNotFoundException e) {
            // Test a fost șters cu succes
        }

        // Verifică dacă mai există întrebări pentru test
        Collection<TestQuestion> remainingQuestions = testQuestionService.getQuestionsByTestId(testId);

        // Verifică dacă mai există răspunsuri pentru test
        Collection<TestAnswer> remainingAnswers = testAnswerService.getAnswersByTestId(testId);

        Map<String, Object> result = new HashMap<>();
        result.put("testExists", testExists);
        result.put("remainingQuestions", remainingQuestions.size());
        result.put("remainingAnswers", remainingAnswers.size());

        return ResponseEntity.ok(result);
    }
}