package com.backend.mapper;

import com.backend.dto.AnswerFCDTO;
import com.backend.dto.FlashcardDTO;
import com.backend.model.AnswerFC;
import com.backend.model.Flashcard;
import com.backend.model.Material;
import com.backend.model.User;
import com.backend.repository.MaterialRepository;
import com.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FlashcardMapperTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private AnswerFCMapper answerFCMapper;

    @InjectMocks
    private FlashcardMapper flashcardMapper;

    private Flashcard flashcard;
    private FlashcardDTO flashcardDTO;
    private User user;
    private Material material;
    private AnswerFC answer1;
    private AnswerFC answer2;
    private AnswerFCDTO answerDTO1;
    private AnswerFCDTO answerDTO2;
    private final Date lastStudiedAt = new Date();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Doe");

        material = new Material();
        material.setId(1L);
        material.setName("Study Material");

        answer1 = new AnswerFC();
        answer1.setId(1L);
        answer1.setOptionText("Option 1");
        answer1.setCorrect(true);

        answer2 = new AnswerFC();
        answer2.setId(2L);
        answer2.setOptionText("Option 2");
        answer2.setCorrect(false);

        answerDTO1 = new AnswerFCDTO();
        answerDTO1.setId(1L);
        answerDTO1.setOptionText("Option 1");
        answerDTO1.setCorrect(true);

        answerDTO2 = new AnswerFCDTO();
        answerDTO2.setId(2L);
        answerDTO2.setOptionText("Option 2");
        answerDTO2.setCorrect(false);

        Set<AnswerFC> answers = new HashSet<>();
        answers.add(answer1);
        answers.add(answer2);

        Set<AnswerFCDTO> answerDTOs = new HashSet<>();
        answerDTOs.add(answerDTO1);
        answerDTOs.add(answerDTO2);

        flashcard = new Flashcard();
        flashcard.setId(1L);
        flashcard.setQuestion("What is polymorphism?");
        flashcard.setLevel(3);
        flashcard.setLastStudiedAt(lastStudiedAt);
        flashcard.setQuestionType("Multiple Choice");
        flashcard.setUser(user);
        flashcard.setMaterial(material);
        flashcard.setAnswers(answers);

        flashcardDTO = new FlashcardDTO();
        flashcardDTO.setId(1L);
        flashcardDTO.setQuestion("What is polymorphism?");
        flashcardDTO.setLevel(3);
        flashcardDTO.setLastStudiedAt(lastStudiedAt);
        flashcardDTO.setQuestionType("Multiple Choice");
        flashcardDTO.setUserId(1);
        flashcardDTO.setMaterialId(1L);
        flashcardDTO.setAnswers(answerDTOs);
    }

    @Test
    void toDTO_WithValidFlashcard_ShouldMapAllFields() {
        // Arrange
        when(answerFCMapper.toDTO(answer1)).thenReturn(answerDTO1);
        when(answerFCMapper.toDTO(answer2)).thenReturn(answerDTO2);

        // Act
        FlashcardDTO result = flashcardMapper.toDTO(flashcard);

        // Assert
        assertNotNull(result);
        assertEquals(flashcard.getId(), result.getId());
        assertEquals(flashcard.getQuestion(), result.getQuestion());
        assertEquals(flashcard.getLevel(), result.getLevel());
        assertEquals(flashcard.getLastStudiedAt(), result.getLastStudiedAt());
        assertEquals(flashcard.getQuestionType(), result.getQuestionType());
        assertEquals(user.getId(), result.getUserId());
        assertEquals(material.getId(), result.getMaterialId());

        // Check answers were mapped
        assertNotNull(result.getAnswers());
        assertEquals(2, result.getAnswers().size());

        // Verify mapper interactions
        verify(answerFCMapper).toDTO(answer1);
        verify(answerFCMapper).toDTO(answer2);
    }

    @Test
    void toDTO_WithNullFlashcard_ShouldReturnNull() {
        // Act
        FlashcardDTO result = flashcardMapper.toDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toDTO_WithNullRelations_ShouldMapOtherFields() {
        // Arrange
        flashcard.setUser(null);
        flashcard.setMaterial(null);
        flashcard.setAnswers(null);

        // Act
        FlashcardDTO result = flashcardMapper.toDTO(flashcard);

        // Assert
        assertNotNull(result);
        assertEquals(flashcard.getId(), result.getId());
        assertEquals(flashcard.getQuestion(), result.getQuestion());
        assertEquals(flashcard.getLevel(), result.getLevel());
        assertEquals(flashcard.getLastStudiedAt(), result.getLastStudiedAt());
        assertEquals(flashcard.getQuestionType(), result.getQuestionType());
        assertNull(result.getUserId());
        assertNull(result.getMaterialId());
        assertNull(result.getAnswers());
    }

    @Test
    void toEntity_WithValidDTO_ShouldMapAllFields() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));

        // Act
        Flashcard result = flashcardMapper.toEntity(flashcardDTO);

        // Assert
        assertNotNull(result);
        assertEquals(flashcardDTO.getId(), result.getId());
        assertEquals(flashcardDTO.getQuestion(), result.getQuestion());
        assertEquals(flashcardDTO.getLevel(), result.getLevel());
        assertEquals(flashcardDTO.getLastStudiedAt(), result.getLastStudiedAt());
        assertEquals(flashcardDTO.getQuestionType(), result.getQuestionType());
        assertEquals(user, result.getUser());
        assertEquals(material, result.getMaterial());

        // Check that answers set is initialized but empty (answers are added separately)
        assertNotNull(result.getAnswers());
        assertTrue(result.getAnswers().isEmpty());

        // Verify repository interactions
        verify(userRepository).findById(1);
        verify(materialRepository).findById(1L);
    }

    @Test
    void toEntity_WithNullDTO_ShouldReturnNull() {
        // Act
        Flashcard result = flashcardMapper.toEntity(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_WithNullIds_ShouldMapOtherFields() {
        // Arrange
        flashcardDTO.setUserId(null);
        flashcardDTO.setMaterialId(null);

        // Act
        Flashcard result = flashcardMapper.toEntity(flashcardDTO);

        // Assert
        assertNotNull(result);
        assertEquals(flashcardDTO.getId(), result.getId());
        assertEquals(flashcardDTO.getQuestion(), result.getQuestion());
        assertEquals(flashcardDTO.getLevel(), result.getLevel());
        assertEquals(flashcardDTO.getLastStudiedAt(), result.getLastStudiedAt());
        assertEquals(flashcardDTO.getQuestionType(), result.getQuestionType());
        assertNull(result.getUser());
        assertNull(result.getMaterial());
        assertNotNull(result.getAnswers());
    }

    @Test
    void toEntity_WithNonexistentReferences_ShouldMapOtherFields() {
        // Arrange
        flashcardDTO.setUserId(999);
        flashcardDTO.setMaterialId(999L);

        when(userRepository.findById(999)).thenReturn(Optional.empty());
        when(materialRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Flashcard result = flashcardMapper.toEntity(flashcardDTO);

        // Assert
        assertNotNull(result);
        assertEquals(flashcardDTO.getId(), result.getId());
        assertEquals(flashcardDTO.getQuestion(), result.getQuestion());
        assertEquals(flashcardDTO.getLevel(), result.getLevel());
        assertEquals(flashcardDTO.getLastStudiedAt(), result.getLastStudiedAt());
        assertEquals(flashcardDTO.getQuestionType(), result.getQuestionType());
        assertNull(result.getUser());
        assertNull(result.getMaterial());

        // Verify repository interactions
        verify(userRepository).findById(999);
        verify(materialRepository).findById(999L);
    }
}