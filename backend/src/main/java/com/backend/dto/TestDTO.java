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

    public void setId(Long id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public void setProfessorId(Integer professorId) {
        this.professorId = professorId;
    }
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
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
    public Date getDate() {
        return date;
    }
    public Integer getProfessorId() {
        return professorId;
    }
    public Long getCourseId() {
        return courseId;
    }
}