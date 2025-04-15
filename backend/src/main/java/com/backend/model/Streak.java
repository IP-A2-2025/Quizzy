package com.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "streak")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Streak {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "streakid")
    private Long id;

    @Column(name = "currentstreak", nullable = false)
    private int currentStreak;

    @Column(name = "lastcompleteddate", nullable = false)
    private Date lastCompletedDate;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    @JsonBackReference
    private User user;
}