package com.backend.repository;

import com.backend.model.AnswerFC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnswerFCRepository extends JpaRepository<AnswerFC, Long> {

    @Query("SELECT a FROM AnswerFC a WHERE a.flashcard.id = :flashcardId")
    List<AnswerFC> findByFlashcardId(@Param("flashcardId") Long flashcardId);

    @Query("SELECT a FROM AnswerFC a WHERE a.isCorrect = true AND a.flashcard.id = :flashcardId")
    List<AnswerFC> findCorrectAnswersByFlashcardId(@Param("flashcardId") Long flashcardId);
}