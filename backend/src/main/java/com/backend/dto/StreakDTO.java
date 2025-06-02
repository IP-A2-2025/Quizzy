package com.backend.dto;

import lombok.Data;
import java.sql.Date;

@Data
public class StreakDTO {
    private Long id;
    private int currentStreak;
    private Date lastCompletedDate;
    private Integer userId;

    public void setId(Long id) {
        this.id = id;
    }
    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }
    public void setLastCompletedDate(Date lastCompletedDate) {
        this.lastCompletedDate = lastCompletedDate;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Long getId() {
        return id;
    }
    public int getCurrentStreak() {
        return currentStreak;
    }
    public Date getLastCompletedDate() {
        return lastCompletedDate;
    }
    public Integer getUserId() {
        return userId;
    }
}