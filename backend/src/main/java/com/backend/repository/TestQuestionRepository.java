package com.backend.repository;

import com.backend.model.TestQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestQuestionRepository extends JpaRepository<TestQuestion, Long> {

    @Query("SELECT q FROM TestQuestion q WHERE q.test.id = :testId")
    List<TestQuestion> findByTestId(@Param("testId") Long testId);

    @Query("SELECT SUM(q.pointValue) FROM TestQuestion q WHERE q.test.id = :testId")
    Float getTotalPointsByTestId(@Param("testId") Long testId);

    @Query("SELECT q FROM TestQuestion q WHERE q.test.course.id = :courseId")
    List<TestQuestion> findByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT q FROM TestQuestion q WHERE q.test.professor.id = :professorId")
    List<TestQuestion> findByProfessorId(@Param("professorId") Integer professorId);

    @Query("SELECT q FROM TestQuestion q WHERE q.questionText LIKE %:keyword%")
    List<TestQuestion> findByQuestionTextContaining(@Param("keyword") String keyword);

    @Query("SELECT q FROM TestQuestion q WHERE q.pointValue = :points")
    List<TestQuestion> findByPointValue(@Param("points") Float points);

    @Query("SELECT q FROM TestQuestion q WHERE q.pointValue >= :minPoints")
    List<TestQuestion> findByMinimumPoints(@Param("minPoints") Float minPoints);

    @Query("SELECT COUNT(q) FROM TestQuestion q WHERE q.test.id = :testId")
    Long countQuestionsByTestId(@Param("testId") Long testId);

    @Query("SELECT COUNT(q) FROM TestQuestion q WHERE q.test.course.id = :courseId")
    Long countQuestionsByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT COUNT(q) FROM TestQuestion q WHERE q.test.professor.id = :professorId")
    Long countQuestionsByProfessorId(@Param("professorId") Integer professorId);

    @Query("SELECT COUNT(q) FROM TestQuestion q WHERE q.pointValue = :points")
    Long countQuestionsByPointValue(@Param("points") Float points);

    @Query("SELECT COUNT(q) FROM TestQuestion q WHERE q.pointValue >= :minPoints")
    Long countQuestionsByMinimumPoints(@Param("minPoints") Float minPoints);

    @Query("SELECT AVG(q.pointValue) FROM TestQuestion q WHERE q.test.id = :testId")
    Float getAveragePointsPerQuestion(@Param("testId") Long testId);

    @Query("SELECT q FROM TestQuestion q WHERE q.test.id = :testId ORDER BY q.pointValue DESC")
    List<TestQuestion> findByTestIdOrderByPointsDesc(@Param("testId") Long testId);

    @Query("SELECT q FROM TestQuestion q WHERE q.test.id = :testId AND q.pointValue >= :minPoints")
    List<TestQuestion> findByTestIdAndMinPoints(@Param("testId") Long testId, @Param("minPoints") Float minPoints);
}