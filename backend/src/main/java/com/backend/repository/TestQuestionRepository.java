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
}