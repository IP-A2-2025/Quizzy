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
    private Test test;

    @ManyToOne
    @JoinColumn(name = "userid")
    @MapsId("userID")
    @JsonBackReference
    private User user;
}