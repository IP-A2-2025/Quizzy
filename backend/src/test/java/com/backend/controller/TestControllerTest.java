package com.backend.controller;

import com.backend.dto.TestDTO;
import com.backend.service.TestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestControllerTest {

    @Mock
    private TestService testService;

    @InjectMocks
    private TestController testController;

    private TestDTO testSampleDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testSampleDTO = new TestDTO();
        testSampleDTO.setId(1L);
        testSampleDTO.setTitle("Sample Test");
        testSampleDTO.setDate(new Date());
        testSampleDTO.setProfessorId(1);
        testSampleDTO.setCourseId(1L);
    }

    @Test
    void shouldReturnAllTests() {
        Collection<TestDTO> tests = new ArrayList<>();
        tests.add(testSampleDTO);

        when(testService.getAllTests()).thenReturn(tests);

        ResponseEntity<Collection<TestDTO>> response = testController.getAllTests();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(testSampleDTO, response.getBody().iterator().next());
    }

    @Test
    void shouldReturnTestById() {
        when(testService.getTestById(1L)).thenReturn(testSampleDTO);

        ResponseEntity<TestDTO> response = testController.getTestById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testSampleDTO, response.getBody());
    }

    @Test
    void shouldCreateTest() {
        when(testService.createTest(testSampleDTO)).thenReturn(testSampleDTO);

        ResponseEntity<TestDTO> response = testController.createTest(testSampleDTO);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(testSampleDTO, response.getBody());

        verify(testService, times(1)).createTest(testSampleDTO);
    }

    @Test
    void shouldSaveTest() {
        when(testService.saveTest(testSampleDTO)).thenReturn(testSampleDTO);

        ResponseEntity<TestDTO> response = testController.saveTest(testSampleDTO);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(testSampleDTO, response.getBody());

        verify(testService, times(1)).saveTest(testSampleDTO);
    }

    @Test
    void shouldUpdateTest() {
        when(testService.updateTest(1L, testSampleDTO)).thenReturn(testSampleDTO);

        ResponseEntity<TestDTO> response = testController.updateTest(1L, testSampleDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testSampleDTO, response.getBody());

        verify(testService, times(1)).updateTest(1L, testSampleDTO);
    }

    @Test
    void shouldDeleteTestById() {
        doNothing().when(testService).deleteTestById(1L);

        ResponseEntity<Void> response = testController.deleteTestById(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(testService, times(1)).deleteTestById(1L);
    }

    @Test
    void shouldReturnTestsByProfessorId() {
        when(testService.findTestsByProfId(100)).thenReturn(List.of(testSampleDTO));

        ResponseEntity<Collection<TestDTO>> response = testController.getTestsByProfessorId(100);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnTestsByCourseId() {
        when(testService.findTestsByCourseId(10L)).thenReturn(List.of(testSampleDTO));

        ResponseEntity<Collection<TestDTO>> response = testController.getTestsByCourseId(10L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnTestsByStudentId() {
        when(testService.findTestsForStudentEnrollments(5)).thenReturn(List.of(testSampleDTO));

        ResponseEntity<Collection<TestDTO>> response = testController.getTestsByStudentId(5);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnUpcomingTests() {
        when(testService.findUpcomingTests()).thenReturn(List.of(testSampleDTO));

        ResponseEntity<Collection<TestDTO>> response = testController.getUpcomingTests();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnTestsByDateRange() {
        Date start = new Date(System.currentTimeMillis() - 1000 * 60 * 60);
        Date end = new Date(System.currentTimeMillis() + 1000 * 60 * 60);

        when(testService.findByDateBetween(start, end)).thenReturn(List.of(testSampleDTO));

        ResponseEntity<Collection<TestDTO>> response = testController.getTestsByDateRange(start, end);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnTestsByTitle() {
        when(testService.findByTitle("Sample")).thenReturn(List.of(testSampleDTO));

        ResponseEntity<Collection<TestDTO>> response = testController.getTestsByTitle("Sample");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnTestsByDescription() {
        when(testService.findByDescription("desc")).thenReturn(List.of(testSampleDTO));

        ResponseEntity<Collection<TestDTO>> response = testController.getTestsByDescription("desc");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnTestsByMonth() {
        when(testService.findByMonth(4)).thenReturn(List.of(testSampleDTO));

        ResponseEntity<Collection<TestDTO>> response = testController.getTestsByMonth(4);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnTestsByYear() {
        when(testService.findByYear(2025)).thenReturn(List.of(testSampleDTO));

        ResponseEntity<Collection<TestDTO>> response = testController.getTestsByYear(2025);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnTestsByExactDate() {
        Date date = new Date();

        when(testService.findTestsByExactDate(date)).thenReturn(List.of(testSampleDTO));

        ResponseEntity<Collection<TestDTO>> response = testController.getTestsByExactDate(date);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnTestsByMonthAndYear() {
        when(testService.findByMonthAndYear(4, 2025)).thenReturn(List.of(testSampleDTO));

        ResponseEntity<Collection<TestDTO>> response = testController.getTestsByMonthAndYear(4, 2025);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
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
