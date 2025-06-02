package com.backend.dto;

import lombok.Data;
import java.util.Date;

@Data
public class GradeDTO {
    private Long testId;
    private Integer userId;
    private float grade;
    private Date submissionDate;

    public void setTestId(Long testId) {
        this.testId = testId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public void setGrade(float grade) {
        this.grade = grade;
    }
    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Long getTestId() {
        return testId;
    }
    public Integer getUserId() {
        return userId;
    }
    public float getGrade() {
        return grade;
    }
    public Date getSubmissionDate() {
        return submissionDate;
    }
}