package com.backend.mapper;

import com.backend.dto.FlashcardSessionDTO;
import com.backend.model.Course;
import com.backend.model.FlashcardSession;
import com.backend.model.User;
import com.backend.repository.CourseRepository;
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

class FlashcardSessionMapperTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private FlashcardSessionMapper flashcardSessionMapper;

    private FlashcardSession flashcardSession;
    private FlashcardSessionDTO flashcardSessionDTO;
    private User user;
    private Course course;
    private final Date timestamp = new Date();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Doe");

        course = new Course();
        course.setId(1L);
        course.setTitle("Java Programming");

        flashcardSession = new FlashcardSession();
        flashcardSession.setId(1L);
        flashcardSession.setTimestamp(timestamp);
        flashcardSession.setFlashcardCount(10);
        flashcardSession.setUser(user);
        flashcardSession.setCourse(course);

        flashcardSessionDTO = new FlashcardSessionDTO();
        flashcardSessionDTO.setId(1L);
        flashcardSessionDTO.setTimestamp(timestamp);
        flashcardSessionDTO.setFlashcardCount(10);
        flashcardSessionDTO.setUserId(1);
        flashcardSessionDTO.setCourseId(1L);
    }

    @Test
    void toDTO_WithValidSession_ShouldMapAllFields() {
        // Act
        FlashcardSessionDTO result = flashcardSessionMapper.toDTO(flashcardSession);

        // Assert
        assertNotNull(result);
        assertEquals(flashcardSession.getId(), result.getId());
        assertEquals(flashcardSession.getTimestamp(), result.getTimestamp());
        assertEquals(flashcardSession.getFlashcardCount(), result.getFlashcardCount());
        assertEquals(user.getId(), result.getUserId());
        assertEquals(course.getId(), result.getCourseId());
    }

    @Test
    void toDTO_WithNullSession_ShouldReturnNull() {
        // Act
        FlashcardSessionDTO result = flashcardSessionMapper.toDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toDTO_WithNullRelations_ShouldMapOtherFields() {
        // Arrange
        flashcardSession.setUser(null);
        flashcardSession.setCourse(null);

        // Act
        FlashcardSessionDTO result = flashcardSessionMapper.toDTO(flashcardSession);

        // Assert
        assertNotNull(result);
        assertEquals(flashcardSession.getId(), result.getId());
        assertEquals(flashcardSession.getTimestamp(), result.getTimestamp());
        assertEquals(flashcardSession.getFlashcardCount(), result.getFlashcardCount());
        assertNull(result.getUserId());
        assertNull(result.getCourseId());
    }

    @Test
    void toEntity_WithValidDTO_ShouldMapAllFields() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // Act
        FlashcardSession result = flashcardSessionMapper.toEntity(flashcardSessionDTO);

        // Assert
        assertNotNull(result);
        assertEquals(flashcardSessionDTO.getId(), result.getId());
        assertEquals(flashcardSessionDTO.getTimestamp(), result.getTimestamp());
        assertEquals(flashcardSessionDTO.getFlashcardCount(), result.getFlashcardCount());
        assertEquals(user, result.getUser());
        assertEquals(course, result.getCourse());

        // Verify repository interactions
        verify(userRepository).findById(1);
        verify(courseRepository).findById(1L);
    }

    @Test
    void toEntity_WithNullDTO_ShouldReturnNull() {
        // Act
        FlashcardSession result = flashcardSessionMapper.toEntity(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_WithNullIds_ShouldMapOtherFields() {
        // Arrange
        flashcardSessionDTO.setUserId(null);
        flashcardSessionDTO.setCourseId(null);

        // Act
        FlashcardSession result = flashcardSessionMapper.toEntity(flashcardSessionDTO);

        // Assert
        assertNotNull(result);
        assertEquals(flashcardSessionDTO.getId(), result.getId());
        assertEquals(flashcardSessionDTO.getTimestamp(), result.getTimestamp());
        assertEquals(flashcardSessionDTO.getFlashcardCount(), result.getFlashcardCount());
        assertNull(result.getUser());
        assertNull(result.getCourse());
    }

    @Test
    void toEntity_WithNonexistentReferences_ShouldMapOtherFields() {
        // Arrange
        flashcardSessionDTO.setUserId(999);
        flashcardSessionDTO.setCourseId(999L);

        when(userRepository.findById(999)).thenReturn(Optional.empty());
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        FlashcardSession result = flashcardSessionMapper.toEntity(flashcardSessionDTO);

        // Assert
        assertNotNull(result);
        assertEquals(flashcardSessionDTO.getId(), result.getId());
        assertEquals(flashcardSessionDTO.getTimestamp(), result.getTimestamp());
        assertEquals(flashcardSessionDTO.getFlashcardCount(), result.getFlashcardCount());
        assertNull(result.getUser());
        assertNull(result.getCourse());

        // Verify repository interactions
        verify(userRepository).findById(999);
        verify(courseRepository).findById(999L);
    }
}
