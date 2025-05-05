package com.backend.mapper;

import com.backend.dto.TestQuestionDTO;
import com.backend.model.TestEntity;
import com.backend.model.TestQuestion;
import com.backend.repository.TestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestQuestionMapperTest {

    @Mock
    private TestRepository testRepository;

    @InjectMocks
    private TestQuestionMapper questionMapper;

    private TestQuestion question;
    private TestQuestionDTO questionDTO;
    private TestEntity testEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testEntity = new TestEntity();
        testEntity.setId(1L);
        testEntity.setTitle("Math Test");

        question = new TestQuestion();
        question.setId(1L);
        question.setQuestionText("What is 2+2?");
        question.setPointValue(5.0f);
        question.setTest(testEntity);

        questionDTO = new TestQuestionDTO();
        questionDTO.setId(1L);
        questionDTO.setQuestionText("What is 2+2?");
        questionDTO.setPointValue(5.0f);
        questionDTO.setTestId(1L);
    }

    @Test
    void toDTO_WithValidQuestion_ShouldMapAllFields() {
        // Act
        TestQuestionDTO result = questionMapper.toDTO(question);

        // Assert
        assertNotNull(result);
        assertEquals(question.getId(), result.getId());
        assertEquals(question.getQuestionText(), result.getQuestionText());
        assertEquals(question.getPointValue(), result.getPointValue());
        assertEquals(testEntity.getId(), result.getTestId());
    }

    @Test
    void toDTO_WithNullQuestion_ShouldReturnNull() {
        // Act
        TestQuestionDTO result = questionMapper.toDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toDTO_WithNullTest_ShouldMapOtherFields() {
        // Arrange
        question.setTest(null);

        // Act
        TestQuestionDTO result = questionMapper.toDTO(question);

        // Assert
        assertNotNull(result);
        assertEquals(question.getId(), result.getId());
        assertEquals(question.getQuestionText(), result.getQuestionText());
        assertEquals(question.getPointValue(), result.getPointValue());
        assertNull(result.getTestId());
    }

    @Test
    void toEntity_WithValidDTO_ShouldMapAllFields() {
        // Arrange
        when(testRepository.findById(1L)).thenReturn(Optional.of(testEntity));

        // Act
        TestQuestion result = questionMapper.toEntity(questionDTO);

        // Assert
        assertNotNull(result);
        assertEquals(questionDTO.getId(), result.getId());
        assertEquals(questionDTO.getQuestionText(), result.getQuestionText());
        assertEquals(questionDTO.getPointValue(), result.getPointValue());
        assertEquals(testEntity, result.getTest());

        // Verify repository interactions
        verify(testRepository).findById(1L);
    }

    @Test
    void toEntity_WithNullDTO_ShouldReturnNull() {
        // Act
        TestQuestion result = questionMapper.toEntity(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_WithNullTestId_ShouldMapOtherFields() {
        // Arrange
        questionDTO.setTestId(null);

        // Act
        TestQuestion result = questionMapper.toEntity(questionDTO);

        // Assert
        assertNotNull(result);
        assertEquals(questionDTO.getId(), result.getId());
        assertEquals(questionDTO.getQuestionText(), result.getQuestionText());
        assertEquals(questionDTO.getPointValue(), result.getPointValue());
        assertNull(result.getTest());
    }

    @Test
    void toEntity_WithNonexistentTest_ShouldMapOtherFields() {
        // Arrange
        questionDTO.setTestId(999L);

        when(testRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        TestQuestion result = questionMapper.toEntity(questionDTO);

        // Assert
        assertNotNull(result);
        assertEquals(questionDTO.getId(), result.getId());
        assertEquals(questionDTO.getQuestionText(), result.getQuestionText());
        assertEquals(questionDTO.getPointValue(), result.getPointValue());
        assertNull(result.getTest());

        // Verify repository interactions
        verify(testRepository).findById(999L);
    }
}