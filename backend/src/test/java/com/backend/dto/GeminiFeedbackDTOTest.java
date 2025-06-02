package com.backend.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class GeminiFeedbackDTOTest {

    private GeminiFeedbackDTO geminiFeedbackDTO;

    @BeforeEach
    void setUp() {
        geminiFeedbackDTO = new GeminiFeedbackDTO();
    }

    @Test
    void shouldCreateEmptyGeminiFeedbackDTO() {
        // When
        GeminiFeedbackDTO dto = new GeminiFeedbackDTO();

        // Then
        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getScore());
        assertNull(dto.getFlashcardId());
        assertNull(dto.getUserId());
    }

    @Test
    void shouldSetAndGetId() {
        // Given
        Long expectedId = 123L;

        // When
        geminiFeedbackDTO.setId(expectedId);

        // Then
        assertEquals(expectedId, geminiFeedbackDTO.getId());
    }

    @Test
    void shouldSetAndGetScore() {
        // Given
        Double expectedScore = 87.5;

        // When
        geminiFeedbackDTO.setScore(expectedScore);

        // Then
        assertEquals(expectedScore, geminiFeedbackDTO.getScore());
    }

    @Test
    void shouldSetAndGetFlashcardId() {
        // Given
        Long expectedFlashcardId = 456L;

        // When
        geminiFeedbackDTO.setFlashcardId(expectedFlashcardId);

        // Then
        assertEquals(expectedFlashcardId, geminiFeedbackDTO.getFlashcardId());
    }

    @Test
    void shouldSetAndGetUserId() {
        // Given
        Integer expectedUserId = 789;

        // When
        geminiFeedbackDTO.setUserId(expectedUserId);

        // Then
        assertEquals(expectedUserId, geminiFeedbackDTO.getUserId());
    }

    @Test
    void shouldHandleNullValues() {
        // When
        geminiFeedbackDTO.setId(null);
        geminiFeedbackDTO.setScore(null);
        geminiFeedbackDTO.setFlashcardId(null);
        geminiFeedbackDTO.setUserId(null);

        // Then
        assertNull(geminiFeedbackDTO.getId());
        assertNull(geminiFeedbackDTO.getScore());
        assertNull(geminiFeedbackDTO.getFlashcardId());
        assertNull(geminiFeedbackDTO.getUserId());
    }

    @Test
    void shouldSetAllFieldsCorrectly() {
        // Given
        Long expectedId = 1L;
        Double expectedScore = 95.0;
        Long expectedFlashcardId = 2L;
        Integer expectedUserId = 3;

        // When
        geminiFeedbackDTO.setId(expectedId);
        geminiFeedbackDTO.setScore(expectedScore);
        geminiFeedbackDTO.setFlashcardId(expectedFlashcardId);
        geminiFeedbackDTO.setUserId(expectedUserId);

        // Then
        assertEquals(expectedId, geminiFeedbackDTO.getId());
        assertEquals(expectedScore, geminiFeedbackDTO.getScore());
        assertEquals(expectedFlashcardId, geminiFeedbackDTO.getFlashcardId());
        assertEquals(expectedUserId, geminiFeedbackDTO.getUserId());
    }

    @Test
    void shouldHandleZeroScore() {
        // Given
        Double zeroScore = 0.0;

        // When
        geminiFeedbackDTO.setScore(zeroScore);

        // Then
        assertEquals(zeroScore, geminiFeedbackDTO.getScore());
    }

    @Test
    void shouldHandleMaxScore() {
        // Given
        Double maxScore = 100.0;

        // When
        geminiFeedbackDTO.setScore(maxScore);

        // Then
        assertEquals(maxScore, geminiFeedbackDTO.getScore());
    }

    @Test
    void shouldHandleNegativeScore() {
        // Given
        Double negativeScore = -10.0;

        // When
        geminiFeedbackDTO.setScore(negativeScore);

        // Then
        assertEquals(negativeScore, geminiFeedbackDTO.getScore());
    }

    @Test
    void shouldHandleDecimalScore() {
        // Given
        Double decimalScore = 73.25;

        // When
        geminiFeedbackDTO.setScore(decimalScore);

        // Then
        assertEquals(decimalScore, geminiFeedbackDTO.getScore());
    }

    @Test
    void shouldHandleZeroIds() {
        // Given
        Long zeroLongId = 0L;
        Integer zeroIntId = 0;

        // When
        geminiFeedbackDTO.setId(zeroLongId);
        geminiFeedbackDTO.setFlashcardId(zeroLongId);
        geminiFeedbackDTO.setUserId(zeroIntId);

        // Then
        assertEquals(zeroLongId, geminiFeedbackDTO.getId());
        assertEquals(zeroLongId, geminiFeedbackDTO.getFlashcardId());
        assertEquals(zeroIntId, geminiFeedbackDTO.getUserId());
    }

    @Test
    void shouldHandleNegativeIds() {
        // Given
        Long negativeLongId = -1L;
        Integer negativeIntId = -1;

        // When
        geminiFeedbackDTO.setId(negativeLongId);
        geminiFeedbackDTO.setFlashcardId(negativeLongId);
        geminiFeedbackDTO.setUserId(negativeIntId);

        // Then
        assertEquals(negativeLongId, geminiFeedbackDTO.getId());
        assertEquals(negativeLongId, geminiFeedbackDTO.getFlashcardId());
        assertEquals(negativeIntId, geminiFeedbackDTO.getUserId());
    }

    @Test
    void shouldHandleLargeIds() {
        // Given
        Long largeLongId = Long.MAX_VALUE;
        Integer largeIntId = Integer.MAX_VALUE;

        // When
        geminiFeedbackDTO.setId(largeLongId);
        geminiFeedbackDTO.setFlashcardId(largeLongId);
        geminiFeedbackDTO.setUserId(largeIntId);

        // Then
        assertEquals(largeLongId, geminiFeedbackDTO.getId());
        assertEquals(largeLongId, geminiFeedbackDTO.getFlashcardId());
        assertEquals(largeIntId, geminiFeedbackDTO.getUserId());
    }

    @Test
    void shouldMaintainIndependentFields() {
        // Given
        GeminiFeedbackDTO dto1 = new GeminiFeedbackDTO();
        GeminiFeedbackDTO dto2 = new GeminiFeedbackDTO();

        // When
        dto1.setId(1L);
        dto1.setScore(50.0);
        dto2.setId(2L);
        dto2.setScore(75.0);

        // Then
        assertNotEquals(dto1.getId(), dto2.getId());
        assertNotEquals(dto1.getScore(), dto2.getScore());
    }
} 