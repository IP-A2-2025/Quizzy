package com.backend.dto;

import lombok.Data;

@Data
public class TestAnswerDTO {
    private Long id;
    private String optionText;
    private boolean isCorrect;
    private Long questionId;

    public void setId(Long id) {
        this.id = id;
    }
    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }
    public void setCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
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
    public Long getQuestionId() {
        return questionId;
    }
}