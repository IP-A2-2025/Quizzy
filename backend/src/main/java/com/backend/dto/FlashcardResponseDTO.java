package com.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlashcardResponseDTO {
    private Long flashcardId;
    private Integer userId;
    private int quality;
    private boolean isCorrect;
    private Double percentageCorrect;
    private boolean isWrittenAnswer;
    
    @Override
    public String toString() {
        return "FlashcardResponseDTO{" +
                "flashcardId=" + flashcardId +
                ", userId=" + userId +
                ", quality=" + quality +
                ", isCorrect=" + isCorrect +
                ", percentageCorrect=" + percentageCorrect +
                ", isWrittenAnswer=" + isWrittenAnswer +
                "}";
    }
}
