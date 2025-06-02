package com.backend.dto;

import lombok.Data;
import java.util.Date;

@Data
public class FlashcardSessionDTO {
    private Long id;
    private Date timestamp;
    private int flashcardCount;
    private Integer userId;
    private Long courseId;

    public void setId(Long id) {
        this.id = id;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    public void setFlashcardCount(int flashcardCount) {
        this.flashcardCount = flashcardCount;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getId() {
        return id;
    }
    public Date getTimestamp() {
        return timestamp;
    }
    public int getFlashcardCount() {
        return flashcardCount;
    }
    public Integer getUserId() {
        return userId;
    }
    public Long getCourseId() {
        return courseId;
    }
}