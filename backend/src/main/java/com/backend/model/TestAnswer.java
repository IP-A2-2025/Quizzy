package com.backend.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "testanswer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answerid")
    private Long id;

    @Column(name = "optiontext", nullable = false)
    private String optionText;

    @Column(name = "iscorrect", nullable = false)
    private boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "questionid", nullable = false)
    @JsonBackReference
    private TestQuestion testQuestion;
}
