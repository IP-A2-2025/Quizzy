package com.backend.controller;

import com.backend.model.TestAnswer;
import com.backend.service.TestAnswerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TestAnswerControllerTest {

    @Mock
    private TestAnswerService testAnswerService;

    @InjectMocks
    private TestAnswerController testAnswerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllAnswers_ShouldReturn200() {
        List<TestAnswer> answers = Arrays.asList(new TestAnswer(), new TestAnswer());
        when(testAnswerService.getAllAnswers()).thenReturn(answers);

        ResponseEntity<Collection<TestAnswer>> response = testAnswerController.getAllAnswers();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(answers, response.getBody());
    }

    @Test
    void getAnswerById_ShouldReturn200() {
        TestAnswer answer = new TestAnswer();
        when(testAnswerService.getAnswerById(1L)).thenReturn(answer);

        ResponseEntity<TestAnswer> response = testAnswerController.getAnswerById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(answer, response.getBody());
    }

    @Test
    void createAnswer_ShouldReturn201() {
        TestAnswer answer = new TestAnswer();
        when(testAnswerService.createAnswer(answer)).thenReturn(answer);

        ResponseEntity<TestAnswer> response = testAnswerController.createAnswer(answer);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(answer, response.getBody());
    }

    @Test
    void updateAnswer_ShouldReturn200() {
        TestAnswer answer = new TestAnswer();
        when(testAnswerService.updateAnswer(eq(1L), any(TestAnswer.class))).thenReturn(answer);

        ResponseEntity<TestAnswer> response = testAnswerController.updateAnswer(1L, answer);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(answer, response.getBody());
    }

    @Test
    void deleteAnswer_ShouldReturn204() {
        doNothing().when(testAnswerService).deleteAnswerById(1L);

        ResponseEntity<Void> response = testAnswerController.deleteAnswer(1L);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void getAnswersByQuestionId_ShouldReturn200() {
        List<TestAnswer> answers = Arrays.asList(new TestAnswer());
        when(testAnswerService.getAnswersByQuestionId(1L)).thenReturn(answers);

        ResponseEntity<Collection<TestAnswer>> response = testAnswerController.getAnswersByQuestionId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(answers, response.getBody());
    }

    @Test
    void getAnswersByTestId_ShouldReturn200() {
        List<TestAnswer> answers = Arrays.asList(new TestAnswer());
        when(testAnswerService.getAnswersByTestId(1L)).thenReturn(answers);

        ResponseEntity<Collection<TestAnswer>> response = testAnswerController.getAnswersByTestId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(answers, response.getBody());
    }

    @Test
    void getCorrectAnswersByQuestionId_ShouldReturn200() {
        List<TestAnswer> answers = Arrays.asList(new TestAnswer());
        when(testAnswerService.getCorrectAnswersByQuestionId(1L)).thenReturn(answers);

        ResponseEntity<Collection<TestAnswer>> response = testAnswerController.getCorrectAnswersByQuestionId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(answers, response.getBody());
    }

    @Test
    void getIncorrectAnswersByQuestionId_ShouldReturn200() {
        List<TestAnswer> answers = Arrays.asList(new TestAnswer());
        when(testAnswerService.getIncorrectAnswersByQuestionId(1L)).thenReturn(answers);

        ResponseEntity<Collection<TestAnswer>> response = testAnswerController.getIncorrectAnswersByQuestionId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(answers, response.getBody());
    }

    @Test
    void getCorrectAnswersByTestId_ShouldReturn200() {
        List<TestAnswer> answers = Arrays.asList(new TestAnswer());
        when(testAnswerService.getCorrectAnswersByTestId(1L)).thenReturn(answers);

        ResponseEntity<Collection<TestAnswer>> response = testAnswerController.getCorrectAnswersByTestId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(answers, response.getBody());
    }

    @Test
    void searchAnswersByOptionText_ShouldReturn200() {
        List<TestAnswer> answers = Arrays.asList(new TestAnswer());
        when(testAnswerService.findByOptionTextContaining("keyword")).thenReturn(answers);

        ResponseEntity<Collection<TestAnswer>> response = testAnswerController.searchAnswersByOptionText("keyword");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(answers, response.getBody());
    }

    @Test
    void countAnswersByQuestionId_ShouldReturn200() {
        when(testAnswerService.countAnswersByQuestionId(1L)).thenReturn(4L);

        ResponseEntity<Long> response = testAnswerController.countAnswersByQuestionId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(4L, response.getBody());
    }

    @Test
    void countCorrectAnswersByQuestionId_ShouldReturn200() {
        when(testAnswerService.countCorrectAnswersByQuestionId(1L)).thenReturn(2L);

        ResponseEntity<Long> response = testAnswerController.countCorrectAnswersByQuestionId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2L, response.getBody());
    }

    @Test
    void countAnswersByTestId_ShouldReturn200() {
        when(testAnswerService.countAnswersByTestId(1L)).thenReturn(10L);

        ResponseEntity<Long> response = testAnswerController.countAnswersByTestId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(10L, response.getBody());
    }

    @Test
    void countCorrectAnswersByTestId_ShouldReturn200() {
        when(testAnswerService.countCorrectAnswersByTestId(1L)).thenReturn(5L);

        ResponseEntity<Long> response = testAnswerController.countCorrectAnswersByTestId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(5L, response.getBody());
    }
}
