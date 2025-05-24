package com.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "flashcardprogress")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlashcardProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flashcardprogressid")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "flashcardid", nullable = false)
    @JsonBackReference
    private Flashcard flashcard;

    @Column(name = "easefactor", nullable = false)
    private Double easeFactor;

    @Column(name = "interval", nullable = false)
    private Integer interval;

    @Column(name = "repetitions", nullable = false)
    private Integer repetitions;

    @Column(name = "duedate", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dueDate;

    @Column(name = "lastreviewed", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastReviewed;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Flashcard getFlashcard() {
        return flashcard;
    }
    public void setFlashcard(Flashcard flashcard) {
        this.flashcard = flashcard;
    }
    public double getEaseFactor() {
        return easeFactor;
    }
    public void setEaseFactor(double easeFactor) {
        this.easeFactor = easeFactor;
    }
    public int getInterval() {
        return interval;
    }
    public void setInterval(int interval) {
        this.interval = interval;
    }
    public int getRepetitions() {
        return repetitions;
    }
    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }
    public Date getDueDate() {
        return dueDate;
    }
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    public Date getLastReviewed() {
        return lastReviewed;
    }
    public void setLastReviewed(Date lastReviewed) {
        this.lastReviewed = lastReviewed;
    }
}
