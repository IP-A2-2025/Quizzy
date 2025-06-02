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
    private Integer level;

    @Column(name = "laststudiedat")
    private Date lastStudiedAt;

    @Column(name = "questiontype")
    private String questionType;
    
    @Column(name = "pageindex")
    private Integer pageIndex;

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

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public Integer getLevel() {
        return level;
    }
    public void setLevel(Integer level) {
        this.level = level;
    }

    public Date getLastStudiedAt() {
        return lastStudiedAt;
    }
    public void setLastStudiedAt(Date lastStudiedAt) {
        this.lastStudiedAt = lastStudiedAt;
    }
    public String getQuestionType() {
        return questionType;
    }
    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }
    public Integer getPageIndex() {
        return pageIndex;
    }
    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Material getMaterial() {
        return material;
    }
    public void setMaterial(Material material) {
        this.material = material;
    }

    public Set<AnswerFC> getAnswers() {
        return answers;
    }
    public void setAnswers(Set<AnswerFC> answers) {
        this.answers = answers;
    }
}