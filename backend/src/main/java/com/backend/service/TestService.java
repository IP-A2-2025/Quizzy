package com.backend.service;

import com.backend.dto.TestDTO;
import com.backend.mapper.TestMapper;
import com.backend.model.Course;
import com.backend.model.TestEntity;
import com.backend.model.User;
import com.backend.repository.CourseRepository;
import com.backend.repository.TestRepository;
import com.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final TestMapper testMapper;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public TestService(TestRepository testRepository, TestMapper testMapper,UserRepository userRepository,CourseRepository courseRepository) {
        this.testRepository = Objects.requireNonNull(testRepository, "TestRepository must not be null");
        this.testMapper = testMapper;
        this.userRepository = userRepository;
        this.courseRepository=courseRepository;
    }

    @Transactional
    public TestDTO saveTest(TestDTO testDTO) {
        return Optional.ofNullable(testDTO)
                .map(testMapper::toEntity)
                .map(testRepository::save)
                .map(testMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("TestDTO must not be null"));
    }

    @Transactional
    public TestDTO createTest(TestDTO testDTO) {
        return Optional.ofNullable(testDTO)
                .filter(t -> t.getId() == null)
                .map(this::saveTest)
                .orElseThrow(() -> new IllegalArgumentException("New test must not have an ID"));
    }

    @Transactional(readOnly = true)
    public Collection<TestDTO> getAllTests() {
        return testRepository.findAll().stream()
                .map(testMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TestDTO getTestById(Long id) {
        return Optional.ofNullable(id)
                .filter(i -> i > 0)
                .flatMap(testRepository::findById)
                .map(testMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Test not found with id " + id));
    }

    @Transactional(readOnly = true)
    public Collection<TestDTO> findTestsByProfId(Integer profId) {
        return Optional.ofNullable(profId)
                .filter(id -> id > 0)
                .map(testRepository::findByProfessorId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid professor ID"))
                .stream()
                .map(testMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Collection<TestDTO> findTestsByCourseId(Long courseId) {
        return Optional.ofNullable(courseId)
                .filter(id -> id > 0)
                .map(testRepository::findByCourseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID"))
                .stream()
                .map(testMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Collection<TestDTO> findUpcomingTests() {
        return testRepository.findUpcomingTests().stream()
                .map(testMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Collection<TestDTO> findTestsForStudentEnrollments(Integer studentId) {
        return Optional.ofNullable(studentId)
                .filter(id -> id > 0)
                .map(testRepository::findTestsForStudentEnrollments)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student ID"))
                .stream()
                .map(testMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Collection<TestDTO> findByDateBetween(Date startDate, Date endDate) {
        return Optional.ofNullable(startDate)
                .map(start -> Optional.ofNullable(endDate)
                        .filter(end -> !end.before(start))
                        .map(end -> testRepository.findByDateBetween(start, end))
                        .orElseThrow(() -> new IllegalArgumentException("End date must not be before start date")))
                .orElseThrow(() -> new IllegalArgumentException("Dates must not be null"))
                .stream()
                .map(testMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Collection<TestDTO> findByTitle(String title) {
        return Optional.ofNullable(title)
                .map(testRepository::findByTitle)
                .orElseThrow(() -> new IllegalArgumentException("Title must not be null"))
                .stream()
                .map(testMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Collection<TestDTO> findByDescription(String description) {
        return Optional.ofNullable(description)
                .map(testRepository::findByDescription)
                .orElseThrow(() -> new IllegalArgumentException("Description must not be null"))
                .stream()
                .map(testMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Collection<TestDTO> findByMonth(Integer month) {
        return Optional.ofNullable(month)
                .map(testRepository::findByMonth)
                .orElseThrow(() -> new IllegalArgumentException("Month must not be null"))
                .stream()
                .map(testMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Collection<TestDTO> findByYear(Integer year) {
        return Optional.ofNullable(year)
                .map(testRepository::findByYear)
                .orElseThrow(() -> new IllegalArgumentException("Year must not be null"))
                .stream()
                .map(testMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Collection<TestDTO> findTestsByExactDate(Date date) {
        return Optional.ofNullable(date)
                .map(testRepository::findTestsByExactDate)
                .orElseThrow(() -> new IllegalArgumentException("Date must not be null"))
                .stream()
                .map(testMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Collection<TestDTO> findByMonthAndYear(Integer month, Integer year) {
        return Optional.ofNullable(month)
                .flatMap(m -> Optional.ofNullable(year)
                        .map(y -> testRepository.findByMonthAndYear(m, y)))
                .orElseThrow(() -> new IllegalArgumentException("Month and year must not be null"))
                .stream()
                .map(testMapper::toDTO)
                .collect(Collectors.toList());
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
    public TestDTO updateTest(Long id, TestDTO testDTO) {
        // Validate input
        if (testDTO == null) {
            throw new IllegalArgumentException("TestDTO must not be null");
        }

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid test ID");
        }

        // Check if test exists
        TestEntity existingTest = testRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Test not found with id " + id));

        // Validate required fields
        if (testDTO.getTitle() == null || testDTO.getDate() == null ||
                testDTO.getProfessorId() == null || testDTO.getCourseId() == null) {
            throw new IllegalArgumentException("All required fields must be provided");
        }

        // Update fields
        updateTestFields(existingTest, testDTO);

        // Save and return
        return testMapper.toDTO(testRepository.save(existingTest));
    }

    private void updateTestFields(TestEntity existingTest, TestDTO dto) {
        existingTest.setTitle(dto.getTitle());
        existingTest.setDescription(dto.getDescription());
        existingTest.setDate(dto.getDate());


        if (!existingTest.getProfessor().getId().equals(dto.getProfessorId())) {
            User professor = userRepository.findById(dto.getProfessorId())
                    .orElseThrow(() -> new EntityNotFoundException("Professor not found"));
            existingTest.setProfessor(professor);
        }
        
        if (!existingTest.getCourse().getId().equals(dto.getCourseId())) {
            Course course = courseRepository.findById(dto.getCourseId())
                    .orElseThrow(() -> new EntityNotFoundException("Course not found"));
            existingTest.setCourse(course);
        }
    }
}
