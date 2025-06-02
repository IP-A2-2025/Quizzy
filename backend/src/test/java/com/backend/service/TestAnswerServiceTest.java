package com.backend.service;

import com.backend.model.TestAnswer;
import com.backend.model.TestQuestion;
import com.backend.repository.TestAnswerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TestAnswerServiceTest {

    @Mock
    private TestAnswerRepository testAnswerRepository;

    @InjectMocks
    private TestAnswerService testAnswerService;

    private TestAnswer testAnswer;
    private TestQuestion testQuestion;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testQuestion = new TestQuestion();
        testQuestion.setId(1L);
        testQuestion.setQuestionText("Test Question");
        testQuestion.setPointValue(10.0f);

        testAnswer = new TestAnswer();
        testAnswer.setId(1L);
        testAnswer.setOptionText("Test Answer");
        testAnswer.setCorrect(true);
        testAnswer.setTestQuestion(testQuestion);
    }

    @Test
    void shouldGetAllAnswers() {
        when(testAnswerRepository.findAll()).thenReturn(List.of(testAnswer));

        var result = testAnswerService.getAllAnswers();

        assertEquals(1, result.size());
        verify(testAnswerRepository).findAll();
    }

    @Test
    void shouldGetAnswerById() {
        when(testAnswerRepository.findById(1L)).thenReturn(Optional.of(testAnswer));

        var result = testAnswerService.getAnswerById(1L);

        assertEquals(testAnswer, result);
        verify(testAnswerRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenAnswerNotFound() {
        when(testAnswerRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> testAnswerService.getAnswerById(999L));
    }

    @Test
    void shouldThrowExceptionWhenInvalidAnswerId() {
        assertThrows(EntityNotFoundException.class, () -> testAnswerService.getAnswerById(0L));
        assertThrows(EntityNotFoundException.class, () -> testAnswerService.getAnswerById(null));
    }

    @Test
    void shouldGetAnswersByQuestionId() {
        when(testAnswerRepository.findByQuestionId(1L)).thenReturn(List.of(testAnswer));

        var result = testAnswerService.getAnswersByQuestionId(1L);

        assertEquals(1, result.size());
        verify(testAnswerRepository).findByQuestionId(1L);
    }

    @Test
    void shouldThrowExceptionWhenInvalidQuestionIdForGetAnswers() {
        assertThrows(IllegalArgumentException.class, () -> testAnswerService.getAnswersByQuestionId(0L));
        assertThrows(IllegalArgumentException.class, () -> testAnswerService.getAnswersByQuestionId(null));
    }

    @Test
    void shouldGetCorrectAnswersByQuestionId() {
        when(testAnswerRepository.findCorrectAnswersByQuestionId(1L)).thenReturn(List.of(testAnswer));

        var result = testAnswerService.getCorrectAnswersByQuestionId(1L);

        assertEquals(1, result.size());
        verify(testAnswerRepository).findCorrectAnswersByQuestionId(1L);
    }

    @Test
    void shouldGetAnswersByTestId() {
        when(testAnswerRepository.findByTestId(1L)).thenReturn(List.of(testAnswer));

        var result = testAnswerService.getAnswersByTestId(1L);

        assertEquals(1, result.size());
        verify(testAnswerRepository).findByTestId(1L);
    }

    @Test
    void shouldCountCorrectAnswersByQuestionId() {
        when(testAnswerRepository.countCorrectAnswersByQuestionId(1L)).thenReturn(2L);

        var result = testAnswerService.countCorrectAnswersByQuestionId(1L);

        assertEquals(2L, result);
        verify(testAnswerRepository).countCorrectAnswersByQuestionId(1L);
    }

    @Test
    void shouldCountAnswersByTestId() {
        when(testAnswerRepository.countAnswersByTestId(1L)).thenReturn(5L);

        var result = testAnswerService.countAnswersByTestId(1L);

        assertEquals(5L, result);
        verify(testAnswerRepository).countAnswersByTestId(1L);
    }

    @Test
    void shouldCountCorrectAnswersByTestId() {
        when(testAnswerRepository.countCorrectAnswersByTestId(1L)).thenReturn(3L);

        var result = testAnswerService.countCorrectAnswersByTestId(1L);

        assertEquals(3L, result);
        verify(testAnswerRepository).countCorrectAnswersByTestId(1L);
    }

    @Test
    void shouldGetIncorrectAnswersByQuestionId() {
        TestAnswer incorrectAnswer = new TestAnswer();
        incorrectAnswer.setId(2L);
        incorrectAnswer.setCorrect(false);
        
        when(testAnswerRepository.findIncorrectAnswersByQuestionId(1L)).thenReturn(List.of(incorrectAnswer));

        var result = testAnswerService.getIncorrectAnswersByQuestionId(1L);

        assertEquals(1, result.size());
        verify(testAnswerRepository).findIncorrectAnswersByQuestionId(1L);
    }

    @Test
    void shouldFindByOptionTextContaining() {
        when(testAnswerRepository.findByOptionTextContaining("Test")).thenReturn(List.of(testAnswer));

        var result = testAnswerService.findByOptionTextContaining("Test");

        assertEquals(1, result.size());
        verify(testAnswerRepository).findByOptionTextContaining("Test");
    }

    @Test
    void shouldThrowExceptionWhenNullKeyword() {
        assertThrows(IllegalArgumentException.class, () -> testAnswerService.findByOptionTextContaining(null));
    }

    @Test
    void shouldCreateAnswer() {
        TestAnswer newAnswer = new TestAnswer();
        newAnswer.setOptionText("New Answer");
        newAnswer.setCorrect(true);

        when(testAnswerRepository.save(any(TestAnswer.class))).thenReturn(newAnswer);

        var result = testAnswerService.createAnswer(newAnswer);

        assertEquals(newAnswer, result);
        verify(testAnswerRepository).save(newAnswer);
    }

    @Test
    void shouldThrowExceptionWhenCreateAnswerWithId() {
        TestAnswer invalidAnswer = new TestAnswer();
        invalidAnswer.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> testAnswerService.createAnswer(invalidAnswer));
    }

    @Test
    void shouldUpdateAnswer() {
        TestAnswer updateData = new TestAnswer();
        updateData.setOptionText("Updated Answer");
        updateData.setCorrect(false);

        when(testAnswerRepository.findById(1L)).thenReturn(Optional.of(testAnswer));
        when(testAnswerRepository.save(any(TestAnswer.class))).thenReturn(testAnswer);

        var result = testAnswerService.updateAnswer(1L, updateData);

        assertEquals("Updated Answer", result.getOptionText());
        assertFalse(result.isCorrect());
        verify(testAnswerRepository).findById(1L);
        verify(testAnswerRepository).save(testAnswer);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentAnswer() {
        when(testAnswerRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> testAnswerService.updateAnswer(999L, new TestAnswer()));
    }

    @Test
    void shouldDeleteAnswerById() {
        when(testAnswerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(testAnswerRepository).deleteById(1L);

        testAnswerService.deleteAnswerById(1L);

        verify(testAnswerRepository).existsById(1L);
        verify(testAnswerRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentAnswer() {
        when(testAnswerRepository.existsById(999L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> testAnswerService.deleteAnswerById(999L));
    }
} 