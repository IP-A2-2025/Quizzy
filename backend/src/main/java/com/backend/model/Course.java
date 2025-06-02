package com.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
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
    private Set<TestEntity> tests;

    @OneToMany(mappedBy = "course")
    @JsonBackReference
    private Set<FlashcardSession> flashcardSessions;

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
    public User getProfessor() {
        return professor;
    }
    public Set<Material> getMaterials() {
        return materials;
    }
    public Set<Enrollment> getEnrollments() {
        return enrollments;
    }
    public Set<TestEntity> getTests() {
        return tests;
    }
    public Set<FlashcardSession> getFlashcardSessions() {
        return flashcardSessions;
    }
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
    public void setProfessor(User professor) {
        this.professor = professor;
    }
}