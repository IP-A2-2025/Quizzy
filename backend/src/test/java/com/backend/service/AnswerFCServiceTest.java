package com.backend.service;

import com.backend.model.AnswerFC;
import com.backend.model.Flashcard;
import com.backend.repository.AnswerFCRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnswerFCServiceTest {

    @Mock
    private AnswerFCRepository answerFCRepository;

    @InjectMocks
    private AnswerFCService answerFCService;

    private AnswerFC answer;
    private Flashcard flashcard;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        flashcard = new Flashcard();
        flashcard.setId(1L);

        answer = new AnswerFC();
        answer.setId(1L);
        answer.setOptionText("Option 1");
        answer.setCorrect(true);
        answer.setFlashcard(flashcard);
    }

    @Test
    void testGetAllAnswers() {
        when(answerFCRepository.findAll()).thenReturn(Arrays.asList(answer));

        List<AnswerFC> answers = answerFCService.getAllAnswers();

        assertEquals(1, answers.size());
        assertEquals(answer.getId(), answers.get(0).getId());
    }

    @Test
    void testGetAnswerById_Found() {
        when(answerFCRepository.findById(1L)).thenReturn(Optional.of(answer));

        AnswerFC foundAnswer = answerFCService.getAnswerById(1L);

        assertNotNull(foundAnswer);
        assertEquals(1L, foundAnswer.getId());
    }

    @Test
    void testGetAnswerById_NotFound() {
        when(answerFCRepository.findById(2L)).thenReturn(Optional.empty());

        AnswerFC foundAnswer = answerFCService.getAnswerById(2L);

        assertNull(foundAnswer);
    }

    @Test
    void testGetAnswersByFlashcardId() {
        when(answerFCRepository.findByFlashcardId(1L)).thenReturn(Arrays.asList(answer));

        List<AnswerFC> answers = answerFCService.getAnswersByFlashcardId(1L);

        assertEquals(1, answers.size());
        assertEquals(1L, answers.get(0).getFlashcard().getId());
    }

    @Test
    void testGetCorrectAnswersByFlashcardId() {
        when(answerFCRepository.findCorrectAnswersByFlashcardId(1L)).thenReturn(Arrays.asList(answer));

        List<AnswerFC> correctAnswers = answerFCService.getCorrectAnswersByFlashcardId(1L);

        assertEquals(1, correctAnswers.size());
        assertTrue(correctAnswers.get(0).isCorrect());
    }

    @Test
    void testCreateAnswer() {
        when(answerFCRepository.save(any(AnswerFC.class))).thenReturn(answer);

        AnswerFC created = answerFCService.createAnswer(answer);

        assertNotNull(created);
        assertEquals("Option 1", created.getOptionText());
    }

    @Test
    void testUpdateAnswer_Found() {
        AnswerFC updated = new AnswerFC();
        updated.setOptionText("Updated Option");
        updated.setCorrect(false);
        updated.setFlashcard(flashcard);

        when(answerFCRepository.findById(1L)).thenReturn(Optional.of(answer));
        when(answerFCRepository.save(any(AnswerFC.class))).thenReturn(answer);

        AnswerFC result = answerFCService.updateAnswer(1L, updated);

        assertNotNull(result);
        assertEquals("Updated Option", result.getOptionText());
        assertFalse(result.isCorrect());
    }

    @Test
    void testUpdateAnswer_NotFound() {
        when(answerFCRepository.findById(2L)).thenReturn(Optional.empty());

        AnswerFC updated = new AnswerFC();
        updated.setOptionText("Updated Option");
        updated.setCorrect(false);
        updated.setFlashcard(flashcard);

        AnswerFC result = answerFCService.updateAnswer(2L, updated);

        assertNull(result);
    }

    @Test
    void testDeleteAnswer() {
        doNothing().when(answerFCRepository).deleteById(1L);

        answerFCService.deleteAnswer(1L);

        verify(answerFCRepository, times(1)).deleteById(1L);
    }
}
