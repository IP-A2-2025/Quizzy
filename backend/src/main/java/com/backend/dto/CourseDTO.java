package com.backend.dto;

import lombok.Data;

@Data
public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private String semestru;
    private Integer professorId;

    public void setId(Long id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setSemestru(String semestru) {
        this.semestru = semestru;
    }
    public void setProfessorId(Integer professorId) {
        this.professorId = professorId;
    }

    public Long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getSemestru() {
        return semestru;
    }
    public Integer getProfessorId() {
        return professorId;
    }
}