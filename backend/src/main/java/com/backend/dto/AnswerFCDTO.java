package com.backend.dto;

import lombok.Data;

@Data
public class AnswerFCDTO {
    private Long id;
    private String optionText;
    private boolean isCorrect;
    private Long flashcardId;

    public void setId(Long id) {
        this.id = id;
    }
    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
    public void setFlashcardId(Long flashcardId) {
        this.flashcardId = flashcardId;
    }

    public Long getId() {
        return id;
    }
    public String getOptionText() {
        return optionText;
    }
    public boolean isCorrect() {
        return isCorrect;
    }
    public Long getFlashcardId() {
        return flashcardId;
    }
}