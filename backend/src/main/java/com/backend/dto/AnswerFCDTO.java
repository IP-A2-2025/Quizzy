package com.backend.dto;

import lombok.Data;

@Data
public class AnswerFCDTO {
    private Long id;
    private String optionText;
    private boolean isCorrect;
    private Long flashcardId;
}