package com.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "grade")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Grade {

    @EmbeddedId
    private GradeId id;

    @Column(name = "grade", nullable = false)
    private float grade;

    @Column(name = "submissiondate", nullable = false)
    private Date submissionDate;

    @ManyToOne
    @JoinColumn(name = "testid")
    @MapsId("testID")
    @JsonBackReference
    private TestEntity test;

    @ManyToOne
    @JoinColumn(name = "userid")
    @MapsId("userID")
    @JsonBackReference
    private User user;

    public GradeId getId() {
        return id;
    }
    public void setId(GradeId id) {
        this.id = id;
    }
    public float getGrade() {
        return grade;
    }
    public void setGrade(float grade) {
        this.grade = grade;
    }
    public Date getSubmissionDate() {
        return submissionDate;
    }
    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }
    public TestEntity getTest() {
        return test;
    }
    public void setTest(TestEntity test) {
        this.test = test;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}