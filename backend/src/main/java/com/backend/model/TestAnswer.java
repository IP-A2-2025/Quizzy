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

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getOptionText() {
        return optionText;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
    public void setCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }
    public TestQuestion getTestQuestion() {
        return testQuestion;
    }
    public void setTestQuestion(TestQuestion testQuestion) {
        this.testQuestion = testQuestion;
    }
}
