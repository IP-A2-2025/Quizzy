package com.backend.dto;

import lombok.Data;
import java.util.Date;
import java.util.Set;

@Data
public class FlashcardDTO {
    private Long id;
    private String question;
    private int level;
    private Date lastStudiedAt;
    private String questionType;
    private Integer userId;
    private Long materialId;
    private Set<AnswerFCDTO> answers;
}