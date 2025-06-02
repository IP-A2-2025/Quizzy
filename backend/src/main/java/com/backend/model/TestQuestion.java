package com.backend.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    @JsonBackReference
    private TestEntity test;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getQuestionText() {
        return questionText;
    }
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
    public float getPointValue() {
        return pointValue;
    }
    public void setPointValue(float pointValue) {
        this.pointValue = pointValue;
    }
    public TestEntity getTest() {
        return test;
    }
    public void setTest(TestEntity test) {
        this.test = test;
    }

    @OneToMany(mappedBy = "testQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<TestAnswer> answers = new ArrayList<>();

}
