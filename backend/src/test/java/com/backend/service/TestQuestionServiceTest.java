package com.backend.service;

import com.backend.model.TestEntity;
import com.backend.model.TestQuestion;
import com.backend.repository.TestQuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TestQuestionServiceTest {

    @Mock
    private TestQuestionRepository testQuestionRepository;

    @InjectMocks
    private TestQuestionService testQuestionService;

    private TestQuestion testQuestion;
    private TestEntity testEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testEntity = new TestEntity();
        testEntity.setId(1L);

        testQuestion = new TestQuestion();
        testQuestion.setId(1L);
        testQuestion.setQuestionText("Test Question");
        testQuestion.setPointValue(10.0f);
        testQuestion.setTest(testEntity);
        testQuestion.setAnswers(new ArrayList<>());
    }

    @Test
    void shouldSaveQuestion() {
        when(testQuestionRepository.save(testQuestion)).thenReturn(testQuestion);

        TestQuestion result = testQuestionService.saveQuestion(testQuestion);

        assertEquals(testQuestion, result);
        verify(testQuestionRepository).save(testQuestion);
    }

    @Test
    void shouldThrowExceptionWhenSavingNullQuestion() {
        assertThrows(IllegalArgumentException.class, () -> testQuestionService.saveQuestion(null));
    }

    @Test
    void shouldCreateQuestion() {
        TestQuestion newQuestion = new TestQuestion();
        newQuestion.setQuestionText("New Question");
        newQuestion.setPointValue(5.0f);

        when(testQuestionRepository.save(any(TestQuestion.class))).thenReturn(newQuestion);

        TestQuestion result = testQuestionService.createQuestion(newQuestion);

        assertEquals(newQuestion, result);
        verify(testQuestionRepository).save(newQuestion);
    }

    @Test
    void shouldThrowExceptionWhenCreatingQuestionWithId() {
        TestQuestion invalidQuestion = new TestQuestion();
        invalidQuestion.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> testQuestionService.createQuestion(invalidQuestion));
    }

    @Test
    void shouldGetAllQuestions() {
        when(testQuestionRepository.findAll()).thenReturn(List.of(testQuestion));

        var result = testQuestionService.getAllQuestions();

        assertEquals(1, result.size());
        verify(testQuestionRepository).findAll();
    }

    @Test
    void shouldGetQuestionById() {
        when(testQuestionRepository.findById(1L)).thenReturn(Optional.of(testQuestion));

        var result = testQuestionService.getQuestionById(1L);

        assertEquals(testQuestion, result);
        verify(testQuestionRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenQuestionNotFound() {
        when(testQuestionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> testQuestionService.getQuestionById(999L));
    }

    @Test
    void shouldGetQuestionsByTestId() {
        when(testQuestionRepository.findByTestId(1L)).thenReturn(List.of(testQuestion));

        var result = testQuestionService.getQuestionsByTestId(1L);

        assertEquals(1, result.size());
        verify(testQuestionRepository).findByTestId(1L);
    }

    @Test
    void shouldThrowExceptionWhenInvalidTestId() {
        assertThrows(IllegalArgumentException.class, () -> testQuestionService.getQuestionsByTestId(0L));
        assertThrows(IllegalArgumentException.class, () -> testQuestionService.getQuestionsByTestId(null));
    }

    @Test
    void shouldCountQuestionsByTest() {
        when(testQuestionRepository.countQuestionsByTestId(1L)).thenReturn(5L);

        var result = testQuestionService.countQuestionsByTest(1L);

        assertEquals(5L, result);
        verify(testQuestionRepository).countQuestionsByTestId(1L);
    }

    @Test
    void shouldGetTotalPointsByTest() {
        when(testQuestionRepository.getTotalPointsByTestId(1L)).thenReturn(50.0f);

        var result = testQuestionService.getTotalPointsByTest(1L);

        assertEquals(50.0f, result);
        verify(testQuestionRepository).getTotalPointsByTestId(1L);
    }

    @Test
    void shouldGetQuestionsByCourse() {
        when(testQuestionRepository.findByCourseId(1L)).thenReturn(List.of(testQuestion));

        var result = testQuestionService.getQuestionsByCourse(1L);

        assertEquals(1, result.size());
        verify(testQuestionRepository).findByCourseId(1L);
    }

    @Test
    void shouldGetQuestionsByProfessor() {
        when(testQuestionRepository.findByProfessorId(1)).thenReturn(List.of(testQuestion));

        var result = testQuestionService.getQuestionsByProfessor(1);

        assertEquals(1, result.size());
        verify(testQuestionRepository).findByProfessorId(1);
    }

    @Test
    void shouldSearchQuestionsByText() {
        when(testQuestionRepository.findByQuestionTextContaining("Test")).thenReturn(List.of(testQuestion));

        var result = testQuestionService.searchQuestionsByText("Test");

        assertEquals(1, result.size());
        verify(testQuestionRepository).findByQuestionTextContaining("Test");
    }

    @Test
    void shouldGetQuestionsByPoints() {
        when(testQuestionRepository.findByPointValue(10.0f)).thenReturn(List.of(testQuestion));

        var result = testQuestionService.getQuestionsByPoints(10.0f);

        assertEquals(1, result.size());
        verify(testQuestionRepository).findByPointValue(10.0f);
    }

    @Test
    void shouldGetQuestionsByMinPoints() {
        when(testQuestionRepository.findByMinimumPoints(5.0f)).thenReturn(List.of(testQuestion));

        var result = testQuestionService.getQuestionsByMinPoints(5.0f);

        assertEquals(1, result.size());
        verify(testQuestionRepository).findByMinimumPoints(5.0f);
    }

    @Test
    void shouldCountQuestionsByCourse() {
        when(testQuestionRepository.countQuestionsByCourseId(1L)).thenReturn(10L);

        var result = testQuestionService.countQuestionsByCourse(1L);

        assertEquals(10L, result);
        verify(testQuestionRepository).countQuestionsByCourseId(1L);
    }

    @Test
    void shouldCountQuestionsByProfessor() {
        when(testQuestionRepository.countQuestionsByProfessorId(1)).thenReturn(15L);

        var result = testQuestionService.countQuestionsByProfessor(1);

        assertEquals(15L, result);
        verify(testQuestionRepository).countQuestionsByProfessorId(1);
    }

    @Test
    void shouldCountQuestionsByPoints() {
        when(testQuestionRepository.countQuestionsByPointValue(10.0f)).thenReturn(3L);

        var result = testQuestionService.countQuestionsByPoints(10.0f);

        assertEquals(3L, result);
        verify(testQuestionRepository).countQuestionsByPointValue(10.0f);
    }

    @Test
    void shouldCountQuestionsByMinPoints() {
        when(testQuestionRepository.countQuestionsByMinimumPoints(5.0f)).thenReturn(20L);

        var result = testQuestionService.countQuestionsByMinPoints(5.0f);

        assertEquals(20L, result);
        verify(testQuestionRepository).countQuestionsByMinimumPoints(5.0f);
    }

    @Test
    void shouldGetAveragePointsPerQuestion() {
        when(testQuestionRepository.getAveragePointsPerQuestion(1L)).thenReturn(7.5f);

        var result = testQuestionService.getAveragePointsPerQuestion(1L);

        assertEquals(7.5f, result);
        verify(testQuestionRepository).getAveragePointsPerQuestion(1L);
    }

    @Test
    void shouldGetQuestionsByTestIdSortedByPoints() {
        when(testQuestionRepository.findByTestIdOrderByPointsDesc(1L)).thenReturn(List.of(testQuestion));

        var result = testQuestionService.getQuestionsByTestIdSortedByPoints(1L);

        assertEquals(1, result.size());
        verify(testQuestionRepository).findByTestIdOrderByPointsDesc(1L);
    }

    @Test
    void shouldGetQuestionsByTestAndMinPoints() {
        when(testQuestionRepository.findByTestIdAndMinPoints(1L, 5.0f)).thenReturn(List.of(testQuestion));

        var result = testQuestionService.getQuestionsByTestAndMinPoints(1L, 5.0f);

        assertEquals(1, result.size());
        verify(testQuestionRepository).findByTestIdAndMinPoints(1L, 5.0f);
    }

    @Test
    void shouldDeleteQuestionById() {
        when(testQuestionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(testQuestionRepository).deleteById(1L);

        testQuestionService.deleteQuestionById(1L);

        verify(testQuestionRepository).existsById(1L);
        verify(testQuestionRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentQuestion() {
        when(testQuestionRepository.existsById(999L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> testQuestionService.deleteQuestionById(999L));
    }

    @Test
    void shouldUpdateQuestion() {
        TestQuestion updateData = new TestQuestion();
        updateData.setQuestionText("Updated Question");
        updateData.setPointValue(15.0f);

        when(testQuestionRepository.findById(1L)).thenReturn(Optional.of(testQuestion));
        when(testQuestionRepository.save(any(TestQuestion.class))).thenReturn(testQuestion);

        var result = testQuestionService.updateQuestion(1L, updateData);

        assertEquals("Updated Question", result.getQuestionText());
        assertEquals(15.0f, result.getPointValue());
        verify(testQuestionRepository).findById(1L);
        verify(testQuestionRepository).save(testQuestion);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentQuestion() {
        when(testQuestionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> testQuestionService.updateQuestion(999L, new TestQuestion()));
    }
} 