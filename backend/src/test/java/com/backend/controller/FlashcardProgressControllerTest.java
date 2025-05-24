package com.backend.controller;

import com.backend.model.Flashcard;
import com.backend.model.FlashcardProgress;
import com.backend.model.User;
import com.backend.service.FlashcardProgressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlashcardProgressControllerTest {

    @Mock
    private FlashcardProgressService flashcardProgressService;

    @InjectMocks
    private FlashcardProgressController flashcardProgressController;

    private FlashcardProgress createFlashcardProgress(Long id, Integer userId, Long flashcardId) {
        FlashcardProgress progress = new FlashcardProgress();
        progress.setId(id);
        User user = new User();
        user.setId(userId);
        progress.setUser(user);
        Flashcard flashcard = new Flashcard();
        flashcard.setId(flashcardId);
        progress.setFlashcard(flashcard);
        progress.setEaseFactor(2.5);
        progress.setInterval(10);
        progress.setRepetitions(1);
        progress.setDueDate(new Date());
        progress.setLastReviewed(new Date());
        return progress;
    }

    @Test
    void shouldReturnAllFlashcardProgress() {
        List<FlashcardProgress> progresses = Arrays.asList(
                createFlashcardProgress(1L, 1, 101L),
                createFlashcardProgress(2L, 2, 102L)
        );
        when(flashcardProgressService.getAllFlashcardProgress()).thenReturn(progresses);

        ResponseEntity<List<FlashcardProgress>> response = flashcardProgressController.getAllFlashcardProgress();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void shouldReturnFlashcardProgressByIdIfExists() {
        FlashcardProgress progress = createFlashcardProgress(1L, 1, 101L);
        when(flashcardProgressService.getFlashcardProgressById(1L)).thenReturn(Optional.of(progress));

        ResponseEntity<FlashcardProgress> response = flashcardProgressController.getFlashcardProgressById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void shouldReturnNotFoundIfFlashcardProgressByIdDoesNotExist() {
        when(flashcardProgressService.getFlashcardProgressById(1L)).thenReturn(Optional.empty());

        ResponseEntity<FlashcardProgress> response = flashcardProgressController.getFlashcardProgressById(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void shouldCreateFlashcardProgressSuccessfully() {
        FlashcardProgress toCreate = createFlashcardProgress(null, 1, 101L);
        FlashcardProgress created = createFlashcardProgress(1L, 1, 101L);
        when(flashcardProgressService.createFlashcardProgress(toCreate)).thenReturn(created);

        ResponseEntity<FlashcardProgress> response = flashcardProgressController.createFlashcardProgress(toCreate);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void shouldUpdateFlashcardProgressSuccessfully() {
        FlashcardProgress toUpdate = createFlashcardProgress(1L, 1, 101L);
        when(flashcardProgressService.updateFlashcardProgress(1L, toUpdate)).thenReturn(toUpdate);

        ResponseEntity<FlashcardProgress> response = flashcardProgressController.updateFlashcardProgress(1L, toUpdate);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void shouldDeleteFlashcardProgressSuccessfully() {
        doNothing().when(flashcardProgressService).deleteFlashcardProgress(1L);

        ResponseEntity<Void> response = flashcardProgressController.deleteFlashcardProgress(1L);

        assertEquals(200, response.getStatusCodeValue());
        verify(flashcardProgressService, times(1)).deleteFlashcardProgress(1L);
    }

    @Test
    void shouldReturnByUserId() {
        List<FlashcardProgress> progresses = Arrays.asList(createFlashcardProgress(1L, 5, 9L));
        when(flashcardProgressService.getByUserId(5)).thenReturn(progresses);

        ResponseEntity<List<FlashcardProgress>> response = flashcardProgressController.getByUserId(5);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnByFlashcardId() {
        List<FlashcardProgress> progresses = Arrays.asList(createFlashcardProgress(2L, 3, 10L));
        when(flashcardProgressService.getByFlashcardId(10L)).thenReturn(progresses);

        ResponseEntity<List<FlashcardProgress>> response = flashcardProgressController.getByFlashcardId(10L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnDueProgress() {
        Date date = new Date();
        List<FlashcardProgress> progresses = Arrays.asList(createFlashcardProgress(4L, 3, 15L));
        when(flashcardProgressService.getDueProgress(date, 3)).thenReturn(progresses);

        ResponseEntity<List<FlashcardProgress>> response = flashcardProgressController.getDueProgress(date, 3);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnByUserIdAndMaterialId() {
        List<FlashcardProgress> progresses = Arrays.asList(createFlashcardProgress(5L, 3, 20L));
        when(flashcardProgressService.getByUserIdAndMaterialId(3, 20L)).thenReturn(progresses);

        ResponseEntity<List<FlashcardProgress>> response = flashcardProgressController.getByUserIdAndMaterialId(3, 20L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnByUserIdAndLevel() {
        List<FlashcardProgress> progresses = Arrays.asList(createFlashcardProgress(6L, 3, 21L));
        when(flashcardProgressService.getByUserIdAndLevel(3, 2)).thenReturn(progresses);

        ResponseEntity<List<FlashcardProgress>> response = flashcardProgressController.getByUserIdAndLevel(3, 2);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnByUserIdAndPageIndex() {
        List<FlashcardProgress> progresses = Arrays.asList(createFlashcardProgress(7L, 3, 22L));
        when(flashcardProgressService.getByUserIdAndPageIndex(3, 12)).thenReturn(progresses);

        ResponseEntity<List<FlashcardProgress>> response = flashcardProgressController.getByUserIdAndPageIndex(3, 12);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnByUserIdAndCourseId() {
        List<FlashcardProgress> progresses = Arrays.asList(createFlashcardProgress(8L, 4, 25L));
        when(flashcardProgressService.getByUserIdAndCourseId(4, 50L)).thenReturn(progresses);

        ResponseEntity<List<FlashcardProgress>> response = flashcardProgressController.getByUserIdAndCourseId(4, 50L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnByUserIdAndQuestionType() {
        List<FlashcardProgress> progresses = Arrays.asList(createFlashcardProgress(9L, 3, 21L));
        when(flashcardProgressService.getByUserIdAndQuestionType(3, "typeA")).thenReturn(progresses);

        ResponseEntity<List<FlashcardProgress>> response = flashcardProgressController.getByUserIdAndQuestionType(3, "typeA");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnByFlashcardIdAndUserIdIfExists() {
        FlashcardProgress progress = createFlashcardProgress(10L, 5, 30L);
        when(flashcardProgressService.getByFlashcardIdAndUserId(30L, 5)).thenReturn(progress);

        ResponseEntity<FlashcardProgress> response = flashcardProgressController.getByFlashcardIdAndUserId(30L, 5);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(10L, response.getBody().getId());
    }

    @Test
    void shouldReturnNotFoundByFlashcardIdAndUserIdIfNotExists() {
        when(flashcardProgressService.getByFlashcardIdAndUserId(40L, 10)).thenReturn(null);

        ResponseEntity<FlashcardProgress> response = flashcardProgressController.getByFlashcardIdAndUserId(40L, 10);

        assertEquals(404, response.getStatusCodeValue());
    }
}
