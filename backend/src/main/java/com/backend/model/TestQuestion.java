package com.backend.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "testquestion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "questionid")
    private Long id;

    @Column(name = "questiontext", nullable = false)
    private String questionText;

    @Column(name = "pointvalue", nullable = false)
    private float pointValue;

    @ManyToOne
    @JoinColumn(name = "testid", nullable = false)
    @JsonManagedReference
    private Test test;
}
