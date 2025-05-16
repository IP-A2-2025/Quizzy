package com.backend.service;

import com.backend.model.Flashcard;
import com.backend.repository.FlashcardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlashcardServiceTest {

    private FlashcardRepository flashcardRepository;
    private FlashcardService flashcardService;

    @BeforeEach
    void setUp() {
        flashcardRepository = mock(FlashcardRepository.class);
        flashcardService = new FlashcardService(flashcardRepository);
    }

    @Test
    void testGetAllFlashcards() {
        Flashcard flashcard = new Flashcard();
        when(flashcardRepository.findAll()).thenReturn(List.of(flashcard));

        List<Flashcard> result = flashcardService.getAllFlashcards();
        assertEquals(1, result.size());
        verify(flashcardRepository, times(1)).findAll();
    }

    @Test
    void testGetFlashcardById() {
        Flashcard flashcard = new Flashcard();
        flashcard.setId(1L);

        when(flashcardRepository.findById(1L)).thenReturn(Optional.of(flashcard));

        Optional<Flashcard> result = flashcardService.getFlashcardById(1L);
        assertNotNull(result);
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testCreateFlashcard() {
        Flashcard flashcard = new Flashcard();
        when(flashcardRepository.save(flashcard)).thenReturn(flashcard);

        Flashcard result = flashcardService.createFlashcard(flashcard);
        assertEquals(flashcard, result);
    }

    @Test
    void testUpdateFlashcard() {
        Flashcard flashcard = new Flashcard();
        flashcard.setId(1L);
        flashcard.setQuestion("Q1");

        when(flashcardRepository.findById(1L)).thenReturn(Optional.of(flashcard));
        when(flashcardRepository.save(any(Flashcard.class))).thenReturn(flashcard);

        Flashcard result = flashcardService.updateFlashcard(1L, flashcard);

        assertEquals("Q1", result.getQuestion());
        verify(flashcardRepository).save(any(Flashcard.class));
    }


    @Test
    void testDeleteFlashcard() {
        flashcardService.deleteFlashcard(1L);
        verify(flashcardRepository, times(1)).deleteById(1L);
    }
    
    @Test
    void testGetByPageIndex() {
        Flashcard flashcard = new Flashcard();
        flashcard.setPageIndex(5);
        
        when(flashcardRepository.findByPageIndex(5)).thenReturn(List.of(flashcard));
        
        List<Flashcard> result = flashcardService.getByPageIndex(5);
        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getPageIndex());
        verify(flashcardRepository, times(1)).findByPageIndex(5);
    }
    
    @Test
    void testGetByPageIndexAndUserId() {
        Flashcard flashcard = new Flashcard();
        flashcard.setPageIndex(5);
        
        when(flashcardRepository.findByPageIndexAndUserId(5, 1)).thenReturn(List.of(flashcard));
        
        List<Flashcard> result = flashcardService.getByPageIndexAndUserId(5, 1);
        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getPageIndex());
        verify(flashcardRepository, times(1)).findByPageIndexAndUserId(5, 1);
    }
    
    @Test
    void testGetByPageIndexAndMaterialId() {
        Flashcard flashcard = new Flashcard();
        flashcard.setPageIndex(5);
        
        when(flashcardRepository.findByPageIndexAndMaterialId(5, 1L)).thenReturn(List.of(flashcard));
        
        List<Flashcard> result = flashcardService.getByPageIndexAndMaterialId(5, 1L);
        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getPageIndex());
        verify(flashcardRepository, times(1)).findByPageIndexAndMaterialId(5, 1L);
    }
}
