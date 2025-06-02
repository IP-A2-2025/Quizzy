package com.backend.service;

import com.backend.model.TestEntity;
import com.backend.repository.TestRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class TestService {

    private final TestRepository testRepository;

    @Autowired
    public TestService(TestRepository testRepository) {
        this.testRepository = Objects.requireNonNull(testRepository, "TestRepository must not be null");
    }

    @Transactional
    public TestEntity saveTest(TestEntity test) {
        return Optional.ofNullable(test)
                .map(testRepository::save)
                .orElseThrow(() -> new IllegalArgumentException("Test must not be null"));
    }

    @Transactional
    public TestEntity createTest(TestEntity test) {
        return Optional.ofNullable(test)
                .filter(t -> t.getId() == null)
                .map(this::saveTest)
                .orElseThrow(() -> new IllegalArgumentException("New test must not have an ID"));
    }

    @Transactional(readOnly = true)
    public Collection<TestEntity> getAllTests() {
        return testRepository.findAll();
    }

    @Transactional(readOnly = true)
    public TestEntity getTestById(Long id) {
        return Optional.ofNullable(id)
                .filter(i -> i > 0)
                .flatMap(testRepository::findById)
                .orElseThrow(() -> new EntityNotFoundException("Test not found with id " + id));
    }

    @Transactional(readOnly = true)
    public Collection<TestEntity> findTestsByProfId(Integer profId) {
        return Optional.ofNullable(profId)
                .filter(id -> id > 0)
                .map(testRepository::findByProfessorId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid professor ID"));
    }

    @Transactional(readOnly = true)
    public Collection<TestEntity> findTestsByCourseId(Long courseId) {
        return Optional.ofNullable(courseId)
                .filter(id -> id > 0)
                .map(testRepository::findByCourseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID"));
    }

    @Transactional(readOnly = true)
    public Collection<TestEntity> findUpcomingTests() {
        return testRepository.findUpcomingTests();
    }

    @Transactional(readOnly = true)
    public Collection<TestEntity> findTestsForStudentEnrollments(Integer studentId) {
        return Optional.ofNullable(studentId)
                .filter(id -> id > 0)
                .map(testRepository::findTestsForStudentEnrollments)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student ID"));
    }

    @Transactional(readOnly = true)
    public Collection<TestEntity> findByDateBetween(Date startDate, Date endDate) {
        return Optional.ofNullable(startDate)
                .map(start -> Optional.ofNullable(endDate)
                        .filter(end -> !end.before(start))
                        .map(end -> testRepository.findByDateBetween(start, end))
                        .orElseThrow(() -> new IllegalArgumentException("End date must not be before start date")))
                .orElseThrow(() -> new IllegalArgumentException("Dates must not be null"));
    }

    @Transactional(readOnly = true)
    public Collection<TestEntity> findByTitle(String title) {
        return Optional.ofNullable(title)
                .map(testRepository::findByTitle)
                .orElseThrow(() -> new IllegalArgumentException("Title must not be null"));
    }

    @Transactional(readOnly = true)
    public Collection<TestEntity> findByDescription(String description) {
        return Optional.ofNullable(description)
                .map(testRepository::findByDescription)
                .orElseThrow(() -> new IllegalArgumentException("Description must not be null"));
    }

    @Transactional(readOnly = true)
    public Collection<TestEntity> findByMonth(Integer month) {
        return Optional.ofNullable(month)
                .map(testRepository::findByMonth)
                .orElseThrow(() -> new IllegalArgumentException("Month must not be null"));
    }

    @Transactional(readOnly = true)
    public Collection<TestEntity> findByYear(Integer year) {
        return Optional.ofNullable(year)
                .map(testRepository::findByYear)
                .orElseThrow(() -> new IllegalArgumentException("Year must not be null"));
    }

    @Transactional(readOnly = true)
    public Collection<TestEntity> findTestsByExactDate(Date date) {
        return Optional.ofNullable(date)
                .map(testRepository::findTestsByExactDate)
                .orElseThrow(() -> new IllegalArgumentException("Date must not be null"));
    }

    @Transactional(readOnly = true)
    public Collection<TestEntity> findByMonthAndYear(Integer month, Integer year) {
        return Optional.ofNullable(month)
                .flatMap(m -> Optional.ofNullable(year)
                        .map(y -> testRepository.findByMonthAndYear(m, y)))
                .orElseThrow(() -> new IllegalArgumentException("Month and year must not be null"));
    }

    @Transactional(readOnly = true)
    public Long countTestsByCourse(Integer courseId) {
        return Optional.ofNullable(courseId)
                .filter(id -> id > 0)
                .map(testRepository::countTestsByCourse)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID"));
    }

    @Transactional(readOnly = true)
    public Long countTestsByProfessor(Integer professorId) {
        return Optional.ofNullable(professorId)
                .filter(id -> id > 0)
                .map(testRepository::countTestsByProfessor)
                .orElseThrow(() -> new IllegalArgumentException("Invalid professor ID"));
    }

    @Transactional(readOnly = true)
    public Long countUpcomingTests() {
        return testRepository.countUpcomingTests();
    }

    @Transactional(readOnly = true)
    public Long countTestsForStudentEnrollments(Integer studentId) {
        return Optional.ofNullable(studentId)
                .filter(id -> id > 0)
                .map(testRepository::countTestsForStudentEnrollments)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student ID"));
    }

    @Transactional(readOnly = true)
    public Long countTestsForStudent(Integer studentId) {
        return Optional.ofNullable(studentId)
                .filter(id -> id > 0)
                .map(testRepository::countTestsForStudent)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student ID"));
    }

    @Transactional(readOnly = true)
    public Long countTestsByDateBetween(Date startDate, Date endDate) {
        return Optional.ofNullable(startDate)
                .map(start -> Optional.ofNullable(endDate)
                        .filter(end -> !end.before(start))
                        .map(end -> testRepository.countTestsByDateBetween(start, end))
                        .orElseThrow(() -> new IllegalArgumentException("End date must not be before start date")))
                .orElseThrow(() -> new IllegalArgumentException("Dates must not be null"));
    }

    @Transactional
    public void deleteTestById(Long id) {
        Optional.ofNullable(id)
                .filter(i -> i > 0)
                .ifPresentOrElse(
                        i -> {
                            if (!testRepository.existsById(i)) {
                                throw new EntityNotFoundException("Test not found with id " + i);
                            }
                            testRepository.deleteById(i);
                        },
                        () -> {
                            throw new IllegalArgumentException("Invalid test ID");
                        }
                );
    }

    @Transactional
    public TestEntity updateTest(Long id, TestEntity test) {
        return Optional.ofNullable(id)
                .filter(i -> i > 0)
                .map(i -> Optional.ofNullable(test)
                        .map(t -> {
                            TestEntity existingTest = testRepository.findById(i)
                                    .orElseThrow(() -> new EntityNotFoundException("Test not found with id " + i));
                            return updateTestFields(existingTest, t);
                        })
                        .orElseThrow(() -> new IllegalArgumentException("Updated test data must not be null")))
                .orElseThrow(() -> new IllegalArgumentException("Invalid test ID"));
    }

    @Transactional
    private TestEntity updateTestFields(TestEntity existingTest, TestEntity testToUpdate) {
        return Optional.ofNullable(testToUpdate)
                .map(update -> {
                    Optional.ofNullable(update.getTitle())
                            .ifPresent(existingTest::setTitle);
                    Optional.ofNullable(update.getDescription())
                            .ifPresent(existingTest::setDescription);
                    Optional.ofNullable(update.getDate())
                            .ifPresent(existingTest::setDate);
                    Optional.ofNullable(update.getProfessor())
                            .ifPresent(existingTest::setProfessor);
                    Optional.ofNullable(update.getCourse())
                            .ifPresent(existingTest::setCourse);
                    Optional.ofNullable(update.getQuestions())
                            .ifPresent(existingTest::setQuestions);
                    return testRepository.save(existingTest);
                })
                .orElseThrow(() -> new IllegalArgumentException("Test data to update cannot be null"));
    }
}