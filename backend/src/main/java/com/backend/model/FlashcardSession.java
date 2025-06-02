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

    public Long getId() {
        return id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getFlashcardCount() {
        return flashcardCount;
    }

    public User getUser() {
        return user;
    }
    public Course getCourse() {
        return course;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setFlashcardCount(int flashcardCount) {
        this.flashcardCount = flashcardCount;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setCourse(Course course) {
        this.course = course;
    }
}

