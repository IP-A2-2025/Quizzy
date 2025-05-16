package com.backend.dto;

import lombok.Data;
import java.util.Date;
import java.util.Set;

@Data
public class FlashcardDTO {
    private Long id;
    private String question;
    private int level;
    private Date lastStudiedAt;
    private String questionType;
    private Integer pageIndex;
    private Integer userId;
    private Long materialId;
    private Set<AnswerFCDTO> answers;

    public void setId(Long id) {
        this.id = id;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public void setLastStudiedAt(Date lastStudiedAt) {
        this.lastStudiedAt = lastStudiedAt;
    }
    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }
    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }
    public void setAnswers(Set<AnswerFCDTO> answers) {
        this.answers = answers;
    }
    public Long getId() {
        return id;
    }
    public String getQuestion() {
        return question;
    }
    public int getLevel() {
        return level;
    }
    public Date getLastStudiedAt() {
        return lastStudiedAt;
    }
    public String getQuestionType() {
        return questionType;
    }
    public Integer getPageIndex() {
        return pageIndex;
    }
    public Integer getUserId() {
        return userId;
    }
    public Long getMaterialId() {
        return materialId;
    }
    public Set<AnswerFCDTO> getAnswers() {
        return answers;
    }
}