package com.backend.dto;

import lombok.Data;

@Data
public class TestQuestionDTO {
    private Long id;
    private String questionText;
    private float pointValue;
    private Long testId;
}