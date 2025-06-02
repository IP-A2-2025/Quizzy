package com.backend.mapper;

import com.backend.dto.TestDTO;
import com.backend.model.Course;
import com.backend.model.TestEntity;
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

class TestMapperTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TestMapper testMapper;

    private User professor;
    private Course course;
    private TestEntity testEntity;
    private TestDTO testDTO;
    private final Date testDate = new Date();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        professor = new User();
        professor.setId(1);
        professor.setFirstName("Professor");
        professor.setLastName("Smith");

        course = new Course();
        course.setId(1L);
        course.setTitle("Mathematics");

        testEntity = new TestEntity();
        testEntity.setId(1L);
        testEntity.setTitle("Midterm Exam");
        testEntity.setDescription("Covers chapters 1-5");
        testEntity.setDate(testDate);
        testEntity.setProfessor(professor);
        testEntity.setCourse(course);

        testDTO = new TestDTO();
        testDTO.setId(1L);
        testDTO.setTitle("Midterm Exam");
        testDTO.setDescription("Covers chapters 1-5");
        testDTO.setDate(testDate);
        testDTO.setProfessorId(1);
        testDTO.setCourseId(1L);
    }

    @Test
    void toDTO_WithValidTest_ShouldMapAllFields() {
        // Act
        TestDTO result = testMapper.toDTO(testEntity);

        // Assert
        assertNotNull(result);
        assertEquals(testEntity.getId(), result.getId());
        assertEquals(testEntity.getTitle(), result.getTitle());
        assertEquals(testEntity.getDescription(), result.getDescription());
        assertEquals(testEntity.getDate(), result.getDate());
        assertEquals(professor.getId(), result.getProfessorId());
        assertEquals(course.getId(), result.getCourseId());
    }

    @Test
    void toDTO_WithNullTest_ShouldReturnNull() {
        // Act
        TestDTO result = testMapper.toDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toDTO_WithNullProfessorAndCourse_ShouldMapOtherFields() {
        // Arrange
        testEntity.setProfessor(null);
        testEntity.setCourse(null);

        // Act
        TestDTO result = testMapper.toDTO(testEntity);

        // Assert
        assertNotNull(result);
        assertEquals(testEntity.getId(), result.getId());
        assertEquals(testEntity.getTitle(), result.getTitle());
        assertEquals(testEntity.getDescription(), result.getDescription());
        assertEquals(testEntity.getDate(), result.getDate());
        assertNull(result.getProfessorId());
        assertNull(result.getCourseId());
    }

    @Test
    void toEntity_WithValidDTO_ShouldMapAllFields() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(professor));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // Act
        TestEntity result = testMapper.toEntity(testDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testDTO.getId(), result.getId());
        assertEquals(testDTO.getTitle(), result.getTitle());
        assertEquals(testDTO.getDescription(), result.getDescription());
        assertEquals(testDTO.getDate(), result.getDate());
        assertEquals(professor, result.getProfessor());
        assertEquals(course, result.getCourse());

        // Verify repository interactions
        verify(userRepository).findById(1);
        verify(courseRepository).findById(1L);
    }

    @Test
    void toEntity_WithNullDTO_ShouldReturnNull() {
        // Act
        TestEntity result = testMapper.toEntity(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_WithNullIds_ShouldMapOtherFields() {
        // Arrange
        testDTO.setProfessorId(null);
        testDTO.setCourseId(null);

        // Act
        TestEntity result = testMapper.toEntity(testDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testDTO.getId(), result.getId());
        assertEquals(testDTO.getTitle(), result.getTitle());
        assertEquals(testDTO.getDescription(), result.getDescription());
        assertEquals(testDTO.getDate(), result.getDate());
        assertNull(result.getProfessor());
        assertNull(result.getCourse());
    }

    @Test
    void toEntity_WithNonexistentEntities_ShouldMapOtherFields() {
        // Arrange
        testDTO.setProfessorId(999);
        testDTO.setCourseId(999L);

        when(userRepository.findById(999)).thenReturn(Optional.empty());
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        TestEntity result = testMapper.toEntity(testDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testDTO.getId(), result.getId());
        assertEquals(testDTO.getTitle(), result.getTitle());
        assertEquals(testDTO.getDescription(), result.getDescription());
        assertEquals(testDTO.getDate(), result.getDate());
        assertNull(result.getProfessor());
        assertNull(result.getCourse());

        // Verify repository interactions
        verify(userRepository).findById(999);
        verify(courseRepository).findById(999L);
    }
}