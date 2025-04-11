package com.backend.dto;

import lombok.Data;
import java.util.Date;

@Data
public class GradeDTO {
    private Long testId;
    private Integer userId;
    private float grade;
    private Date submissionDate;
}