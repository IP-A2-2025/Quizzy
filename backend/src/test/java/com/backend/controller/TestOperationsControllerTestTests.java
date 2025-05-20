
package com.backend.controller;

import com.backend.model.TestAnswer;
import com.backend.model.TestEntity;
import com.backend.model.TestQuestion;
import com.backend.service.TestAnswerService;
import com.backend.service.TestQuestionService;
import com.backend.service.TestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;

@WebMvcTest(TestOperationsControllerTest.class)
public class TestOperationsControllerTestTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestService testService;

    @Autowired
    private TestQuestionService testQuestionService;

    @Autowired
    private TestAnswerService testAnswerService;

    private TestEntity mockTest;
    private TestQuestion mockQuestion;
    private TestAnswer mockAnswer;

    @BeforeEach
    void setUp() {
        mockTest = new TestEntity();
        mockTest.setId(1L);
        mockTest.setTitle("Test Automat");

        mockQuestion = new TestQuestion();
        mockQuestion.setId(2L);
        mockQuestion.setQuestionText("Întrebare 1");
        mockQuestion.setTest(mockTest);

        mockAnswer = new TestAnswer();
        mockAnswer.setId(3L);
        mockAnswer.setOptionText("Răspuns 1");
        mockAnswer.setCorrect(true);
        mockAnswer.setTestQuestion(mockQuestion);

        Mockito.when(testService.createTest(any())).thenReturn(mockTest);
        Mockito.when(testQuestionService.createQuestion(any())).thenReturn(mockQuestion);
        Mockito.when(testAnswerService.createAnswer(any())).thenReturn(mockAnswer);
    }

    @Test
    void testCreateTestScenario() throws Exception {
        mockMvc.perform(post("/test-operations/create-test-scenario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.testId").value(1L))
                .andExpect(jsonPath("$.questionId").value(2L))
                .andExpect(jsonPath("$.answerId").value(3L));
    }

    @Test
    void testDeleteTestScenario() throws Exception {
        Long testId = 1L;

        Mockito.doNothing().when(testService).deleteTestAndRelatedEntities(testId);
        Mockito.when(testQuestionService.getQuestionsByTestId(testId)).thenReturn(Collections.emptyList());
        Mockito.when(testAnswerService.getAnswersByTestId(testId)).thenReturn(Collections.emptyList());
        Mockito.doThrow(new jakarta.persistence.EntityNotFoundException()).when(testService).getTestById(testId);

        mockMvc.perform(delete("/test-operations/delete-test-scenario/{testId}", testId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.testExists").value(false))
                .andExpect(jsonPath("$.remainingQuestions").value(0))
                .andExpect(jsonPath("$.remainingAnswers").value(0));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public TestService testService() {
            return Mockito.mock(TestService.class);
        }

        @Bean
        public TestQuestionService testQuestionService() {
            return Mockito.mock(TestQuestionService.class);
        }

        @Bean
        public TestAnswerService testAnswerService() {
            return Mockito.mock(TestAnswerService.class);
        }
    }
}
