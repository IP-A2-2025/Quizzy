package com.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "courseid")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "semestru")
    private String semestru;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    @JsonBackReference
    private User professor;

    @OneToMany(mappedBy = "course")
    @JsonBackReference
    private Set<Material> materials;

    @OneToMany(mappedBy = "course")
    @JsonBackReference
    private Set<Enrollment> enrollments;

    @OneToMany(mappedBy = "course")
    @JsonBackReference
    private Set<Test> tests;

    @OneToMany(mappedBy = "course")
    @JsonBackReference
    private Set<FlashcardSession> flashcardSessions;
}