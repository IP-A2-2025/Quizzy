package com.backend.dto;

import lombok.Data;

@Data
public class GeminiFeedbackDTO {
    private Long id;
    private Double score;
    private Long flashcardId;
    private Integer userId;

    public void setId(Long id) { this.id = id;}
    public void setScore(Double score) { this.score = score;}
    public void setFlashcardId(Long flashcardId) { this.flashcardId = flashcardId;}
    public void setUserId(Integer userId) { this.userId = userId;}

    public Long getId() { return id;}
    public Double getScore() { return score;}
    public Long getFlashcardId() { return flashcardId;}
    public Integer getUserId() { return userId;}
}
