package com.backend.controller;

import com.backend.model.TestQuestion;
import com.backend.service.TestQuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TestQuestionControllerTest {

    @Mock
    private TestQuestionService testQuestionService;

    @InjectMocks
    private TestQuestionController testQuestionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTestQuestions_ShouldReturnOk() {
        List<TestQuestion> questions = Arrays.asList(new TestQuestion(), new TestQuestion());
        when(testQuestionService.getAllQuestions()).thenReturn(questions);

        ResponseEntity<Collection<TestQuestion>> response = testQuestionController.getAllTestQuestions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(questions, response.getBody());
    }

    @Test
    void getTestQuestionById_ShouldReturnOk() {
        TestQuestion question = new TestQuestion();
        when(testQuestionService.getQuestionById(1L)).thenReturn(question);

        ResponseEntity<TestQuestion> response = testQuestionController.getTestQuestionById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(question, response.getBody());
    }

    @Test
    void createTestQuestion_ShouldReturnCreated() {
        TestQuestion question = new TestQuestion();
        when(testQuestionService.createQuestion(question)).thenReturn(question);

        ResponseEntity<TestQuestion> response = testQuestionController.createTestQuestion(question);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(question, response.getBody());
    }

    @Test
    void updateTestQuestion_ShouldReturnOk() {
        TestQuestion question = new TestQuestion();
        when(testQuestionService.updateQuestion(eq(1L), any(TestQuestion.class))).thenReturn(question);

        ResponseEntity<TestQuestion> response = testQuestionController.updateTestQuestion(1L, question);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(question, response.getBody());
    }

    @Test
    void deleteTestQuestion_ShouldReturnNoContent() {
        doNothing().when(testQuestionService).deleteQuestionById(1L);

        ResponseEntity<Void> response = testQuestionController.deleteTestQuestion(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void getTestQuestionsByTestId_ShouldReturnOk() {
        List<TestQuestion> questions = Arrays.asList(new TestQuestion());
        when(testQuestionService.getQuestionsByTestId(1L)).thenReturn(questions);

        ResponseEntity<Collection<TestQuestion>> response = testQuestionController.getTestQuestionsByTestId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(questions, response.getBody());
    }

    @Test
    void countQuestionsByTestId_ShouldReturnOk() {
        when(testQuestionService.countQuestionsByTest(1L)).thenReturn(5L);

        ResponseEntity<Long> response = testQuestionController.countQuestionsByTestId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5L, response.getBody());
    }

    @Test
    void getTotalPointsByTest_ShouldReturnOk() {
        when(testQuestionService.getTotalPointsByTest(1L)).thenReturn(25.0F);

        ResponseEntity<Float> response = testQuestionController.getTotalPointsByTest(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(25.0F, response.getBody());
    }

    @Test
    void getQuestionsByCourse_ShouldReturnOk() {
        List<TestQuestion> questions = Arrays.asList(new TestQuestion());
        when(testQuestionService.getQuestionsByCourse(1L)).thenReturn(questions);

        ResponseEntity<List<TestQuestion>> response = testQuestionController.getQuestionsByCourse(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(questions, response.getBody());
    }

    @Test
    void getQuestionsByProfessor_ShouldReturnOk() {
        List<TestQuestion> questions = Arrays.asList(new TestQuestion());
        when(testQuestionService.getQuestionsByProfessor(1)).thenReturn(questions);

        ResponseEntity<List<TestQuestion>> response = testQuestionController.getQuestionsByProfessor(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(questions, response.getBody());
    }

    @Test
    void searchQuestionsByText_ShouldReturnOk() {
        List<TestQuestion> questions = Arrays.asList(new TestQuestion());
        when(testQuestionService.searchQuestionsByText("sample")).thenReturn(questions);

        ResponseEntity<List<TestQuestion>> response = testQuestionController.searchQuestionsByText("sample");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(questions, response.getBody());
    }

    @Test
    void getQuestionsByPoints_ShouldReturnOk() {
        List<TestQuestion> questions = Arrays.asList(new TestQuestion());
        when(testQuestionService.getQuestionsByPoints(5.0F)).thenReturn(questions);

        ResponseEntity<List<TestQuestion>> response = testQuestionController.getQuestionsByPoints(5.0F);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(questions, response.getBody());
    }

    @Test
    void getQuestionsByMinPoints_ShouldReturnOk() {
        List<TestQuestion> questions = Arrays.asList(new TestQuestion());
        when(testQuestionService.getQuestionsByMinPoints(3.0F)).thenReturn(questions);

        ResponseEntity<List<TestQuestion>> response = testQuestionController.getQuestionsByMinPoints(3.0F);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(questions, response.getBody());
    }

    @Test
    void countQuestionsByCourse_ShouldReturnOk() {
        when(testQuestionService.countQuestionsByCourse(1L)).thenReturn(10L);

        ResponseEntity<Long> response = testQuestionController.countQuestionsByCourse(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(10L, response.getBody());
    }

    @Test
    void countQuestionsByProfessor_ShouldReturnOk() {
        when(testQuestionService.countQuestionsByProfessor(1)).thenReturn(15L);

        ResponseEntity<Long> response = testQuestionController.countQuestionsByProfessor(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(15L, response.getBody());
    }

    @Test
    void countQuestionsByPoints_ShouldReturnOk() {
        when(testQuestionService.countQuestionsByPoints(5.0F)).thenReturn(7L);

        ResponseEntity<Long> response = testQuestionController.countQuestionsByPoints(5.0F);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(7L, response.getBody());
    }

    @Test
    void countQuestionsByMinPoints_ShouldReturnOk() {
        when(testQuestionService.countQuestionsByMinPoints(2.5F)).thenReturn(8L);

        ResponseEntity<Long> response = testQuestionController.countQuestionsByMinPoints(2.5F);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(8L, response.getBody());
    }

    @Test
    void getAveragePointsPerQuestion_ShouldReturnOk() {
        when(testQuestionService.getAveragePointsPerQuestion(1L)).thenReturn(4.2F);

        ResponseEntity<Float> response = testQuestionController.getAveragePointsPerQuestion(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(4.2F, response.getBody());
    }

    @Test
    void getQuestionsByTestIdSortedByPoints_ShouldReturnOk() {
        List<TestQuestion> questions = Arrays.asList(new TestQuestion());
        when(testQuestionService.getQuestionsByTestIdSortedByPoints(1L)).thenReturn(questions);

        ResponseEntity<List<TestQuestion>> response = testQuestionController.getQuestionsByTestIdSortedByPoints(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(questions, response.getBody());
    }

    @Test
    void getQuestionsByTestAndMinPoints_ShouldReturnOk() {
        List<TestQuestion> questions = Arrays.asList(new TestQuestion());
        when(testQuestionService.getQuestionsByTestAndMinPoints(1L, 3.5F)).thenReturn(questions);

        ResponseEntity<List<TestQuestion>> response = testQuestionController.getQuestionsByTestAndMinPoints(1L, 3.5F);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(questions, response.getBody());
    }
}
