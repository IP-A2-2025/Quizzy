package com.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "flashcard")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Flashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flashcardid")
    private Long id;

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "level")
    private int level;

    @Column(name = "laststudiedat")
    private Date lastStudiedAt;

    @Column(name = "questiontype")
    private String questionType;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "materialid")
    @JsonBackReference
    private Material material;

    @OneToMany(mappedBy = "flashcard", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<AnswerFC> answers;
}