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
}