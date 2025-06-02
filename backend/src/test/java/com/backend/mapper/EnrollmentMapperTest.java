package com.backend.mapper;

import com.backend.dto.EnrollmentDTO;
import com.backend.model.Course;
import com.backend.model.Enrollment;
import com.backend.model.EnrollmentId;
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

class EnrollmentMapperTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private EnrollmentMapper enrollmentMapper;

    private Enrollment enrollment;
    private EnrollmentDTO enrollmentDTO;
    private User student;
    private Course course;
    private final Date enrollmentDate = new Date();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        student = new User();
        student.setId(1);
        student.setFirstName("John");
        student.setLastName("Doe");

        course = new Course();
        course.setId(1L);
        course.setTitle("Mathematics");

        EnrollmentId enrollmentId = new EnrollmentId();
        enrollmentId.setUserID(1);
        enrollmentId.setCourseID(1L);

        enrollment = new Enrollment();
        enrollment.setId(enrollmentId);
        enrollment.setEnrollmentDate(enrollmentDate);
        enrollment.setGrade(String.valueOf(85.0));
        enrollment.setUser(student);
        enrollment.setCourse(course);

        enrollmentDTO = new EnrollmentDTO();
        enrollmentDTO.setUserId(1);
        enrollmentDTO.setCourseId(1L);
        enrollmentDTO.setEnrollmentDate(enrollmentDate);
        enrollmentDTO.setGrade(String.valueOf(85.0));
    }

    @Test
    void toDTO_WithValidEnrollment_ShouldMapAllFields() {
        // Act
        EnrollmentDTO result = enrollmentMapper.toDTO(enrollment);

        // Assert
        assertNotNull(result);
        assertEquals(enrollment.getId().getUserID(), result.getUserId());
        assertEquals(enrollment.getId().getCourseID(), result.getCourseId());
        assertEquals(enrollment.getEnrollmentDate(), result.getEnrollmentDate());
        assertEquals(enrollment.getGrade(), result.getGrade());
    }

    @Test
    void toDTO_WithNullEnrollment_ShouldReturnNull() {
        // Act
        EnrollmentDTO result = enrollmentMapper.toDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_WithValidDTO_ShouldMapAllFields() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // Act
        Enrollment result = enrollmentMapper.toEntity(enrollmentDTO);

        // Assert
        assertNotNull(result);
        assertEquals(enrollmentDTO.getUserId(), result.getId().getUserID());
        assertEquals(enrollmentDTO.getCourseId(), result.getId().getCourseID());
        assertEquals(enrollmentDTO.getEnrollmentDate(), result.getEnrollmentDate());
        assertEquals(enrollmentDTO.getGrade(), result.getGrade());
        assertEquals(student, result.getUser());
        assertEquals(course, result.getCourse());

        // Verify repository interactions
        verify(userRepository).findById(1);
        verify(courseRepository).findById(1L);
    }

    @Test
    void toEntity_WithNullDTO_ShouldReturnNull() {
        // Act
        Enrollment result = enrollmentMapper.toEntity(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_WithNonexistentEntities_ShouldStillSetId() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Enrollment result = enrollmentMapper.toEntity(enrollmentDTO);

        // Assert
        assertNotNull(result);
        assertEquals(enrollmentDTO.getUserId(), result.getId().getUserID());
        assertEquals(enrollmentDTO.getCourseId(), result.getId().getCourseID());
        assertEquals(enrollmentDTO.getEnrollmentDate(), result.getEnrollmentDate());
        assertEquals(enrollmentDTO.getGrade(), result.getGrade());
        assertNull(result.getUser());
        assertNull(result.getCourse());

        // Verify repository interactions
        verify(userRepository).findById(1);
        verify(courseRepository).findById(1L);
    }
}