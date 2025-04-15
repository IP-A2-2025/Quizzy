package com.backend.dto;

import lombok.Data;
import java.sql.Date;

@Data
public class StreakDTO {
    private Long id;
    private int currentStreak;
    private Date lastCompletedDate;
    private Integer userId;
}