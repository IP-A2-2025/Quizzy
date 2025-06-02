package com.backend.repository;

import com.backend.model.TestAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestAnswerRepository extends JpaRepository<TestAnswer, Long> {

    @Query("SELECT a FROM TestAnswer a WHERE a.testQuestion.id = :questionId")
    List<TestAnswer> findByQuestionId(@Param("questionId") Long questionId);

    @Query("SELECT a FROM TestAnswer a WHERE a.isCorrect = true AND a.testQuestion.id = :questionId")
    List<TestAnswer> findCorrectAnswersByQuestionId(@Param("questionId") Long questionId);

    @Query("SELECT a FROM TestAnswer a WHERE a.testQuestion.test.id = :testId")
    List<TestAnswer> findByTestId(@Param("testId") Long testId);

    @Query("SELECT COUNT(a) FROM TestAnswer a WHERE a.isCorrect = true AND a.testQuestion.id = :questionId")
    Long countCorrectAnswersByQuestionId(@Param("questionId") Long questionId);

    @Query("SELECT COUNT(a) FROM TestAnswer a WHERE a.testQuestion.test.id = :testId")
    Long countAnswersByTestId(@Param("testId") Long testId);

    @Query("SELECT COUNT(a) FROM TestAnswer a WHERE a.isCorrect = true AND a.testQuestion.test.id = :testId")
    Long countCorrectAnswersByTestId(@Param("testId") Long testId);

    @Query("SELECT a FROM TestAnswer a WHERE a.isCorrect = false AND a.testQuestion.id = :questionId")
    List<TestAnswer> findIncorrectAnswersByQuestionId(@Param("questionId") Long questionId);

    @Query("SELECT a FROM TestAnswer a WHERE LOWER(a.optionText) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<TestAnswer> findByOptionTextContaining(@Param("keyword") String keyword);

    @Query("SELECT a FROM TestAnswer a WHERE a.testQuestion.test.id = :testId AND a.isCorrect = true")
    List<TestAnswer> findCorrectAnswersByTestId(@Param("testId") Long testId);

    @Query("SELECT COUNT(a) FROM TestAnswer a WHERE a.testQuestion.id = :questionId")
    Long countAnswersByQuestionId(@Param("questionId") Long questionId);
}