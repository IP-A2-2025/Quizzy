package com.backend.controller;

import com.backend.dto.LoginResponse;
import com.backend.dto.TestDTO;
import com.backend.model.TestEntity;
import com.backend.service.TestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Date;

@RestController
@RequestMapping("/tests")
public class TestController {

    private final TestService testService;
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping
    public ResponseEntity<Collection<TestDTO>> getAllTests() {
        return ResponseEntity.ok(testService.getAllTests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestDTO> getTestById(@PathVariable Long id) {
        return ResponseEntity.ok(testService.getTestById(id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TestEntity> createTest(@Valid @RequestBody TestDTO testDTO) {
        logger.info("Attempting to create new test");
        var test = testService.createTest(testDTO);
        try{
            return ResponseEntity.ok(test);
        } catch (Exception e){
            logger.error("Failed to create test: {}", e.getMessage());
            throw e;
        } finally {
            logger.info("Test created successfully with ID: {}", test.getId());
        }
    }
    @PostMapping("/save")
        public ResponseEntity<TestDTO> saveTest(@RequestBody TestDTO testDTO) {
            return ResponseEntity.status(HttpStatus.CREATED).body(testService.saveTest(testDTO));
        }

    @PutMapping("/{id}")
    public ResponseEntity<TestEntity> updateTest(@PathVariable Long id, @RequestBody TestDTO testDTO) {
        logger.info("Attempting to update new test");
        var test = testService.updateTest(id, testDTO);
        try{
            return ResponseEntity.ok(test);
        } catch (Exception e){
            logger.error("Failed to update test: {}", e.getMessage());
            throw e;
        } finally {
            logger.info("Test updated successfully with ID: {}", test.getId());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTestById(@PathVariable Long id) {
        logger.info("Attempting to delete test");
        try {
            return ResponseEntity.ok(testService.deleteTestById(id));
        } catch (Exception e) {
            logger.error("Failed to delete test: {}", e.getMessage());
            throw e;
        } finally {
            logger.info("Test deleted successfully with ID: {}", id);
        }
    }

    @GetMapping("/professor/{professorId}")
    public ResponseEntity<Collection<TestDTO>> getTestsByProfessorId(@PathVariable Integer professorId) {
        return ResponseEntity.ok(testService.findTestsByProfId(professorId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<Collection<TestDTO>> getTestsByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(testService.findTestsByCourseId(courseId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<Collection<TestDTO>> getTestsByStudentId(@PathVariable Integer studentId) {
        return ResponseEntity.ok(testService.findTestsForStudentEnrollments(studentId));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<Collection<TestDTO>> getUpcomingTests() {
        return ResponseEntity.ok(testService.findUpcomingTests());
    }

    @GetMapping("/byDate")
    public ResponseEntity<Collection<TestDTO>> getTestsByDateRange(@RequestParam Date start, @RequestParam Date end) {
        return ResponseEntity.ok(testService.findByDateBetween(start, end));
    }

    @GetMapping("/byTitle")
    public ResponseEntity<Collection<TestDTO>> getTestsByTitle(@RequestParam String title) {
        return ResponseEntity.ok(testService.findByTitle(title));
    }

    @GetMapping("/byDescription")
    public ResponseEntity<Collection<TestDTO>> getTestsByDescription(@RequestParam String description) {
        return ResponseEntity.ok(testService.findByDescription(description));
    }

    @GetMapping("/byMonth")
    public ResponseEntity<Collection<TestDTO>> getTestsByMonth(@RequestParam Integer month) {
        return ResponseEntity.ok(testService.findByMonth(month));
    }

    @GetMapping("/byYear")
    public ResponseEntity<Collection<TestDTO>> getTestsByYear(@RequestParam Integer year) {
        return ResponseEntity.ok(testService.findByYear(year));
    }

    @GetMapping("/byExactDate")
    public ResponseEntity<Collection<TestDTO>> getTestsByExactDate(@RequestParam Date date) {
        return ResponseEntity.ok(testService.findTestsByExactDate(date));
    }

    @GetMapping("/byMonthYear")
    public ResponseEntity<Collection<TestDTO>> getTestsByMonthAndYear(@RequestParam Integer month, @RequestParam Integer year) {
        return ResponseEntity.ok(testService.findByMonthAndYear(month, year));
    }

    @GetMapping("/countByCourse/{courseId}")
    public ResponseEntity<Long> countTestsByCourse(@PathVariable Integer courseId) {
        return ResponseEntity.ok(testService.countTestsByCourse(courseId));
    }

    @GetMapping("/countByProfessor/{professorId}")
    public ResponseEntity<Long> countTestsByProfessor(@PathVariable Integer professorId) {
        return ResponseEntity.ok(testService.countTestsByProfessor(professorId));
    }

    @GetMapping("/countUpcoming")
    public ResponseEntity<Long> countUpcomingTests() {
        return ResponseEntity.ok(testService.countUpcomingTests());
    }

    @GetMapping("/countByStudent/{studentId}")
    public ResponseEntity<Long> countTestsByStudent(@PathVariable Integer studentId) {
        return ResponseEntity.ok(testService.countTestsForStudent(studentId));
    }

    @GetMapping("/countByStudentEnrollments/{studentId}")
    public ResponseEntity<Long> countTestsByStudentEnrollments(@PathVariable Integer studentId) {
        return ResponseEntity.ok(testService.countTestsForStudentEnrollments(studentId));
    }

    @GetMapping("/countByDateRange")
    public ResponseEntity<Long> countTestsByDateRange(@RequestParam Date start, @RequestParam Date end) {
        return ResponseEntity.ok(testService.countTestsByDateBetween(start, end));
    }
//    @DeleteMapping("/complete/{id}")
//    public ResponseEntity<Void> deleteTestAndRelatedEntities(@PathVariable Long id) {
//        testService.deleteTestAndRelatedEntities(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @DeleteMapping("/multiple")
//    public ResponseEntity<Integer> deleteMultipleTests(@RequestBody Collection<Long> testIds) {
//        int deletedCount = testService.deleteMultipleTests(testIds);
//        return ResponseEntity.ok(deletedCount);
//    }
}