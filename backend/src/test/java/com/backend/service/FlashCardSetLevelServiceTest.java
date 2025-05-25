package com.backend.service;

import com.backend.model.Flashcard;
import com.backend.repository.FlashcardRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FlashCardSetLevelServiceTest {

    @Mock
    private FlashcardRepository flashcardRepository;

    @InjectMocks
    private FlashCardSetLevelService flashCardSetLevelService;

    private Flashcard testFlashcard;
    private final InputStream originalSystemIn = System.in;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testFlashcard = new Flashcard();
        testFlashcard.setId(5L);
        testFlashcard.setQuestion("Test Question");
        testFlashcard.setLevel(0);
    }

    @AfterEach
    void tearDown() {
        // Restore original System.in
        System.setIn(originalSystemIn);
    }

    @Test
    void shouldSetFlashcardLevel() {
        // Prepare mock input for Scanner (simulate user entering "2")
        String simulatedUserInput = "2\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        // Mock repository to return our test flashcard for ID 5
        when(flashcardRepository.findById(5L)).thenReturn(Optional.of(testFlashcard));
        
        // Call the method under test
        flashCardSetLevelService.setFlashcardLevel();

        // Verify flashcard was updated with level 2
        verify(flashcardRepository).findById(5L);
        
        // The flashcard passed to save should have level 2 (from our simulated input)
        verify(flashcardRepository).save(argThat(flashcard -> 
            flashcard.getId().equals(5L) && 
            flashcard.getLevel() == 2
        ));
    }

    @Test
    void shouldHandleInvalidInputBeforeValidInput() {
        // Simulate user entering invalid input first, then valid input "3"
        String simulatedUserInput = "invalid\n0\n5\n3\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        // Mock repository
        when(flashcardRepository.findById(5L)).thenReturn(Optional.of(testFlashcard));
        
        // Call the method under test
        flashCardSetLevelService.setFlashcardLevel();

        // Verify flashcard was updated with level 3
        verify(flashcardRepository).findById(5L);
        verify(flashcardRepository).save(argThat(flashcard -> 
            flashcard.getId().equals(5L) && 
            flashcard.getLevel() == 3
        ));
    }

    @Test
    void shouldNotUpdateWhenFlashcardNotFound() {
        // Mock repository to return empty Optional
        when(flashcardRepository.findById(5L)).thenReturn(Optional.empty());
        
        // Call the method under test
        flashCardSetLevelService.setFlashcardLevel();

        // Verify findById was called but save was not
        verify(flashcardRepository).findById(5L);
        verify(flashcardRepository, never()).save(any(Flashcard.class));
    }
}