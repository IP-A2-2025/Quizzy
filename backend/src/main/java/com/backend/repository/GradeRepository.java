package com.backend.repository;

import com.backend.model.Grade;
import com.backend.model.GradeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, GradeId> {

    List<Grade> findByUserId(Integer userId);

    List<Grade> findByTestId(Long testId);

    @Query("SELECT g FROM Grade g WHERE g.test.course.id = :courseId AND g.user.id = :userId")
    List<Grade> findByCourseIdAndUserId(@Param("courseId") Long courseId, @Param("userId") Integer userId);

    // Average grade for a test
    @Query("SELECT AVG(g.grade) FROM Grade g WHERE g.test.id = :testId")
    Optional<Double> findAverageGradeByTestId(@Param("testId") Long testId);

    // Average grade for a course and user
    @Query("SELECT AVG(g.grade) FROM Grade g WHERE g.test.course.id = :courseId AND g.user.id = :userId")
    Optional<Double> findAverageGradeByCourseIdAndUserId(@Param("courseId") Long courseId, @Param("userId") Integer userId);

    // Grades submitted in a date range for a user
    @Query("SELECT g FROM Grade g WHERE g.submissionDate BETWEEN :startDate AND :endDate AND g.user.id = :userId")
    List<Grade> findBySubmissionDateBetweenAndUserId(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("userId") Integer userId
    );
}
