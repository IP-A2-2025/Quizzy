package com.backend.controller;

import com.backend.model.TestEntity;
import com.backend.service.TestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.*;

import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class TestControllerTest {

    @Mock
    private TestService testService;

    @InjectMocks
    private TestController testController;

    private TestEntity testSample;

    @BeforeEach
    void setUp() {
        openMocks(this);

        testSample = new TestEntity();
        testSample.setId(1L);
        testSample.setTitle("Sample Test");
        testSample.setDate(new Date());
    }

    @Test
    void shouldReturnAllTests() {
        List<TestEntity> tests = List.of(testSample);
        when(testService.getAllTests()).thenReturn(tests);

        ResponseEntity<Collection<TestEntity>> response = testController.getAllTests();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(tests, response.getBody());
    }

    @Test
    void shouldReturnTestById() {
        when(testService.getTestById(1L)).thenReturn(testSample);

        ResponseEntity<TestEntity> response = testController.getTestById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testSample, response.getBody());
    }

    @Test
    void shouldCreateTest() {
        when(testService.createTest(testSample)).thenReturn(testSample);

        ResponseEntity<TestEntity> response = testController.createTest(testSample);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(testSample, response.getBody());
    }

    @Test
    void shouldSaveTest() {
        when(testService.saveTest(testSample)).thenReturn(testSample);

        ResponseEntity<TestEntity> response = testController.saveTest(testSample);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(testSample, response.getBody());
    }

    @Test
    void shouldUpdateTest() {
        when(testService.updateTest(1L, testSample)).thenReturn(testSample);

        ResponseEntity<TestEntity> response = testController.updateTest(1L, testSample);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testSample, response.getBody());
    }

    @Test
    void shouldDeleteTestById() {
        ResponseEntity<Void> response = testController.deleteTestById(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(testService).deleteTestById(1L);
    }

    @Test
    void shouldReturnTestsByProfessorId() {
        when(testService.findTestsByProfId(100)).thenReturn(List.of(testSample));

        ResponseEntity<Collection<TestEntity>> response = testController.getTestsByProfessorId(100);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnTestsByCourseId() {
        when(testService.findTestsByCourseId(10L)).thenReturn(List.of(testSample));

        ResponseEntity<Collection<TestEntity>> response = testController.getTestsByCourseId(10L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnTestsByStudentId() {
        when(testService.findTestsForStudentEnrollments(5)).thenReturn(List.of(testSample));

        ResponseEntity<Collection<TestEntity>> response = testController.getTestsByStudentId(5);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnUpcomingTests() {
        when(testService.findUpcomingTests()).thenReturn(List.of(testSample));

        ResponseEntity<Collection<TestEntity>> response = testController.getUpcomingTests();

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldReturnTestsByDateRange() {
        Date start = new Date(System.currentTimeMillis() - 1000 * 60 * 60);
        Date end = new Date(System.currentTimeMillis() + 1000 * 60 * 60);
        when(testService.findByDateBetween(start, end)).thenReturn(List.of(testSample));

        ResponseEntity<Collection<TestEntity>> response = testController.getTestsByDateRange(start, end);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldReturnTestsByTitle() {
        when(testService.findByTitle("Sample")).thenReturn(List.of(testSample));

        ResponseEntity<Collection<TestEntity>> response = testController.getTestsByTitle("Sample");

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldReturnTestsByDescription() {
        when(testService.findByDescription("desc")).thenReturn(List.of(testSample));

        ResponseEntity<Collection<TestEntity>> response = testController.getTestsByDescription("desc");

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldReturnTestsByMonth() {
        when(testService.findByMonth(4)).thenReturn(List.of(testSample));

        ResponseEntity<Collection<TestEntity>> response = testController.getTestsByMonth(4);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldReturnTestsByYear() {
        when(testService.findByYear(2025)).thenReturn(List.of(testSample));

        ResponseEntity<Collection<TestEntity>> response = testController.getTestsByYear(2025);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldReturnTestsByExactDate() {
        Date date = new Date();
        when(testService.findTestsByExactDate(date)).thenReturn(List.of(testSample));

        ResponseEntity<Collection<TestEntity>> response = testController.getTestsByExactDate(date);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldReturnTestsByMonthAndYear() {
        when(testService.findByMonthAndYear(4, 2025)).thenReturn(List.of(testSample));

        ResponseEntity<Collection<TestEntity>> response = testController.getTestsByMonthAndYear(4, 2025);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldCountTestsByCourse() {
        when(testService.countTestsByCourse(1)).thenReturn(2L);

        ResponseEntity<Long> response = testController.countTestsByCourse(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2L, response.getBody());
    }

    @Test
    void shouldCountTestsByProfessor() {
        when(testService.countTestsByProfessor(1)).thenReturn(3L);

        ResponseEntity<Long> response = testController.countTestsByProfessor(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(3L, response.getBody());
    }

    @Test
    void shouldCountUpcomingTests() {
        when(testService.countUpcomingTests()).thenReturn(4L);

        ResponseEntity<Long> response = testController.countUpcomingTests();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(4L, response.getBody());
    }

    @Test
    void shouldCountTestsByStudent() {
        when(testService.countTestsForStudent(10)).thenReturn(1L);

        ResponseEntity<Long> response = testController.countTestsByStudent(10);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody());
    }

    @Test
    void shouldCountTestsByStudentEnrollments() {
        when(testService.countTestsForStudentEnrollments(10)).thenReturn(5L);

        ResponseEntity<Long> response = testController.countTestsByStudentEnrollments(10);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(5L, response.getBody());
    }

    @Test
    void shouldCountTestsByDateRange() {
        Date start = new Date();
        Date end = new Date();
        when(testService.countTestsByDateBetween(start, end)).thenReturn(6L);

        ResponseEntity<Long> response = testController.countTestsByDateRange(start, end);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(6L, response.getBody());
    }
}