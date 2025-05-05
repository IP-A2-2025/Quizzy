package com.backend.mapper;

import com.backend.dto.MaterialDTO;
import com.backend.model.Course;
import com.backend.model.Material;
import com.backend.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MaterialMapperTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private MaterialMapper materialMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toDTO_WithValidMaterial_ShouldMapAllFields() {
        // Arrange
        Course course = new Course();
        course.setId(1L);
        course.setTitle("Introduction to Computer Science");

        Material material = new Material();
        material.setId(1L);
        material.setName("Lecture Notes");
        material.setPath("/courses/cs101/notes.pdf");
        material.setCourse(course);

        // Act
        MaterialDTO result = materialMapper.toDTO(material);

        // Assert
        assertNotNull(result);
        assertEquals(material.getId(), result.getId());
        assertEquals(material.getName(), result.getName());
        assertEquals(material.getPath(), result.getPath());
        assertEquals(course.getId(), result.getCourseId());
    }

    @Test
    void toDTO_WithNullMaterial_ShouldReturnNull() {
        // Act
        MaterialDTO result = materialMapper.toDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toDTO_WithNullCourse_ShouldMapOtherFields() {
        // Arrange
        Material material = new Material();
        material.setId(1L);
        material.setName("Lecture Notes");
        material.setPath("/courses/cs101/notes.pdf");
        material.setCourse(null);

        // Act
        MaterialDTO result = materialMapper.toDTO(material);

        // Assert
        assertNotNull(result);
        assertEquals(material.getId(), result.getId());
        assertEquals(material.getName(), result.getName());
        assertEquals(material.getPath(), result.getPath());
        assertNull(result.getCourseId());
    }

    @Test
    void toEntity_WithValidDTO_ShouldMapAllFields() {
        // Arrange
        MaterialDTO dto = new MaterialDTO();
        dto.setId(1L);
        dto.setName("Lecture Notes");
        dto.setPath("/courses/cs101/notes.pdf");
        dto.setCourseId(1L);

        Course course = new Course();
        course.setId(1L);
        course.setTitle("Introduction to Computer Science");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // Act
        Material result = materialMapper.toEntity(dto);

        // Assert
        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getPath(), result.getPath());
        assertEquals(course, result.getCourse());

        // Verify repository interactions
        verify(courseRepository).findById(1L);
    }

    @Test
    void toEntity_WithNullDTO_ShouldReturnNull() {
        // Act
        Material result = materialMapper.toEntity(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_WithNullCourseId_ShouldMapOtherFields() {
        // Arrange
        MaterialDTO dto = new MaterialDTO();
        dto.setId(1L);
        dto.setName("Lecture Notes");
        dto.setPath("/courses/cs101/notes.pdf");
        dto.setCourseId(null);

        // Act
        Material result = materialMapper.toEntity(dto);

        // Assert
        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getPath(), result.getPath());
        assertNull(result.getCourse());
    }

    @Test
    void toEntity_WithNonexistentCourse_ShouldMapOtherFields() {
        // Arrange
        MaterialDTO dto = new MaterialDTO();
        dto.setId(1L);
        dto.setName("Lecture Notes");
        dto.setPath("/courses/cs101/notes.pdf");
        dto.setCourseId(999L); // Non-existent course ID

        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Material result = materialMapper.toEntity(dto);

        // Assert
        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getPath(), result.getPath());
        assertNull(result.getCourse());

        // Verify repository interactions
        verify(courseRepository).findById(999L);
    }
}