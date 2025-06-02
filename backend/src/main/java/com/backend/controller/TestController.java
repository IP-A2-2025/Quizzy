package com.backend.controller;

import com.backend.model.TestEntity;
import com.backend.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;

@RestController
@RequestMapping("/tests")
public class TestController {

    private final TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping
    public ResponseEntity<Collection<TestEntity>> getAllTests() {
        return ResponseEntity.ok(testService.getAllTests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestEntity> getTestById(@PathVariable Long id) {
        return ResponseEntity.ok(testService.getTestById(id));
    }

    @PostMapping
    public ResponseEntity<TestEntity> createTest(@RequestBody TestEntity test) {
        return ResponseEntity.status(HttpStatus.CREATED).body(testService.createTest(test));
    }

    @PostMapping("/save")
    public ResponseEntity<TestEntity> saveTest(@RequestBody TestEntity test) {
        return ResponseEntity.status(HttpStatus.CREATED).body(testService.saveTest(test));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestEntity> updateTest(@PathVariable Long id, @RequestBody TestEntity test) {
        return ResponseEntity.ok(testService.updateTest(id, test));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestById(@PathVariable Long id) {
        testService.deleteTestById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/professor/{professorId}")
    public ResponseEntity<Collection<TestEntity>> getTestsByProfessorId(@PathVariable Integer professorId) {
        return ResponseEntity.ok(testService.findTestsByProfId(professorId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<Collection<TestEntity>> getTestsByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(testService.findTestsByCourseId(courseId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<Collection<TestEntity>> getTestsByStudentId(@PathVariable Integer studentId) {
        return ResponseEntity.ok(testService.findTestsForStudentEnrollments(studentId));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<Collection<TestEntity>> getUpcomingTests() {
        return ResponseEntity.ok(testService.findUpcomingTests());
    }

    @GetMapping("/byDate")
    public ResponseEntity<Collection<TestEntity>> getTestsByDateRange(@RequestParam Date start, @RequestParam Date end) {
        return ResponseEntity.ok(testService.findByDateBetween(start, end));
    }

    @GetMapping("/byTitle")
    public ResponseEntity<Collection<TestEntity>> getTestsByTitle(@RequestParam String title) {
        return ResponseEntity.ok(testService.findByTitle(title));
    }

    @GetMapping("/byDescription")
    public ResponseEntity<Collection<TestEntity>> getTestsByDescription(@RequestParam String description) {
        return ResponseEntity.ok(testService.findByDescription(description));
    }

    @GetMapping("/byMonth")
    public ResponseEntity<Collection<TestEntity>> getTestsByMonth(@RequestParam Integer month) {
        return ResponseEntity.ok(testService.findByMonth(month));
    }

    @GetMapping("/byYear")
    public ResponseEntity<Collection<TestEntity>> getTestsByYear(@RequestParam Integer year) {
        return ResponseEntity.ok(testService.findByYear(year));
    }

    @GetMapping("/byExactDate")
    public ResponseEntity<Collection<TestEntity>> getTestsByExactDate(@RequestParam Date date) {
        return ResponseEntity.ok(testService.findTestsByExactDate(date));
    }

    @GetMapping("/byMonthYear")
    public ResponseEntity<Collection<TestEntity>> getTestsByMonthAndYear(@RequestParam Integer month, @RequestParam Integer year) {
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
}