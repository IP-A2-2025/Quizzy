package com.backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private Integer id;

    @Column(name = "firstname", nullable = false)
    private String firstName;

    @Column(name = "lastname", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "password", nullable = false)
    private String password;

    public String getPassword() {
        return password;
    }
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<Enrollment> enrollments;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<Flashcard> flashcards;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<Grade> grades;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<Streak> streaks;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<FlashcardSession> flashcardSessions;

    @OneToMany(mappedBy = "professor")
    @JsonManagedReference
    private Set<Course> courses;

    @OneToMany(mappedBy = "professor")
    @JsonManagedReference
    private Set<Test> tests;
}