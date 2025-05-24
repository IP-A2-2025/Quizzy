package com.backend.mapper;

import com.backend.dto.FlashcardProgressDTO;
import com.backend.model.Flashcard;
import com.backend.model.FlashcardProgress;
import com.backend.model.User;
import com.backend.repository.FlashcardRepository;
import com.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlashcardProgressMapperTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FlashcardRepository flashcardRepository;

    @InjectMocks
    private FlashcardProgressMapper flashcardProgressMapper;

    private User user;
    private Flashcard flashcard;
    private FlashcardProgress flashcardProgress;
    private FlashcardProgressDTO flashcardProgressDTO;
    private final Date dueDate = new Date();
    private final Date lastReviewed = new Date();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1);
        user.setFirstName("Jane");
        user.setLastName("Smith");

        flashcard = new Flashcard();
        flashcard.setId(2L);
        flashcard.setQuestion("Mock question?");

        flashcardProgress = new FlashcardProgress();
        flashcardProgress.setId(3L);
        flashcardProgress.setUser(user);
        flashcardProgress.setFlashcard(flashcard);
        flashcardProgress.setEaseFactor(2.3);
        flashcardProgress.setInterval(8);
        flashcardProgress.setRepetitions(4);
        flashcardProgress.setDueDate(dueDate);
        flashcardProgress.setLastReviewed(lastReviewed);

        flashcardProgressDTO = new FlashcardProgressDTO();
        flashcardProgressDTO.setId(3L);
        flashcardProgressDTO.setUserId(1);
        flashcardProgressDTO.setFlashcardId(2L);
        flashcardProgressDTO.setEaseFactor(2.3);
        flashcardProgressDTO.setInterval(8);
        flashcardProgressDTO.setRepetitions(4);
        flashcardProgressDTO.setDueDate(dueDate);
        flashcardProgressDTO.setLastReviewed(lastReviewed);
    }

    @Test
    void toDTO_WithValidFlashcardProgress_ShouldMapAllFields() {
        FlashcardProgressDTO result = flashcardProgressMapper.toDTO(flashcardProgress);

        assertNotNull(result);
        assertEquals(flashcardProgress.getId(), result.getId());
        assertEquals(user.getId(), result.getUserId());
        assertEquals(flashcard.getId(), result.getFlashcardId());
        assertEquals(flashcardProgress.getEaseFactor(), result.getEaseFactor());
        assertEquals(flashcardProgress.getInterval(), result.getInterval());
        assertEquals(flashcardProgress.getRepetitions(), result.getRepetitions());
        assertEquals(flashcardProgress.getDueDate(), result.getDueDate());
        assertEquals(flashcardProgress.getLastReviewed(), result.getLastReviewed());
    }

    @Test
    void toDTO_WithNullFlashcardProgress_ShouldReturnNull() {
        FlashcardProgressDTO result = flashcardProgressMapper.toDTO(null);

        assertNull(result);
    }

    @Test
    void toDTO_WithNullUserAndFlashcard_ShouldMapFieldsExceptRelations() {
        flashcardProgress.setUser(null);
        flashcardProgress.setFlashcard(null);

        FlashcardProgressDTO result = flashcardProgressMapper.toDTO(flashcardProgress);

        assertNotNull(result);
        assertNull(result.getUserId());
        assertNull(result.getFlashcardId());
        assertEquals(flashcardProgress.getId(), result.getId());
        assertEquals(flashcardProgress.getEaseFactor(), result.getEaseFactor());
        assertEquals(flashcardProgress.getInterval(), result.getInterval());
        assertEquals(flashcardProgress.getRepetitions(), result.getRepetitions());
        assertEquals(flashcardProgress.getDueDate(), result.getDueDate());
        assertEquals(flashcardProgress.getLastReviewed(), result.getLastReviewed());
    }

    @Test
    void toEntity_WithValidDTO_ShouldMapAllFields() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(flashcardRepository.findById(2L)).thenReturn(Optional.of(flashcard));

        FlashcardProgress result = flashcardProgressMapper.toEntity(flashcardProgressDTO);

        assertNotNull(result);
        assertEquals(flashcardProgressDTO.getId(), result.getId());
        assertEquals(user, result.getUser());
        assertEquals(flashcard, result.getFlashcard());
        assertEquals(flashcardProgressDTO.getEaseFactor(), result.getEaseFactor());
        assertEquals(flashcardProgressDTO.getInterval(), result.getInterval());
        assertEquals(flashcardProgressDTO.getRepetitions(), result.getRepetitions());
        assertEquals(flashcardProgressDTO.getDueDate(), result.getDueDate());
        assertEquals(flashcardProgressDTO.getLastReviewed(), result.getLastReviewed());

        verify(userRepository).findById(1);
        verify(flashcardRepository).findById(2L);
    }

    @Test
    void toEntity_WithNullDTO_ShouldReturnNull() {
        FlashcardProgress result = flashcardProgressMapper.toEntity(null);

        assertNull(result);
    }

    @Test
    void toEntity_WithNullIds_ShouldSetRelationsNullAndMapOtherFields() {
        flashcardProgressDTO.setUserId(null);
        flashcardProgressDTO.setFlashcardId(null);

        FlashcardProgress result = flashcardProgressMapper.toEntity(flashcardProgressDTO);

        assertNotNull(result);
        assertNull(result.getUser());
        assertNull(result.getFlashcard());
        assertEquals(flashcardProgressDTO.getId(), result.getId());
        assertEquals(flashcardProgressDTO.getEaseFactor(), result.getEaseFactor());
        assertEquals(flashcardProgressDTO.getInterval(), result.getInterval());
        assertEquals(flashcardProgressDTO.getRepetitions(), result.getRepetitions());
        assertEquals(flashcardProgressDTO.getDueDate(), result.getDueDate());
        assertEquals(flashcardProgressDTO.getLastReviewed(), result.getLastReviewed());
    }

    @Test
    void toEntity_WithNonexistentReferences_ShouldSetRelationsNull() {
        flashcardProgressDTO.setUserId(999);
        flashcardProgressDTO.setFlashcardId(9999L);

        when(userRepository.findById(999)).thenReturn(Optional.empty());
        when(flashcardRepository.findById(9999L)).thenReturn(Optional.empty());

        FlashcardProgress result = flashcardProgressMapper.toEntity(flashcardProgressDTO);

        assertNotNull(result);
        assertNull(result.getUser());
        assertNull(result.getFlashcard());
        assertEquals(flashcardProgressDTO.getId(), result.getId());

        verify(userRepository).findById(999);
        verify(flashcardRepository).findById(9999L);
    }
}
