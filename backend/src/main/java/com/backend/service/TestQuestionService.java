// TestQuestionService.java
package com.backend.service;

import com.backend.dto.TestQuestionDTO;
import com.backend.mapper.TestQuestionMapper;
import com.backend.model.TestEntity;
import com.backend.model.TestQuestion;
import com.backend.repository.TestQuestionRepository;
import com.backend.repository.TestRepository;
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
public class TestQuestionService {

    private final TestQuestionRepository testQuestionRepository;
    private final TestQuestionMapper testQuestionMapper;
    private final TestRepository testRepository;

    @Autowired
    public TestQuestionService(TestQuestionRepository testQuestionRepository,
                               TestQuestionMapper testQuestionMapper,
                               TestRepository testRepository) {
        this.testQuestionRepository = Objects.requireNonNull(testQuestionRepository, "TestQuestionRepository must not be null");
        this.testQuestionMapper = testQuestionMapper;
        this.testRepository = testRepository;
    }

    @Transactional(readOnly = true)
    public Collection<TestQuestionDTO> getAllQuestions() {
        return testQuestionRepository.findAll().stream()
                .map(testQuestionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TestQuestionDTO getQuestionById(Long id) {
        return Optional.ofNullable(id)
                .filter(i -> i > 0)
                .flatMap(testQuestionRepository::findById)
                .map(testQuestionMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Question not found with id " + id));
    }

    @Transactional(readOnly = true)
    public Collection<TestQuestionDTO> getQuestionsByTestId(Long testId) {
        return Optional.ofNullable(testId)
                .filter(id -> id > 0)
                .map(testQuestionRepository::findByTestId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid test ID"))
                .stream()
                .map(testQuestionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Long countQuestionsByTest(Long testId) {
        return Optional.ofNullable(testId)
                .filter(id -> id > 0)
                .map(testQuestionRepository::countQuestionsByTestId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid test ID"));
    }

    @Transactional(readOnly = true)
    public Float getTotalPointsByTest(Long testId) {
        return testQuestionRepository.getTotalPointsByTestId(testId);
    }

    @Transactional(readOnly = true)
    public List<TestQuestionDTO> getQuestionsByCourse(Long courseId) {
        return testQuestionRepository.findByCourseId(courseId).stream()
                .map(testQuestionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TestQuestionDTO> getQuestionsByProfessor(Integer professorId) {
        return testQuestionRepository.findByProfessorId(professorId).stream()
                .map(testQuestionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TestQuestionDTO> searchQuestionsByText(String keyword) {
        return Optional.ofNullable(keyword)
                .map(testQuestionRepository::findByQuestionTextContaining)
                .orElseThrow(() -> new IllegalArgumentException("Keyword cannot be null"))
                .stream()
                .map(testQuestionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TestQuestionDTO> getQuestionsByPoints(Float points) {
        return testQuestionRepository.findByPointValue(points).stream()
                .map(testQuestionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TestQuestionDTO> getQuestionsByMinPoints(Float minPoints) {
        return testQuestionRepository.findByMinimumPoints(minPoints).stream()
                .map(testQuestionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Long countQuestionsByCourse(Long courseId) {
        return testQuestionRepository.countQuestionsByCourseId(courseId);
    }

    @Transactional(readOnly = true)
    public Long countQuestionsByProfessor(Integer professorId) {
        return testQuestionRepository.countQuestionsByProfessorId(professorId);
    }

    @Transactional(readOnly = true)
    public Long countQuestionsByPoints(Float points) {
        return testQuestionRepository.countQuestionsByPointValue(points);
    }

    @Transactional(readOnly = true)
    public Long countQuestionsByMinPoints(Float minPoints) {
        return testQuestionRepository.countQuestionsByMinimumPoints(minPoints);
    }

    @Transactional(readOnly = true)
    public Float getAveragePointsPerQuestion(Long testId) {
        return testQuestionRepository.getAveragePointsPerQuestion(testId);
    }

    @Transactional(readOnly = true)
    public List<TestQuestionDTO> getQuestionsByTestIdSortedByPoints(Long testId) {
        return testQuestionRepository.findByTestIdOrderByPointsDesc(testId).stream()
                .map(testQuestionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TestQuestionDTO> getQuestionsByTestAndMinPoints(Long testId, Float minPoints) {
        return testQuestionRepository.findByTestIdAndMinPoints(testId, minPoints).stream()
                .map(testQuestionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TestQuestionDTO saveQuestion(TestQuestionDTO testQuestionDTO) {
        return Optional.ofNullable(testQuestionDTO)
                .map(dto -> {
                    // Validate required fields
                    if (dto.getTestId() == null) {
                        throw new IllegalArgumentException("Test ID must not be null");
                    }

                    // Create entity manually to ensure proper mapping
                    TestQuestion entity = new TestQuestion();
                    entity.setQuestionText(dto.getQuestionText());
                    entity.setPointValue(dto.getPointValue());

                    // Set the test relationship
                    TestEntity test = testRepository.findById(dto.getTestId())
                            .orElseThrow(() -> new EntityNotFoundException("Test not found with id " + dto.getTestId()));
                    entity.setTest(test);

                    return entity;
                })
                .map(testQuestionRepository::save)
                .map(testQuestionMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("TestQuestionDTO must not be null"));
    }

    @Transactional
    public TestQuestionDTO createQuestion(TestQuestionDTO testQuestionDTO) {
        return Optional.ofNullable(testQuestionDTO)
                .filter(q -> q.getId() == null)
                .map(this::saveQuestion)
                .orElseThrow(() -> new IllegalArgumentException("New question must not have an ID"));
    }

    @Transactional
    public TestQuestionDTO updateQuestion(Long id, TestQuestionDTO testQuestionDTO) {
        // Validate input
        if (testQuestionDTO == null) {
            throw new IllegalArgumentException("TestQuestionDTO must not be null");
        }

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid question ID");
        }

        // Check if question exists
        TestQuestion existingQuestion = testQuestionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Question not found with id " + id));

        // Update fields
        updateQuestionFields(existingQuestion, testQuestionDTO);

        // Save and return
        return testQuestionMapper.toDTO(testQuestionRepository.save(existingQuestion));
    }

    private void updateQuestionFields(TestQuestion existingQuestion, TestQuestionDTO dto) {
        if (dto.getQuestionText() != null) {
            existingQuestion.setQuestionText(dto.getQuestionText());
        }

        existingQuestion.setPointValue(dto.getPointValue());

        // Update test relationship if provided
        if (dto.getTestId() != null) {
            if (!existingQuestion.getTest().getId().equals(dto.getTestId())) {
                TestEntity test = testRepository.findById(dto.getTestId())
                        .orElseThrow(() -> new EntityNotFoundException("Test not found with id " + dto.getTestId()));
                existingQuestion.setTest(test);
            }
        }
    }

    @Transactional
    public void deleteQuestionById(Long id) {
        Optional.ofNullable(id)
                .filter(i -> i > 0)
                .ifPresentOrElse(
                        i -> {
                            if (!testQuestionRepository.existsById(i)) {
                                throw new EntityNotFoundException("Question not found with id " + i);
                            }
                            testQuestionRepository.deleteById(i);
                        },
                        () -> {
                            throw new IllegalArgumentException("Invalid question ID");
                        }
                );
    }

    @Transactional
    public List<TestQuestionDTO> createMultipleQuestions(List<TestQuestionDTO> questions) {
        return Optional.ofNullable(questions)
                .filter(list -> !list.isEmpty())
                .map(list -> list.stream()
                        .filter(q -> q.getId() == null)
                        .map(this::saveQuestion)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new IllegalArgumentException("Questions list cannot be null or empty"));
    }

    @Transactional
    public List<TestQuestionDTO> saveMultipleQuestions(List<TestQuestionDTO> questions) {
        return Optional.ofNullable(questions)
                .filter(list -> !list.isEmpty())
                .map(list -> list.stream()
                        .map(this::saveQuestion)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new IllegalArgumentException("Questions list cannot be null or empty"));
    }

    @Transactional
    public List<TestQuestionDTO> updateMultipleQuestions(List<TestQuestionDTO> questions) {
        return Optional.ofNullable(questions)
                .filter(list -> !list.isEmpty())
                .map(list -> list.stream()
                        .filter(q -> q.getId() != null)
                        .map(q -> updateQuestion(q.getId(), q))
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new IllegalArgumentException("Questions list cannot be null or empty"));
    }
}