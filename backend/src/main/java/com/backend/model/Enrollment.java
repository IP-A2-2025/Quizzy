package com.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "enrollment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {

    @EmbeddedId
    private EnrollmentId id;

    @ManyToOne
    @MapsId("userID")
    @JoinColumn(name = "userid")
    @JsonBackReference
    private User user;

    @ManyToOne
    @MapsId("courseID")
    @JoinColumn(name = "courseid")
    @JsonBackReference
    private Course course;

    @Column(name = "enrollmentdate", nullable = false)
    private Date enrollmentDate;

    @Column(name = "grade")
    private String grade;
}