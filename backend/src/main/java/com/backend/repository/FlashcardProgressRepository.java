package com.backend.repository;

import com.backend.model.FlashcardProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface FlashcardProgressRepository extends JpaRepository<FlashcardProgress, Long> {

    @Query("SELECT fp FROM FlashcardProgress fp WHERE fp.user.id = :userId")
    List<FlashcardProgress> findByUserId(@Param("userId") Integer userId);

    @Query("SELECT fp FROM FlashcardProgress fp WHERE fp.flashcard.id = :flashcardId")
    List<FlashcardProgress> findByFlashcardId(@Param("flashcardId") Long flashcardId);

    @Query("SELECT fp FROM FlashcardProgress fp WHERE fp.dueDate <= :date AND fp.user.id = :userId")
    List<FlashcardProgress> findDueProgress(@Param("date") Date date, @Param("userId") Integer userId);

    @Query("SELECT fp FROM FlashcardProgress fp WHERE fp.user.id = :userId AND fp.flashcard.material.id = :materialId")
    List<FlashcardProgress> findByUserIdAndMaterialId(@Param("userId") Integer userId, @Param("materialId") Long materialId);

    @Query("SELECT fp FROM FlashcardProgress fp WHERE fp.user.id = :userId AND fp.flashcard.level = :level")
    List<FlashcardProgress> findByUserIdAndLevel(@Param("userId") Integer userId, @Param("level") int level);

    @Query("SELECT fp FROM FlashcardProgress fp WHERE fp.user.id = :userId AND fp.flashcard.pageIndex = :pageIndex")
    List<FlashcardProgress> findByUserIdAndPageIndex(@Param("userId") Integer userId, @Param("pageIndex") Integer pageIndex);

    @Query("SELECT fp FROM FlashcardProgress fp WHERE fp.flashcard.material.course.id = :courseId AND fp.user.id = :userId")
    List<FlashcardProgress> findByUserIdAndCourseId(@Param("userId") Integer userId, @Param("courseId") Long courseId);

    @Query("SELECT fp FROM FlashcardProgress fp WHERE fp.user.id = :userId AND fp.flashcard.questionType = :type")
    List<FlashcardProgress> findByUserIdAndQuestionType(@Param("userId") Integer userId, @Param("type") String type);

    @Query("SELECT fp FROM FlashcardProgress fp WHERE fp.flashcard.id = :flashcardId AND fp.user.id = :userId")
    FlashcardProgress findByFlashcardIdAndUserId(@Param("flashcardId") Long flashcardId, @Param("userId") Integer userId);
}
