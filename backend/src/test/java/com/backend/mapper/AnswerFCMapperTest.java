package com.backend.mapper;

import com.backend.dto.AnswerFCDTO;
import com.backend.model.AnswerFC;
import com.backend.model.Flashcard;
import com.backend.repository.FlashcardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnswerFCMapperTest {

    @Mock
    private FlashcardRepository flashcardRepository;

    @Mock
    private FlashcardMapper flashcardMapper;

    @InjectMocks
    private AnswerFCMapper answerFCMapper;

    private AnswerFC answer;
    private AnswerFCDTO answerDTO;
    private Flashcard flashcard;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        flashcard = new Flashcard();
        flashcard.setId(1L);
        flashcard.setQuestion("What is polymorphism?");

        answer = new AnswerFC();
        answer.setId(1L);
        answer.setOptionText("The ability of objects to take different forms");
        answer.setCorrect(true);
        answer.setFlashcard(flashcard);

        answerDTO = new AnswerFCDTO();
        answerDTO.setId(1L);
        answerDTO.setOptionText("The ability of objects to take different forms");
        answerDTO.setCorrect(true);
        answerDTO.setFlashcardId(1L);
    }

    @Test
    void toDTO_WithValidAnswer_ShouldMapAllFields() {
        // Act
        AnswerFCDTO result = answerFCMapper.toDTO(answer);

        // Assert
        assertNotNull(result);
        assertEquals(answer.getId(), result.getId());
        assertEquals(answer.getOptionText(), result.getOptionText());
        assertEquals(answer.isCorrect(), result.isCorrect());
        assertEquals(flashcard.getId(), result.getFlashcardId());
    }

    @Test
    void toDTO_WithNullAnswer_ShouldReturnNull() {
        // Act
        AnswerFCDTO result = answerFCMapper.toDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toDTO_WithNullFlashcard_ShouldMapOtherFields() {
        // Arrange
        answer.setFlashcard(null);

        // Act
        AnswerFCDTO result = answerFCMapper.toDTO(answer);

        // Assert
        assertNotNull(result);
        assertEquals(answer.getId(), result.getId());
        assertEquals(answer.getOptionText(), result.getOptionText());
        assertEquals(answer.isCorrect(), result.isCorrect());
        assertNull(result.getFlashcardId());
    }

    @Test
    void toEntity_WithValidDTO_ShouldMapAllFields() {
        // Arrange
        when(flashcardRepository.findById(1L)).thenReturn(Optional.of(flashcard));

        // Act
        AnswerFC result = answerFCMapper.toEntity(answerDTO);

        // Assert
        assertNotNull(result);
        assertEquals(answerDTO.getId(), result.getId());
        assertEquals(answerDTO.getOptionText(), result.getOptionText());
        assertEquals(answerDTO.isCorrect(), result.isCorrect());
        assertEquals(flashcard, result.getFlashcard());

        // Verify repository interactions
        verify(flashcardRepository).findById(1L);
    }

    @Test
    void toEntity_WithNullDTO_ShouldReturnNull() {
        // Act
        AnswerFC result = answerFCMapper.toEntity(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_WithNullFlashcardId_ShouldMapOtherFields() {
        // Arrange
        answerDTO.setFlashcardId(null);

        // Act
        AnswerFC result = answerFCMapper.toEntity(answerDTO);

        // Assert
        assertNotNull(result);
        assertEquals(answerDTO.getId(), result.getId());
        assertEquals(answerDTO.getOptionText(), result.getOptionText());
        assertEquals(answerDTO.isCorrect(), result.isCorrect());
        assertNull(result.getFlashcard());
    }

    @Test
    void toEntity_WithNonexistentFlashcard_ShouldMapOtherFields() {
        // Arrange
        answerDTO.setFlashcardId(999L);
        when(flashcardRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        AnswerFC result = answerFCMapper.toEntity(answerDTO);

        // Assert
        assertNotNull(result);
        assertEquals(answerDTO.getId(), result.getId());
        assertEquals(answerDTO.getOptionText(), result.getOptionText());
        assertEquals(answerDTO.isCorrect(), result.isCorrect());
        assertNull(result.getFlashcard());

        // Verify repository interactions
        verify(flashcardRepository).findById(999L);
    }
}
