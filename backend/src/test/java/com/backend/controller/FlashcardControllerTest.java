package com.backend.controller;

import com.backend.model.Flashcard;
import com.backend.model.User;
import com.backend.service.FlashcardService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class FlashcardControllerTest {

    @Mock
    private FlashcardService flashcardService;

    @InjectMocks
    private FlashcardController flashcardController;

    public FlashcardControllerTest() {
        openMocks(this);
    }

    private Flashcard createFlashcard(Long id, Integer userId) {
        Flashcard flashcard = new Flashcard();
        flashcard.setId(id);
        User user = new User();
        user.setId(userId);
        flashcard.setUser(user);
        flashcard.setPageIndex(5); // Set a default page index for testing
        return flashcard;
    }

    @Test
    void shouldReturnAllFlashcards() {
        List<Flashcard> flashcards = Arrays.asList(new Flashcard(), new Flashcard());
        when(flashcardService.getAllFlashcards()).thenReturn(flashcards);

        ResponseEntity<List<Flashcard>> response = flashcardController.getAllFlashcards();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void shouldReturnFlashcardByIdIfExists() {
        Flashcard flashcard = createFlashcard(1L, 1);
        when(flashcardService.getFlashcardById(1L)).thenReturn(Optional.of(flashcard));

        ResponseEntity<Flashcard> response = flashcardController.getFlashcardById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void shouldReturnNotFoundIfFlashcardByIdDoesNotExist() {
        when(flashcardService.getFlashcardById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Flashcard> response = flashcardController.getFlashcardById(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void shouldCreateFlashcardSuccessfully() {
        Flashcard flashcard = createFlashcard(null, 1);
        Flashcard created = createFlashcard(1L, 1);
        when(flashcardService.createFlashcard(flashcard)).thenReturn(created);

        ResponseEntity<Flashcard> response = flashcardController.createFlashcard(flashcard);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void shouldUpdateFlashcardSuccessfully() {
        Flashcard flashcard = createFlashcard(1L, 1);
        when(flashcardService.updateFlashcard(1L, flashcard)).thenReturn(flashcard);

        ResponseEntity<Flashcard> response = flashcardController.updateFlashcard(1L, flashcard);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void shouldDeleteFlashcardSuccessfully() {
        doNothing().when(flashcardService).deleteFlashcard(1L);

        ResponseEntity<Void> response = flashcardController.deleteFlashcard(1L);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldReturnFlashcardsByUserId() {
        List<Flashcard> flashcards = Arrays.asList(new Flashcard(), new Flashcard());
        when(flashcardService.getByUserId(1)).thenReturn(flashcards);

        ResponseEntity<List<Flashcard>> response = flashcardController.getByUserId(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void shouldReturnFlashcardsByMaterialId() {
        List<Flashcard> flashcards = Arrays.asList(new Flashcard());
        when(flashcardService.getByMaterialId(5L)).thenReturn(flashcards);

        ResponseEntity<List<Flashcard>> response = flashcardController.getByMaterialId(5L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnDueFlashcards() {
        Date date = new Date();
        List<Flashcard> flashcards = Arrays.asList(new Flashcard());
        // Align with updated service method signature (date, userId)
        when(flashcardService.getDueFlashcards(date, 1)).thenReturn(flashcards);

        // Use the actual getDueUntilDate method that now exists in the controller
        ResponseEntity<List<Flashcard>> response = flashcardController.getDueUntilDate(1, date, 10);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnFlashcardsByPageIndex() {
        List<Flashcard> flashcards = Arrays.asList(createFlashcard(1L, 1), createFlashcard(2L, 2));
        when(flashcardService.getByPageIndex(5)).thenReturn(flashcards);

        ResponseEntity<List<Flashcard>> response = flashcardController.getByPageIndex(5);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void shouldReturnFlashcardsByPageIndexAndUserId() {
        List<Flashcard> flashcards = Arrays.asList(createFlashcard(1L, 1));
        when(flashcardService.getByPageIndexAndUserId(5, 1)).thenReturn(flashcards);

        ResponseEntity<List<Flashcard>> response = flashcardController.getByPageIndexAndUserId(5, 1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnFlashcardsByPageIndexAndMaterialId() {
        List<Flashcard> flashcards = Arrays.asList(createFlashcard(1L, 1));
        when(flashcardService.getByPageIndexAndMaterialId(5, 1L)).thenReturn(flashcards);

        ResponseEntity<List<Flashcard>> response = flashcardController.getByPageIndexAndMaterialId(5, 1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }
}
