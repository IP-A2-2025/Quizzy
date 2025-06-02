package com.backend.dto;

import lombok.Data;

@Data
public class MaterialDTO {
    private Long id;
    private String name;
    private String path;
    private Long courseId;

    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getPath() {
        return path;
    }
    public Long getCourseId() {
        return courseId;
    }
}