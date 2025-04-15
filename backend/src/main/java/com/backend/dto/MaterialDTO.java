package com.backend.dto;

import lombok.Data;

@Data
public class MaterialDTO {
    private Long id;
    private String name;
    private String path;
    private Long courseId;
}