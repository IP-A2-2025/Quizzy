package com.backend.service;

import com.backend.model.TestAnswer;
import com.backend.repository.TestAnswerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Service
public class TestAnswerService {

    private final TestAnswerRepository testAnswerRepository;

    @Autowired
    public TestAnswerService(TestAnswerRepository testAnswerRepository) {
        this.testAnswerRepository = testAnswerRepository;
    }

    @Transactional(readOnly = true)
    public Collection<TestAnswer> getAllAnswers() {
        return testAnswerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public TestAnswer getAnswerById(Long id) {
        return Optional.ofNullable(id)
                .filter(i -> i > 0)
                .flatMap(testAnswerRepository::findById)
                .orElseThrow(() -> new EntityNotFoundException("Answer not found with id " + id));
    }

    @Transactional(readOnly = true)
    public Collection<TestAnswer> getAnswersByQuestionId(Long questionId) {
        return Optional.ofNullable(questionId)
                .filter(id -> id > 0)
                .map(testAnswerRepository::findByQuestionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question ID"));
    }

    @Transactional(readOnly = true)
    public Collection<TestAnswer> getCorrectAnswersByQuestionId(Long questionId) {
        return Optional.ofNullable(questionId)
                .filter(id -> id > 0)
                .map(testAnswerRepository::findCorrectAnswersByQuestionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question ID"));
    }

    @Transactional(readOnly = true)
    public Collection<TestAnswer> getAnswersByTestId(Long testId) {
        return Optional.ofNullable(testId)
                .filter(id -> id > 0)
                .map(testAnswerRepository::findByTestId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid test ID"));
    }

    @Transactional(readOnly = true)
    public Long countCorrectAnswersByQuestionId(Long questionId) {
        return Optional.ofNullable(questionId)
                .filter(id -> id > 0)
                .map(testAnswerRepository::countCorrectAnswersByQuestionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question ID"));
    }

    @Transactional(readOnly = true)
    public Long countAnswersByTestId(Long testId) {
        return Optional.ofNullable(testId)
                .filter(id -> id > 0)
                .map(testAnswerRepository::countAnswersByTestId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid test ID"));
    }

    @Transactional(readOnly = true)
    public Long countCorrectAnswersByTestId(Long testId) {
        return Optional.ofNullable(testId)
                .filter(id -> id > 0)
                .map(testAnswerRepository::countCorrectAnswersByTestId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid test ID"));
    }

    @Transactional(readOnly = true)
    public Collection<TestAnswer> getIncorrectAnswersByQuestionId(Long questionId) {
        return Optional.ofNullable(questionId)
                .filter(id -> id > 0)
                .map(testAnswerRepository::findIncorrectAnswersByQuestionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question ID"));
    }

    @Transactional(readOnly = true)
    public Collection<TestAnswer> findByOptionTextContaining(String keyword) {
        return Optional.ofNullable(keyword)
                .map(testAnswerRepository::findByOptionTextContaining)
                .orElseThrow(() -> new IllegalArgumentException("Keyword cannot be null"));
    }

    @Transactional(readOnly = true)
    public Collection<TestAnswer> getCorrectAnswersByTestId(Long testId) {
        return Optional.ofNullable(testId)
                .filter(id -> id > 0)
                .map(testAnswerRepository::findCorrectAnswersByTestId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid test ID"));
    }

    @Transactional(readOnly = true)
    public Long countAnswersByQuestionId(Long questionId) {
        return Optional.ofNullable(questionId)
                .filter(id -> id > 0)
                .map(testAnswerRepository::countAnswersByQuestionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question ID"));
    }

    @Transactional
    public TestAnswer createAnswer(TestAnswer answer) {
        return Optional.ofNullable(answer)
                .filter(a -> a.getId() == null)
                .map(testAnswerRepository::save)
                .orElseThrow(() -> new IllegalArgumentException("New answer must not have an ID"));
    }

    @Transactional
    public TestAnswer updateAnswer(Long id, TestAnswer answer) {
        return Optional.ofNullable(id)
                .filter(i -> i > 0)
                .map(i -> Optional.ofNullable(answer)
                        .map(a -> {
                            TestAnswer existingAnswer = testAnswerRepository.findById(i)
                                    .orElseThrow(() -> new EntityNotFoundException("Answer not found with id " + i));
                            return updateAnswerFields(existingAnswer, a);
                        })
                        .orElseThrow(() -> new IllegalArgumentException("Updated answer data must not be null")))
                .orElseThrow(() -> new IllegalArgumentException("Invalid answer ID"));
    }

    @Transactional
    private TestAnswer updateAnswerFields(TestAnswer existingAnswer, TestAnswer answerToUpdate) {
        return Optional.ofNullable(answerToUpdate)
                .map(update -> {
                    Optional.ofNullable(update.getOptionText())
                            .ifPresent(existingAnswer::setOptionText);
                    Optional.ofNullable(update.isCorrect())
                            .ifPresent(existingAnswer::setCorrect);
                    return testAnswerRepository.save(existingAnswer);
                })
                .orElseThrow(() -> new IllegalArgumentException("Answer data to update cannot be null"));
    }

    @Transactional
    public void deleteAnswerById(Long id) {
        Optional.ofNullable(id)
                .filter(i -> i > 0)
                .ifPresentOrElse(
                        i -> {
                            if (!testAnswerRepository.existsById(i)) {
                                throw new EntityNotFoundException("Answer not found with id " + i);
                            }
                            testAnswerRepository.deleteById(i);
                        },
                        () -> {
                            throw new IllegalArgumentException("Invalid answer ID");
                        }
                );
    }
}

