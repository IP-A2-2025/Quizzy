package com.backend.dto;

import lombok.Data;

import java.util.Date;

@Data
public class FlashcardProgressDTO {
    private Long id;
    private Integer userId;
    private Long flashcardId;
    private Double easeFactor;
    private Integer interval;
    private Integer repetitions;
    private Date dueDate;
    private Date lastReviewed;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Long getFlashcardId() {
        return flashcardId;
    }
    public void setFlashcardId(Long flashcardId) {
        this.flashcardId = flashcardId;
    }
    public Double getEaseFactor() {
        return easeFactor;
    }
    public void setEaseFactor(Double easeFactor) {
        this.easeFactor = easeFactor;
    }
    public Integer getInterval() {
        return interval;
    }
    public void setInterval(Integer interval) {
        this.interval = interval;
    }
    public Integer getRepetitions() {
        return repetitions;
    }
    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }
    public Date getDueDate() {
        return dueDate;
    }
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    public Date getLastReviewed() {
        return lastReviewed;
    }
    public void setLastReviewed(Date lastReviewed) {
        this.lastReviewed = lastReviewed;
    }
}
