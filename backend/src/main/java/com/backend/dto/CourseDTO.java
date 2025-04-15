package com.backend.dto;

import lombok.Data;

@Data
public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private String semestru;
    private Integer professorId;
}