package com.backend.dto;

import lombok.Data;
import java.util.Date;

@Data
public class TestDTO {
    private Long id;
    private String title;
    private String description;
    private Date date;
    private Integer professorId;
    private Long courseId;
}