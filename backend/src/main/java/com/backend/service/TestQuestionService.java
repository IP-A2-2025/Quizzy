package com.backend.service;

import com.backend.dto.TestQuestionDTO;
import com.backend.mapper.TestQuestionMapper;
import com.backend.model.TestEntity;
import com.backend.model.TestQuestion;
import com.backend.repository.CourseRepository;
import com.backend.repository.TestQuestionRepository;
import com.backend.repository.TestRepository;
import com.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TestQuestionService {

    private final TestQuestionRepository testQuestionRepository;
    private final TestRepository testRepository;

    @Autowired
    public TestQuestionService(TestQuestionRepository testQuestionRepository, TestRepository testRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.testRepository = Objects.requireNonNull(testRepository, "TestRepository must not be null");
        this.testQuestionRepository = Objects.requireNonNull(testQuestionRepository, "TestQuestionRepository must not be null");
    }

    @Transactional
    public TestQuestion saveQuestion(TestQuestion question) {
        return Optional.ofNullable(question)
                .map(testQuestionRepository::save)
                .orElseThrow(() -> new IllegalArgumentException("Question must not be null"));
    }

    @Transactional
    public Long createQuestion(TestQuestionDTO questionDTO) {
        //spaghetti code x_x

        TestQuestionMapper mapper = new TestQuestionMapper(testRepository);
        TestQuestion question = mapper.toEntity(questionDTO);

        Objects.requireNonNull(question, "Question must not be null");

        if (question.getId() != null) {
            throw new IllegalArgumentException("New question must have no ID");
        }

        if (question.getQuestionText() == null || question.getQuestionText().trim().isEmpty()) {
            throw new IllegalArgumentException("Question text cannot be empty");
        }

        if (question.getPointValue() <= 0) {
            throw new IllegalArgumentException("Point value must be positive");
        }

        TestEntity test = question.getTest();
        if (test == null) {
            throw new NullPointerException("Question must be associated with a test");
        }

        if (test.getId() == null) {
            throw new IllegalStateException("Foreign key for test is null");
        }

        if (test.getProfessor() == null) {
            throw new IllegalStateException("Test has no assigned professor");
        }

        String role = test.getProfessor().getRole();
        if (!("PROFESOR".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role))) {
            throw new IllegalArgumentException("Only professors or admins can create questions");
        }

        saveQuestion(question);
        return question.getId();
    }

    @Transactional(readOnly = true)
    public Collection<TestQuestion> getAllQuestions() {
        return testQuestionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public TestQuestion getQuestionById(Long id) {
        return Optional.ofNullable(id)
                .filter(i -> i > 0)
                .flatMap(testQuestionRepository::findById)
                .orElseThrow(() -> new EntityNotFoundException("Question not found with id " + id));
    }

    @Transactional(readOnly = true)
    public Collection<TestQuestion> getQuestionsByTestId(Long testId) {
        return Optional.ofNullable(testId)
                .filter(id -> id > 0)
                .map(testQuestionRepository::findByTestId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid test ID"));
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
    public List<TestQuestion> getQuestionsByCourse(Long courseId) {
        return testQuestionRepository.findByCourseId(courseId);
    }

    @Transactional(readOnly = true)
    public List<TestQuestion> getQuestionsByProfessor(Integer professorId) {
        return testQuestionRepository.findByProfessorId(professorId);
    }

    @Transactional(readOnly = true)
    public List<TestQuestion> searchQuestionsByText(String keyword) {
        return testQuestionRepository.findByQuestionTextContaining(keyword);
    }

    @Transactional(readOnly = true)
    public List<TestQuestion> getQuestionsByPoints(Float points) {
        return testQuestionRepository.findByPointValue(points);
    }

    @Transactional(readOnly = true)
    public List<TestQuestion> getQuestionsByMinPoints(Float minPoints) {
        return testQuestionRepository.findByMinimumPoints(minPoints);
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
    public List<TestQuestion> getQuestionsByTestIdSortedByPoints(Long testId) {
        return testQuestionRepository.findByTestIdOrderByPointsDesc(testId);
    }

    @Transactional(readOnly = true)
    public List<TestQuestion> getQuestionsByTestAndMinPoints(Long testId, Float minPoints) {
        return testQuestionRepository.findByTestIdAndMinPoints(testId, minPoints);
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
    public TestQuestion updateQuestion(Long id, TestQuestion question) {
        return Optional.ofNullable(id)
                .filter(i -> i > 0)
                .map(i -> Optional.ofNullable(question)
                        .map(q -> {
                            TestQuestion existingQuestion = testQuestionRepository.findById(i)
                                    .orElseThrow(() -> new EntityNotFoundException("Question not found with id " + i));
                            return updateQuestionFields(existingQuestion, q);
                        })
                        .orElseThrow(() -> new IllegalArgumentException("Updated question data must not be null")))
                .orElseThrow(() -> new IllegalArgumentException("Invalid question ID"));
    }

    private TestQuestion updateQuestionFields(TestQuestion existingQuestion, TestQuestion questionToUpdate) {
        return Optional.ofNullable(questionToUpdate)
                .map(update -> {
                    Optional.ofNullable(update.getQuestionText())
                            .ifPresent(existingQuestion::setQuestionText);
                    Optional.ofNullable(update.getPointValue())
                            .ifPresent(existingQuestion::setPointValue);
                    Optional.ofNullable(update.getTest())
                            .ifPresent(existingQuestion::setTest);
                    Optional.ofNullable(update.getAnswers())
                            .ifPresent(existingQuestion::setAnswers);
                    return testQuestionRepository.save(existingQuestion);
                })
                .orElseThrow(() -> new IllegalArgumentException("Question data to update cannot be null"));
    }
}
