package com.backend.service;

import com.backend.model.Flashcard;
import com.backend.model.FlashcardProgress;
import com.backend.model.User;
import com.backend.repository.FlashcardProgressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlashcardProgressServiceTest {

    @Mock
    private FlashcardProgressRepository flashcardProgressRepository;

    @InjectMocks
    private FlashcardProgressService flashcardProgressService;

    private FlashcardProgress flashcardProgress;

    @BeforeEach
    void setUp() {
        User user = new User();
        Flashcard flashcard = new Flashcard();
        user.setId(1);
        flashcard.setId(100L);

        flashcardProgress = new FlashcardProgress();
        flashcardProgress.setId(1L);
        flashcardProgress.setUser(user);
        flashcardProgress.setFlashcard(flashcard);
        flashcardProgress.setEaseFactor(2.5);
        flashcardProgress.setInterval(10);
        flashcardProgress.setRepetitions(2);
        flashcardProgress.setDueDate(new Date());
        flashcardProgress.setLastReviewed(new Date());
    }

    @Test
    void testGetAllFlashcardProgress() {
        when(flashcardProgressRepository.findAll()).thenReturn(List.of(flashcardProgress));
        List<FlashcardProgress> result = flashcardProgressService.getAllFlashcardProgress();
        assertEquals(1, result.size());
        verify(flashcardProgressRepository).findAll();
    }

    @Test
    void testGetFlashcardProgressById() {
        when(flashcardProgressRepository.findById(1L)).thenReturn(Optional.of(flashcardProgress));
        Optional<FlashcardProgress> result = flashcardProgressService.getFlashcardProgressById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(flashcardProgressRepository).findById(1L);
    }

    @Test
    void testCreateFlashcardProgress() {
        when(flashcardProgressRepository.save(flashcardProgress)).thenReturn(flashcardProgress);
        FlashcardProgress result = flashcardProgressService.createFlashcardProgress(flashcardProgress);
        assertEquals(flashcardProgress, result);
        verify(flashcardProgressRepository).save(flashcardProgress);
    }

    @Test
    void testUpdateFlashcardProgressSuccess() {
        FlashcardProgress updated = new FlashcardProgress();
        updated.setUser(new User());
        updated.setFlashcard(new Flashcard());
        updated.setEaseFactor(3.0);
        updated.setInterval(12);
        updated.setRepetitions(3);
        Date now = new Date();
        updated.setDueDate(now);
        updated.setLastReviewed(now);

        when(flashcardProgressRepository.findById(1L)).thenReturn(Optional.of(flashcardProgress));
        when(flashcardProgressRepository.save(any(FlashcardProgress.class))).thenReturn(flashcardProgress);

        FlashcardProgress result = flashcardProgressService.updateFlashcardProgress(1L, updated);

        assertNotNull(result);
        assertEquals(3.0, flashcardProgress.getEaseFactor());
        verify(flashcardProgressRepository).save(any(FlashcardProgress.class));
    }

    @Test
    void testUpdateFlashcardProgressNotFound() {
        when(flashcardProgressRepository.findById(99L)).thenReturn(Optional.empty());
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                flashcardProgressService.updateFlashcardProgress(99L, flashcardProgress));
        assertTrue(thrown.getMessage().contains("FlashcardProgress not found"));
        verify(flashcardProgressRepository, never()).save(any());
    }

    @Test
    void testDeleteFlashcardProgress() {
        flashcardProgressService.deleteFlashcardProgress(1L);
        verify(flashcardProgressRepository).deleteById(1L);
    }

    @Test
    void testGetByUserId() {
        when(flashcardProgressRepository.findByUserId(1)).thenReturn(List.of(flashcardProgress));
        List<FlashcardProgress> result = flashcardProgressService.getByUserId(1);
        assertEquals(1, result.size());
        verify(flashcardProgressRepository).findByUserId(1);
    }

    @Test
    void testGetByFlashcardId() {
        when(flashcardProgressRepository.findByFlashcardId(100L)).thenReturn(List.of(flashcardProgress));
        List<FlashcardProgress> result = flashcardProgressService.getByFlashcardId(100L);
        assertEquals(1, result.size());
        verify(flashcardProgressRepository).findByFlashcardId(100L);
    }

    @Test
    void testGetDueProgress() {
        Date date = new Date();
        when(flashcardProgressRepository.findDueProgress(date, 1)).thenReturn(List.of(flashcardProgress));
        List<FlashcardProgress> result = flashcardProgressService.getDueProgress(date, 1);
        assertEquals(1, result.size());
        verify(flashcardProgressRepository).findDueProgress(date, 1);
    }

    @Test
    void testGetByUserIdAndMaterialId() {
        when(flashcardProgressRepository.findByUserIdAndMaterialId(1, 200L)).thenReturn(List.of(flashcardProgress));
        List<FlashcardProgress> result = flashcardProgressService.getByUserIdAndMaterialId(1, 200L);
        assertEquals(1, result.size());
        verify(flashcardProgressRepository).findByUserIdAndMaterialId(1, 200L);
    }

    @Test
    void testGetByUserIdAndLevel() {
        when(flashcardProgressRepository.findByUserIdAndLevel(1, 2)).thenReturn(List.of(flashcardProgress));
        List<FlashcardProgress> result = flashcardProgressService.getByUserIdAndLevel(1, 2);
        assertEquals(1, result.size());
        verify(flashcardProgressRepository).findByUserIdAndLevel(1, 2);
    }

    @Test
    void testGetByUserIdAndPageIndex() {
        when(flashcardProgressRepository.findByUserIdAndPageIndex(1, 3)).thenReturn(List.of(flashcardProgress));
        List<FlashcardProgress> result = flashcardProgressService.getByUserIdAndPageIndex(1, 3);
        assertEquals(1, result.size());
        verify(flashcardProgressRepository).findByUserIdAndPageIndex(1, 3);
    }

    @Test
    void testGetByUserIdAndCourseId() {
        when(flashcardProgressRepository.findByUserIdAndCourseId(1, 4L)).thenReturn(List.of(flashcardProgress));
        List<FlashcardProgress> result = flashcardProgressService.getByUserIdAndCourseId(1, 4L);
        assertEquals(1, result.size());
        verify(flashcardProgressRepository).findByUserIdAndCourseId(1, 4L);
    }

    @Test
    void testGetByUserIdAndQuestionType() {
        when(flashcardProgressRepository.findByUserIdAndQuestionType(1, "MCQ")).thenReturn(List.of(flashcardProgress));
        List<FlashcardProgress> result = flashcardProgressService.getByUserIdAndQuestionType(1, "MCQ");
        assertEquals(1, result.size());
        verify(flashcardProgressRepository).findByUserIdAndQuestionType(1, "MCQ");
    }

    @Test
    void testGetByFlashcardIdAndUserId() {
        when(flashcardProgressRepository.findByFlashcardIdAndUserId(100L, 1)).thenReturn(flashcardProgress);
        FlashcardProgress result = flashcardProgressService.getByFlashcardIdAndUserId(100L, 1);
        assertEquals(flashcardProgress, result);
        verify(flashcardProgressRepository).findByFlashcardIdAndUserId(100L, 1);
    }
}
