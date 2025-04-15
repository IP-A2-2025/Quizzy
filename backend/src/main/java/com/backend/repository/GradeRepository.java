package com.backend.repository;

import com.backend.model.Grade;
import com.backend.model.GradeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, GradeId> {

    @Query("SELECT g FROM Grade g WHERE g.user.id = :userId")
    List<Grade> findByUserId(@Param("userId") Integer userId);

    @Query("SELECT g FROM Grade g WHERE g.test.id = :testId")
    List<Grade> findByTestId(@Param("testId") Long testId);

    @Query("SELECT AVG(g.grade) FROM Grade g WHERE g.test.id = :testId")
    Float getAverageGradeByTestId(@Param("testId") Long testId);

    @Query("SELECT g FROM Grade g WHERE g.submissionDate >= :startDate AND g.submissionDate <= :endDate AND g.user.id = :userId")
    List<Grade> findByDateRangeAndUserId(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("userId") Integer userId);

    @Query("SELECT g FROM Grade g WHERE g.test.course.id = :courseId AND g.user.id = :userId")
    List<Grade> findByCourseIdAndUserId(@Param("courseId") Long courseId, @Param("userId") Integer userId);

    @Query("SELECT AVG(g.grade) FROM Grade g WHERE g.test.course.id = :courseId AND g.user.id = :userId")
    Float getAverageGradeByCourseIdAndUserId(@Param("courseId") Long courseId, @Param("userId") Integer userId);
}