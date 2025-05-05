package com.backend.mapper;

import com.backend.dto.GradeDTO;
import com.backend.model.Grade;
import com.backend.model.GradeId;
import com.backend.model.TestEntity;
import com.backend.model.User;
import com.backend.repository.TestRepository;
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

class GradeMapperTest {

    @Mock
    private TestRepository testRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GradeMapper gradeMapper;

    private Grade grade;
    private GradeDTO gradeDTO;
    private User student;
    private TestEntity testEntity;
    private final Date submissionDate = new Date();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        student = new User();
        student.setId(1);
        student.setFirstName("John");
        student.setLastName("Doe");

        testEntity = new TestEntity();
        testEntity.setId(1L);
        testEntity.setTitle("Math Test");

        GradeId gradeId = new GradeId();
        gradeId.setTestID(1L);
        gradeId.setUserID(1);

        grade = new Grade();
        grade.setId(gradeId);
        grade.setGrade(90.5F);
        grade.setSubmissionDate(submissionDate);
        grade.setUser(student);
        grade.setTest(testEntity);

        gradeDTO = new GradeDTO();
        gradeDTO.setTestId(1L);
        gradeDTO.setUserId(1);
        gradeDTO.setGrade(90.5F);
        gradeDTO.setSubmissionDate(submissionDate);
    }

    @Test
    void toDTO_WithValidGrade_ShouldMapAllFields() {
        // Act
        GradeDTO result = gradeMapper.toDTO(grade);

        // Assert
        assertNotNull(result);
        assertEquals(grade.getId().getTestID(), result.getTestId());
        assertEquals(grade.getId().getUserID(), result.getUserId());
        assertEquals(grade.getGrade(), result.getGrade());
        assertEquals(grade.getSubmissionDate(), result.getSubmissionDate());
    }

    @Test
    void toDTO_WithNullGrade_ShouldReturnNull() {
        // Act
        GradeDTO result = gradeMapper.toDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_WithValidDTO_ShouldMapAllFields() {
        // Arrange
        when(testRepository.findById(1L)).thenReturn(Optional.of(testEntity));
        when(userRepository.findById(1)).thenReturn(Optional.of(student));

        // Act
        Grade result = gradeMapper.toEntity(gradeDTO);

        // Assert
        assertNotNull(result);
        assertEquals(gradeDTO.getTestId(), result.getId().getTestID());
        assertEquals(gradeDTO.getUserId(), result.getId().getUserID());
        assertEquals(gradeDTO.getGrade(), result.getGrade());
        assertEquals(gradeDTO.getSubmissionDate(), result.getSubmissionDate());
        assertEquals(testEntity, result.getTest());
        assertEquals(student, result.getUser());

        // Verify repository interactions
        verify(testRepository).findById(1L);
        verify(userRepository).findById(1);
    }

    @Test
    void toEntity_WithNullDTO_ShouldReturnNull() {
        // Act
        Grade result = gradeMapper.toEntity(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_WithNonexistentEntities_ShouldStillSetId() {
        // Arrange
        when(testRepository.findById(1L)).thenReturn(Optional.empty());
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        Grade result = gradeMapper.toEntity(gradeDTO);

        // Assert
        assertNotNull(result);
        assertEquals(gradeDTO.getTestId(), result.getId().getTestID());
        assertEquals(gradeDTO.getUserId(), result.getId().getUserID());
        assertEquals(gradeDTO.getGrade(), result.getGrade());
        assertEquals(gradeDTO.getSubmissionDate(), result.getSubmissionDate());
        assertNull(result.getTest());
        assertNull(result.getUser());

        // Verify repository interactions
        verify(testRepository).findById(1L);
        verify(userRepository).findById(1);
    }
}