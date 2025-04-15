package com.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "flashcardsession")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlashcardSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sessionid")
    private Long id;

    @Column(name = "timestamp", nullable = false)
    private Date timestamp;

    @Column(name = "flashcardcount", nullable = false)
    private int flashcardCount;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "courseid", nullable = false)
    @JsonBackReference
    private Course course;
}

