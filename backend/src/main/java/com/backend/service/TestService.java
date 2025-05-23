package com.backend.service;

import com.backend.dto.TestDTO;
import com.backend.mapper.TestMapper;
import com.backend.model.TestEntity;
import com.backend.model.TestQuestion;
import com.backend.model.User;
import com.backend.repository.CourseRepository;
import com.backend.repository.TestRepository;
import com.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class TestService {

    private final TestRepository testRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Autowired
    public TestService(TestRepository testRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.testRepository = Objects.requireNonNull(testRepository, "TestRepository must not be null");
        this.courseRepository = Objects.requireNonNull(courseRepository, "CourseRepository must not be null");
        this.userRepository = Objects.requireNonNull(userRepository, "UserRepository must not be null");
    }

    @Transactional
    public TestEntity saveTest(TestEntity test) {
        return Optional.ofNullable(test)
                .map(testRepository::save)
                .orElseThrow(() -> new IllegalArgumentException("Test must not be null"));
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

    @Transactional
    public TestEntity createTest(TestDTO testDTO) {
        TestMapper mapper = new TestMapper(courseRepository, userRepository);
        TestEntity test = mapper.toEntity(testDTO);

        Objects.requireNonNull(test, "Test entity must not be null");
        if (test.getId() != null) {
            throw new IllegalArgumentException("New test must have no ID");
        }

        User creator = test.getProfessor();
        if (creator == null) {
            throw new IllegalArgumentException("Test must have an associated professor/admin");
        }

        String role = creator.getRole();
        if (!("PROFESOR".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role))) {
            throw new AccessDeniedException("Only professors or admins can create tests");
        }
        saveTest(test);
        return new TestEntity(
                    test.getId(),
                    testDTO.getTitle(),
                    "Test added successfully ",
                    testDTO.getDate(),
                    null,
                    null,
                    null
            );
    }

    @Transactional
    public TestEntity updateTest(Long id, TestDTO testDTO) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid test ID");
        }
        if (testDTO == null) {
            throw new IllegalArgumentException("Updated test data must not be null");
        }

        TestEntity existingTest = testRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Test not found with id " + id));

        if (testDTO.getTitle() != null && !testDTO.getTitle().trim().isEmpty()) {
            existingTest.setTitle(testDTO.getTitle());
        }
        if (testDTO.getDescription() != null && !testDTO.getDescription().trim().isEmpty()) {
            existingTest.setDescription(testDTO.getDescription());
        }
        if (testDTO.getDate() != null) {
            existingTest.setDate(testDTO.getDate());
        }

        return saveTest(existingTest);
    }

    @Transactional
    public String deleteTestById(Long id) {
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
        return "Test deleted successfully";
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
    public Collection<TestEntity> findTestsForStudentEnrollments(Integer studentId) {
        return Optional.ofNullable(studentId)
                .filter(id -> id > 0)
                .map(testRepository::findTestsForStudentEnrollments)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student ID"));
    }

    @Transactional(readOnly = true)
    public Collection<TestEntity> findUpcomingTests() {
        return testRepository.findUpcomingTests();
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
//    @Transactional
//    public TestDTO updateTestFields(TestEntity existingTest, TestDTO testToUpdate) {
//        return Optional.ofNullable(testToUpdate)
//                .map(update -> {
//                    Optional.ofNullable(update.getTitle())
//                            .ifPresent(existingTest::setTitle);
//                    Optional.ofNullable(update.getDescription())
//                            .ifPresent(existingTest::setDescription);
//                    Optional.ofNullable(update.getDate())
//                            .ifPresent(existingTest::setDate);
//
//                    return testRepository.save(existingTest);
//                })
//                .orElseThrow(() -> new IllegalArgumentException("Test data to update cannot be null"));
//    }
}