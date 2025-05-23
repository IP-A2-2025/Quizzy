package com.backend.service;

import com.backend.dto.TestAnswerDTO;
import com.backend.mapper.TestAnswerMapper;
import com.backend.model.TestAnswer;
import com.backend.model.TestQuestion;
import com.backend.repository.TestAnswerRepository;
import com.backend.repository.TestQuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TestAnswerService {

    private final TestAnswerRepository testAnswerRepository;
    private final TestAnswerMapper testAnswerMapper;
    private final TestQuestionRepository testQuestionRepository;

    @Autowired
    public TestAnswerService(TestAnswerRepository testAnswerRepository, TestAnswerMapper testAnswerMapper, TestQuestionRepository testQuestionRepository) {
        this.testAnswerRepository = Objects.requireNonNull(testAnswerRepository, "TestAnswerRepository must not be null");
        this.testAnswerMapper = testAnswerMapper;
        this.testQuestionRepository = testQuestionRepository;
    }

    @Transactional(readOnly = true)
    public Collection<TestAnswerDTO> getAllAnswers() {
        return testAnswerRepository.findAll().stream()
                .map(testAnswerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TestAnswerDTO getAnswerById(Long id) {
        return Optional.ofNullable(id)
                .filter(i -> i > 0)
                .flatMap(testAnswerRepository::findById)
                .map(testAnswerMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Answer not found with id " + id));
    }

    @Transactional(readOnly = true)
    public Collection<TestAnswerDTO> getAnswersByQuestionId(Long questionId) {
        return Optional.ofNullable(questionId)
                .filter(id -> id > 0)
                .map(testAnswerRepository::findByQuestionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question ID"))
                .stream()
                .map(testAnswerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Collection<TestAnswerDTO> getCorrectAnswersByQuestionId(Long questionId) {
        return Optional.ofNullable(questionId)
                .filter(id -> id > 0)
                .map(testAnswerRepository::findCorrectAnswersByQuestionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question ID"))
                .stream()
                .map(testAnswerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Collection<TestAnswerDTO> getAnswersByTestId(Long testId) {
        return Optional.ofNullable(testId)
                .filter(id -> id > 0)
                .map(testAnswerRepository::findByTestId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid test ID"))
                .stream()
                .map(testAnswerMapper::toDTO)
                .collect(Collectors.toList());
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
    public Collection<TestAnswerDTO> getIncorrectAnswersByQuestionId(Long questionId) {
        return Optional.ofNullable(questionId)
                .filter(id -> id > 0)
                .map(testAnswerRepository::findIncorrectAnswersByQuestionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question ID"))
                .stream()
                .map(testAnswerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Collection<TestAnswerDTO> findByOptionTextContaining(String keyword) {
        return Optional.ofNullable(keyword)
                .map(testAnswerRepository::findByOptionTextContaining)
                .orElseThrow(() -> new IllegalArgumentException("Keyword cannot be null"))
                .stream()
                .map(testAnswerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Collection<TestAnswerDTO> getCorrectAnswersByTestId(Long testId) {
        return Optional.ofNullable(testId)
                .filter(id -> id > 0)
                .map(testAnswerRepository::findCorrectAnswersByTestId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid test ID"))
                .stream()
                .map(testAnswerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Long countAnswersByQuestionId(Long questionId) {
        return Optional.ofNullable(questionId)
                .filter(id -> id > 0)
                .map(testAnswerRepository::countAnswersByQuestionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question ID"));
    }

    @Transactional
    public TestAnswerDTO saveAnswer(TestAnswerDTO testAnswerDTO) {
        return Optional.ofNullable(testAnswerDTO)
                .map(dto -> {
                    // Validate required fields
                    if (dto.getQuestionId() == null) {
                        throw new IllegalArgumentException("Question ID must not be null");
                    }

                    // Create entity manually to ensure proper mapping
                    TestAnswer entity = new TestAnswer();
                    entity.setOptionText(dto.getOptionText());
                    entity.setCorrect(dto.isCorrect());

                    // Set the question relationship
                    TestQuestion question = testQuestionRepository.findById(dto.getQuestionId())
                            .orElseThrow(() -> new EntityNotFoundException("Question not found with id " + dto.getQuestionId()));
                    entity.setTestQuestion(question);

                    return entity;
                })
                .map(testAnswerRepository::save)
                .map(testAnswerMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("TestAnswerDTO must not be null"));
    }

    @Transactional
    public TestAnswerDTO createAnswer(TestAnswerDTO testAnswerDTO) {
        return Optional.ofNullable(testAnswerDTO)
                .filter(a -> a.getId() == null)
                .map(this::saveAnswer)
                .orElseThrow(() -> new IllegalArgumentException("New answer must not have an ID"));
    }

    @Transactional
    public TestAnswerDTO updateAnswer(Long id, TestAnswerDTO testAnswerDTO) {
        // Validate input
        if (testAnswerDTO == null) {
            throw new IllegalArgumentException("TestAnswerDTO must not be null");
        }

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid answer ID");
        }

        // Check if answer exists
        TestAnswer existingAnswer = testAnswerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Answer not found with id " + id));

        // Update fields
        updateAnswerFields(existingAnswer, testAnswerDTO);

        // Save and return
        return testAnswerMapper.toDTO(testAnswerRepository.save(existingAnswer));
    }

    private void updateAnswerFields(TestAnswer existingAnswer, TestAnswerDTO dto) {
        if (dto.getOptionText() != null) {
            existingAnswer.setOptionText(dto.getOptionText());
        }

        existingAnswer.setCorrect(dto.isCorrect());

        // Update question relationship if provided
        if (dto.getQuestionId() != null) {
            if (!existingAnswer.getTestQuestion().getId().equals(dto.getQuestionId())) {
                TestQuestion question = testQuestionRepository.findById(dto.getQuestionId())
                        .orElseThrow(() -> new EntityNotFoundException("Question not found with id " + dto.getQuestionId()));
                existingAnswer.setTestQuestion(question);
            }
        }
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

    // Add these methods to your TestAnswerService class

    @Transactional
    public Collection<TestAnswerDTO> saveAnswers(Collection<TestAnswerDTO> testAnswerDTOs) {
        if (testAnswerDTOs == null || testAnswerDTOs.isEmpty()) {
            throw new IllegalArgumentException("Answer collection must not be null or empty");
        }

        return testAnswerDTOs.stream()
                .map(this::saveAnswer)
                .collect(Collectors.toList());
    }

    @Transactional
    public Collection<TestAnswerDTO> createAnswers(Collection<TestAnswerDTO> testAnswerDTOs) {
        if (testAnswerDTOs == null || testAnswerDTOs.isEmpty()) {
            throw new IllegalArgumentException("Answer collection must not be null or empty");
        }

        // Validate that all answers are new (no IDs)
        boolean hasExistingIds = testAnswerDTOs.stream()
                .anyMatch(dto -> dto.getId() != null);

        if (hasExistingIds) {
            throw new IllegalArgumentException("New answers must not have IDs");
        }

        return testAnswerDTOs.stream()
                .map(this::saveAnswer)
                .collect(Collectors.toList());
    }
}