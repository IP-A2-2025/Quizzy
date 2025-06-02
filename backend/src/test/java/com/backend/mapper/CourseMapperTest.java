package com.backend.mapper;

import com.backend.dto.CourseDTO;
import com.backend.model.Course;
import com.backend.model.User;
import com.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseMapperTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CourseMapper courseMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toDTO_WithValidCourse_ShouldMapAllFields() {
        // Arrange
        User professor = new User();
        professor.setId(1);
        professor.setFirstName("John");
        professor.setLastName("Doe");

        Course course = new Course();
        course.setId(1L);
        course.setTitle("Introduction to Computer Science");
        course.setDescription("Learn basic programming concepts");
        course.setSemestru("Fall 2025");
        course.setProfessor(professor);

        // Act
        CourseDTO result = courseMapper.toDTO(course);

        // Assert
        assertNotNull(result);
        assertEquals(course.getId(), result.getId());
        assertEquals(course.getTitle(), result.getTitle());
        assertEquals(course.getDescription(), result.getDescription());
        assertEquals(course.getSemestru(), result.getSemestru());
        assertEquals(professor.getId(), result.getProfessorId());
    }

    @Test
    void toDTO_WithNullCourse_ShouldReturnNull() {
        // Act
        CourseDTO result = courseMapper.toDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toDTO_WithNullProfessor_ShouldMapOtherFields() {
        // Arrange
        Course course = new Course();
        course.setId(1L);
        course.setTitle("Introduction to Computer Science");
        course.setDescription("Learn basic programming concepts");
        course.setSemestru("Fall 2025");
        course.setProfessor(null);

        // Act
        CourseDTO result = courseMapper.toDTO(course);

        // Assert
        assertNotNull(result);
        assertEquals(course.getId(), result.getId());
        assertEquals(course.getTitle(), result.getTitle());
        assertEquals(course.getDescription(), result.getDescription());
        assertEquals(course.getSemestru(), result.getSemestru());
        assertNull(result.getProfessorId());
    }

    @Test
    void toEntity_WithValidDTO_ShouldMapAllFields() {
        // Arrange
        CourseDTO dto = new CourseDTO();
        dto.setId(1L);
        dto.setTitle("Introduction to Computer Science");
        dto.setDescription("Learn basic programming concepts");
        dto.setSemestru("Fall 2025");
        dto.setProfessorId(1);

        User professor = new User();
        professor.setId(1);
        professor.setFirstName("John");
        professor.setLastName("Doe");

        when(userRepository.findById(1)).thenReturn(Optional.of(professor));

        // Act
        Course result = courseMapper.toEntity(dto);

        // Assert
        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getTitle(), result.getTitle());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getSemestru(), result.getSemestru());
        assertEquals(professor, result.getProfessor());

        // Verify repository interactions
        verify(userRepository).findById(1);
    }

    @Test
    void toEntity_WithNullDTO_ShouldReturnNull() {
        // Act
        Course result = courseMapper.toEntity(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_WithNullProfessorId_ShouldMapOtherFields() {
        // Arrange
        CourseDTO dto = new CourseDTO();
        dto.setId(1L);
        dto.setTitle("Introduction to Computer Science");
        dto.setDescription("Learn basic programming concepts");
        dto.setSemestru("Fall 2025");
        dto.setProfessorId(null);

        // Act
        Course result = courseMapper.toEntity(dto);

        // Assert
        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getTitle(), result.getTitle());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getSemestru(), result.getSemestru());
        assertNull(result.getProfessor());
    }

    @Test
    void toEntity_WithNonexistentProfessor_ShouldMapOtherFields() {
        // Arrange
        CourseDTO dto = new CourseDTO();
        dto.setId(1L);
        dto.setTitle("Introduction to Computer Science");
        dto.setDescription("Learn basic programming concepts");
        dto.setSemestru("Fall 2025");
        dto.setProfessorId(999); // Non-existent professor ID

        when(userRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Course result = courseMapper.toEntity(dto);

        // Assert
        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getTitle(), result.getTitle());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getSemestru(), result.getSemestru());
        assertNull(result.getProfessor());

        // Verify repository interactions
        verify(userRepository).findById(999);
    }
}