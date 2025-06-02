package com.backend.mapper;

import com.backend.dto.TestAnswerDTO;
import com.backend.model.TestAnswer;
import com.backend.model.TestQuestion;
import com.backend.repository.TestQuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestAnswerMapperTest {

    @Mock
    private TestQuestionRepository testQuestionRepository;

    @InjectMocks
    private TestAnswerMapper answerMapper;

    private TestAnswer answer;
    private TestAnswerDTO answerDTO;
    private TestQuestion question;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        question = new TestQuestion();
        question.setId(1L);
        question.setQuestionText("What is 2+2?");

        answer = new TestAnswer();
        answer.setId(1L);
        answer.setOptionText("4");
        answer.setCorrect(true);
        answer.setTestQuestion(question);

        answerDTO = new TestAnswerDTO();
        answerDTO.setId(1L);
        answerDTO.setOptionText("4");
        answerDTO.setCorrect(true);
        answerDTO.setQuestionId(1L);
    }

    @Test
    void toDTO_WithValidAnswer_ShouldMapAllFields() {
        // Act
        TestAnswerDTO result = answerMapper.toDTO(answer);

        // Assert
        assertNotNull(result);
        assertEquals(answer.getId(), result.getId());
        assertEquals(answer.getOptionText(), result.getOptionText());
        assertEquals(answer.isCorrect(), result.isCorrect());
        assertEquals(question.getId(), result.getQuestionId());
    }

    @Test
    void toDTO_WithNullAnswer_ShouldReturnNull() {
        // Act
        TestAnswerDTO result = answerMapper.toDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toDTO_WithNullQuestion_ShouldMapOtherFields() {
        // Arrange
        answer.setTestQuestion(null);

        // Act
        TestAnswerDTO result = answerMapper.toDTO(answer);

        // Assert
        assertNotNull(result);
        assertEquals(answer.getId(), result.getId());
        assertEquals(answer.getOptionText(), result.getOptionText());
        assertEquals(answer.isCorrect(), result.isCorrect());
        assertNull(result.getQuestionId());
    }

    @Test
    void toEntity_WithValidDTO_ShouldMapAllFields() {
        // Arrange
        when(testQuestionRepository.findById(1L)).thenReturn(Optional.of(question));

        // Act
        TestAnswer result = answerMapper.toEntity(answerDTO);

        // Assert
        assertNotNull(result);
        assertEquals(answerDTO.getId(), result.getId());
        assertEquals(answerDTO.getOptionText(), result.getOptionText());
        assertEquals(answerDTO.isCorrect(), result.isCorrect());
        assertEquals(question, result.getTestQuestion());

        // Verify repository interactions
        verify(testQuestionRepository).findById(1L);
    }

    @Test
    void toEntity_WithNullDTO_ShouldReturnNull() {
        // Act
        TestAnswer result = answerMapper.toEntity(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_WithNullQuestionId_ShouldMapOtherFields() {
        // Arrange
        answerDTO.setQuestionId(null);

        // Act
        TestAnswer result = answerMapper.toEntity(answerDTO);

        // Assert
        assertNotNull(result);
        assertEquals(answerDTO.getId(), result.getId());
        assertEquals(answerDTO.getOptionText(), result.getOptionText());
        assertEquals(answerDTO.isCorrect(), result.isCorrect());
        assertNull(result.getTestQuestion());
    }

    @Test
    void toEntity_WithNonexistentQuestion_ShouldMapOtherFields() {
        // Arrange
        answerDTO.setQuestionId(999L);

        when(testQuestionRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        TestAnswer result = answerMapper.toEntity(answerDTO);

        // Assert
        assertNotNull(result);
        assertEquals(answerDTO.getId(), result.getId());
        assertEquals(answerDTO.getOptionText(), result.getOptionText());
        assertEquals(answerDTO.isCorrect(), result.isCorrect());
        assertNull(result.getTestQuestion());

        // Verify repository interactions
        verify(testQuestionRepository).findById(999L);
    }
}