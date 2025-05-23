package com.backend.service;

import com.backend.dto.TestAnswerDTO;
import com.backend.mapper.TestAnswerMapper;
import com.backend.model.TestAnswer;
import com.backend.model.TestEntity;
import com.backend.model.TestQuestion;
import com.backend.repository.TestAnswerRepository;
import com.backend.repository.TestQuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TestAnswerService {

    private final TestAnswerRepository testAnswerRepository;
    private final TestQuestionRepository testQuestionRepository;

    @Autowired
    public TestAnswerService(TestAnswerRepository testAnswerRepository, TestQuestionRepository testQuestionRepository) {
        this.testAnswerRepository = testAnswerRepository;
        this.testQuestionRepository = testQuestionRepository;
    }

    @Transactional
    public TestAnswer saveAnswer(TestAnswer answer) {
        return Optional.ofNullable(answer)
                .map(testAnswerRepository::save)
                .orElseThrow(() -> new IllegalArgumentException("Answer must not be null"));
    }

    @Transactional
    public Collection<TestAnswer> createAnswers(Collection<TestAnswerDTO> answerDTOs){
        TestAnswerMapper mapper = new TestAnswerMapper(testQuestionRepository);
        Collection<TestAnswer> createdAnswers = answerDTOs.stream()
                .map(dto -> {
                    TestAnswer answer = mapper.toEntity(dto);

                        Objects.requireNonNull(answer, "Answer must not be null");

                        if (answer.getId() != null) {
                            throw new IllegalArgumentException("New answer must have no ID");
                        }

                        if(answer.getTestQuestion() == null || answer.getTestQuestion().getId() == null) {
                            throw new IllegalArgumentException("Test answer must have associated a test question");
                        }

                        if(answer.getOptionText() == null || answer.getOptionText().isEmpty()) {
                            throw new IllegalArgumentException("Option text must exist");
                        }

                        saveAnswer(answer);
                    return new TestAnswer(
                            answer.getId(),
                            "Answer added successfully",
                            dto.isCorrect(),
                            null
                    );
                })
                .collect(Collectors.toList());
        return createdAnswers;
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
    public TestAnswer updateAnswer(Long id, TestAnswerDTO answerDTO) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid answer ID");
        }
        if (answerDTO == null) {
            throw new IllegalArgumentException("Updated answer data must not be null");
        }

        TestAnswer existingAnswer = testAnswerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Answer not found with id " + id));
        
        if(answerDTO.getOptionText() != null && !answerDTO.getOptionText().isEmpty()) {
            existingAnswer.setOptionText(answerDTO.getOptionText());
        }
        if (answerDTO.getQuestionId() != null && testQuestionRepository.existsById(answerDTO.getQuestionId())) {
            existingAnswer.setTestQuestion(testQuestionRepository.getReferenceById(answerDTO.getQuestionId()));
        }
        existingAnswer.setCorrect(answerDTO.isCorrect());
        return saveAnswer(existingAnswer);
    }

    @Transactional
    public String deleteAnswerById(Long id) {
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
        return "Answer deleted successfully";
    }

    @Transactional
    public int deleteMultipleAnswers(Collection<Long> answerIds) {
        if (answerIds == null || answerIds.isEmpty()) {
            throw new IllegalArgumentException("Answer IDs collection must not be null or empty");
        }

        int deletedCount = 0;
        for (Long answerId : answerIds) {
            try {
                deleteAnswerById(answerId);
                deletedCount++;
            } catch (EntityNotFoundException e) {
                // Log the error but continue with other deletions
                System.err.println("Error deleting answer: " + e.getMessage());
            }
        }

        return deletedCount;
    }

    @Transactional
    public int deleteAllAnswersForQuestion(Long questionId) {
        Optional.ofNullable(questionId)
                .filter(id -> id > 0)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question ID"));

        List<TestAnswer> answers = testAnswerRepository.findByQuestionId(questionId);

        if (answers.isEmpty()) {
            return 0;
        }

        Collection<Long> answerIds = answers.stream()
                .map(TestAnswer::getId)
                .toList();

        return deleteMultipleAnswers(answerIds);
    }

    @Transactional
    public int deleteAllAnswersForTest(Long testId) {
        Optional.ofNullable(testId)
                .filter(id -> id > 0)
                .orElseThrow(() -> new IllegalArgumentException("Invalid test ID"));

        List<TestAnswer> answers = testAnswerRepository.findByTestId(testId);

        if (answers.isEmpty()) {
            return 0;
        }

        Collection<Long> answerIds = answers.stream()
                .map(TestAnswer::getId)
                .toList();

        return deleteMultipleAnswers(answerIds);
    }
}

