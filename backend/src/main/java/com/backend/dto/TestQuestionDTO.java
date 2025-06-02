package com.backend.dto;

import lombok.Data;

@Data
public class TestQuestionDTO {
    private Long id;
    private String questionText;
    private float pointValue;
    private Long testId;

    public void setId(Long id) {
        this.id = id;
    }
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
    public void setPointValue(float pointValue) {
        this.pointValue = pointValue;
    }
    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public Long getId() {
        return id;
    }
    public String getQuestionText() {
        return questionText;
    }
    public float getPointValue() {
        return pointValue;
    }
    public Long getTestId() {
        return testId;
    }
}