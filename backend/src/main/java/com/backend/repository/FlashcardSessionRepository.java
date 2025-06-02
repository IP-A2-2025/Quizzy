package com.backend.repository;

import com.backend.model.FlashcardSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface FlashcardSessionRepository extends JpaRepository<FlashcardSession, Long> {

    @Query("SELECT s FROM FlashcardSession s WHERE s.user.id = :userId")
    List<FlashcardSession> findByUserId(@Param("userId") Integer userId);

    @Query("SELECT s FROM FlashcardSession s WHERE s.course.id = :courseId")
    List<FlashcardSession> findByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT s FROM FlashcardSession s WHERE s.timestamp >= :startDate AND s.timestamp <= :endDate AND s.user.id = :userId")
    List<FlashcardSession> findByDateRangeAndUserId(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("userId") Integer userId);

    @Query("SELECT SUM(s.flashcardCount) FROM FlashcardSession s WHERE s.user.id = :userId AND s.timestamp >= :startDate")
    Integer getTotalFlashcardsStudiedSince(@Param("userId") Integer userId, @Param("startDate") Date startDate);

    @Query("SELECT s FROM FlashcardSession s WHERE s.user.id = :userId AND s.course.id = :courseId")
    List<FlashcardSession> findByUserIdAndCourseId(@Param("userId") Integer userId, @Param("courseId") Long courseId);
}