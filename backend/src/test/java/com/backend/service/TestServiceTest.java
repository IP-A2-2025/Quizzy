package com.backend.service;

import com.backend.model.Course;
import com.backend.model.TestEntity;
import com.backend.model.TestQuestion;
import com.backend.model.User;
import com.backend.repository.TestRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TestServiceTest {

    @Mock
    private TestRepository testRepository;

    @InjectMocks
    private TestService testService;

    private TestEntity testEntity;
    private User professor;
    private Course course;
    private Date testDate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        professor = new User();
        professor.setId(1);
        professor.setEmail("professor@example.com");

        course = new Course();
        course.setId(1L);
        course.setTitle("Test Course");

        testDate = new Date();

        testEntity = new TestEntity();
        testEntity.setId(1L);
        testEntity.setTitle("Test Exam");
        testEntity.setDescription("Test Description");
        testEntity.setDate(testDate);
        testEntity.setProfessor(professor);
        testEntity.setCourse(course);
        testEntity.setQuestions(new ArrayList<>());
    }

    @Test
    void shouldSaveTest() {
        when(testRepository.save(testEntity)).thenReturn(testEntity);

        TestEntity result = testService.saveTest(testEntity);

        assertEquals(testEntity, result);
        verify(testRepository).save(testEntity);
    }

    @Test
    void shouldThrowExceptionWhenSavingNullTest() {
        assertThrows(IllegalArgumentException.class, () -> testService.saveTest(null));
    }

    @Test
    void shouldCreateTest() {
        TestEntity newTest = new TestEntity();
        newTest.setTitle("New Test");
        newTest.setDescription("New Description");
        newTest.setDate(testDate);
        newTest.setProfessor(professor);
        newTest.setCourse(course);

        when(testRepository.save(any(TestEntity.class))).thenReturn(newTest);

        TestEntity result = testService.createTest(newTest);

        assertEquals(newTest, result);
        verify(testRepository).save(newTest);
    }

    @Test
    void shouldThrowExceptionWhenCreatingTestWithId() {
        TestEntity invalidTest = new TestEntity();
        invalidTest.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> testService.createTest(invalidTest));
    }

    @Test
    void shouldGetAllTests() {
        when(testRepository.findAll()).thenReturn(List.of(testEntity));

        var result = testService.getAllTests();

        assertEquals(1, result.size());
        verify(testRepository).findAll();
    }

    @Test
    void shouldGetTestById() {
        when(testRepository.findById(1L)).thenReturn(Optional.of(testEntity));

        var result = testService.getTestById(1L);

        assertEquals(testEntity, result);
        verify(testRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTestNotFound() {
        when(testRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> testService.getTestById(999L));
    }

    @Test
    void shouldThrowExceptionWhenInvalidTestId() {
        assertThrows(EntityNotFoundException.class, () -> testService.getTestById(0L));
        assertThrows(EntityNotFoundException.class, () -> testService.getTestById(null));
    }

    @Test
    void shouldFindTestsByProfId() {
        when(testRepository.findByProfessorId(1)).thenReturn(List.of(testEntity));

        var result = testService.findTestsByProfId(1);

        assertEquals(1, result.size());
        verify(testRepository).findByProfessorId(1);
    }

    @Test
    void shouldThrowExceptionWhenInvalidProfId() {
        assertThrows(IllegalArgumentException.class, () -> testService.findTestsByProfId(0));
        assertThrows(IllegalArgumentException.class, () -> testService.findTestsByProfId(null));
    }

    @Test
    void shouldFindTestsByCourseId() {
        when(testRepository.findByCourseId(1L)).thenReturn(List.of(testEntity));

        var result = testService.findTestsByCourseId(1L);

        assertEquals(1, result.size());
        verify(testRepository).findByCourseId(1L);
    }

    @Test
    void shouldThrowExceptionWhenInvalidCourseId() {
        assertThrows(IllegalArgumentException.class, () -> testService.findTestsByCourseId(0L));
        assertThrows(IllegalArgumentException.class, () -> testService.findTestsByCourseId(null));
    }

    @Test
    void shouldFindUpcomingTests() {
        when(testRepository.findUpcomingTests()).thenReturn(List.of(testEntity));

        var result = testService.findUpcomingTests();

        assertEquals(1, result.size());
        verify(testRepository).findUpcomingTests();
    }

    @Test
    void shouldFindTestsForStudentEnrollments() {
        when(testRepository.findTestsForStudentEnrollments(1)).thenReturn(List.of(testEntity));

        var result = testService.findTestsForStudentEnrollments(1);

        assertEquals(1, result.size());
        verify(testRepository).findTestsForStudentEnrollments(1);
    }

    @Test
    void shouldThrowExceptionWhenInvalidStudentId() {
        assertThrows(IllegalArgumentException.class, () -> testService.findTestsForStudentEnrollments(0));
        assertThrows(IllegalArgumentException.class, () -> testService.findTestsForStudentEnrollments(null));
    }

    @Test
    void shouldFindByDateBetween() {
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 86400000); // One day later

        when(testRepository.findByDateBetween(startDate, endDate)).thenReturn(List.of(testEntity));

        var result = testService.findByDateBetween(startDate, endDate);

        assertEquals(1, result.size());
        verify(testRepository).findByDateBetween(startDate, endDate);
    }

    @Test
    void shouldThrowExceptionWhenInvalidDateRange() {
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() - 86400000); // One day earlier

        assertThrows(IllegalArgumentException.class, () -> testService.findByDateBetween(startDate, endDate));
        assertThrows(IllegalArgumentException.class, () -> testService.findByDateBetween(null, endDate));
        assertThrows(IllegalArgumentException.class, () -> testService.findByDateBetween(startDate, null));
    }

    @Test
    void shouldFindByTitle() {
        when(testRepository.findByTitle("Test")).thenReturn(List.of(testEntity));

        var result = testService.findByTitle("Test");

        assertEquals(1, result.size());
        verify(testRepository).findByTitle("Test");
    }

    @Test
    void shouldThrowExceptionWhenNullTitle() {
        assertThrows(IllegalArgumentException.class, () -> testService.findByTitle(null));
    }

    @Test
    void shouldFindByDescription() {
        when(testRepository.findByDescription("Description")).thenReturn(List.of(testEntity));

        var result = testService.findByDescription("Description");

        assertEquals(1, result.size());
        verify(testRepository).findByDescription("Description");
    }

    @Test
    void shouldThrowExceptionWhenNullDescription() {
        assertThrows(IllegalArgumentException.class, () -> testService.findByDescription(null));
    }

    @Test
    void shouldFindByMonth() {
        when(testRepository.findByMonth(7)).thenReturn(List.of(testEntity));

        var result = testService.findByMonth(7);

        assertEquals(1, result.size());
        verify(testRepository).findByMonth(7);
    }

    @Test
    void shouldThrowExceptionWhenNullMonth() {
        assertThrows(IllegalArgumentException.class, () -> testService.findByMonth(null));
    }

    @Test
    void shouldFindByYear() {
        when(testRepository.findByYear(2023)).thenReturn(List.of(testEntity));

        var result = testService.findByYear(2023);

        assertEquals(1, result.size());
        verify(testRepository).findByYear(2023);
    }

    @Test
    void shouldThrowExceptionWhenNullYear() {
        assertThrows(IllegalArgumentException.class, () -> testService.findByYear(null));
    }

    @Test
    void shouldFindTestsByExactDate() {
        when(testRepository.findTestsByExactDate(testDate)).thenReturn(List.of(testEntity));

        var result = testService.findTestsByExactDate(testDate);

        assertEquals(1, result.size());
        verify(testRepository).findTestsByExactDate(testDate);
    }

    @Test
    void shouldThrowExceptionWhenNullExactDate() {
        assertThrows(IllegalArgumentException.class, () -> testService.findTestsByExactDate(null));
    }

    @Test
    void shouldFindByMonthAndYear() {
        when(testRepository.findByMonthAndYear(7, 2023)).thenReturn(List.of(testEntity));

        var result = testService.findByMonthAndYear(7, 2023);

        assertEquals(1, result.size());
        verify(testRepository).findByMonthAndYear(7, 2023);
    }

    @Test
    void shouldThrowExceptionWhenNullMonthOrYear() {
        assertThrows(IllegalArgumentException.class, () -> testService.findByMonthAndYear(null, 2023));
        assertThrows(IllegalArgumentException.class, () -> testService.findByMonthAndYear(7, null));
    }

    @Test
    void shouldCountTestsByCourse() {
        when(testRepository.countTestsByCourse(1)).thenReturn(5L);

        var result = testService.countTestsByCourse(1);

        assertEquals(5L, result);
        verify(testRepository).countTestsByCourse(1);
    }

    @Test
    void shouldThrowExceptionWhenInvalidCourseIdForCount() {
        assertThrows(IllegalArgumentException.class, () -> testService.countTestsByCourse(0));
        assertThrows(IllegalArgumentException.class, () -> testService.countTestsByCourse(null));
    }

    @Test
    void shouldCountTestsByProfessor() {
        when(testRepository.countTestsByProfessor(1)).thenReturn(10L);

        var result = testService.countTestsByProfessor(1);

        assertEquals(10L, result);
        verify(testRepository).countTestsByProfessor(1);
    }

    @Test
    void shouldThrowExceptionWhenInvalidProfessorIdForCount() {
        assertThrows(IllegalArgumentException.class, () -> testService.countTestsByProfessor(0));
        assertThrows(IllegalArgumentException.class, () -> testService.countTestsByProfessor(null));
    }

    @Test
    void shouldCountUpcomingTests() {
        when(testRepository.countUpcomingTests()).thenReturn(3L);

        var result = testService.countUpcomingTests();

        assertEquals(3L, result);
        verify(testRepository).countUpcomingTests();
    }

    @Test
    void shouldCountTestsForStudentEnrollments() {
        when(testRepository.countTestsForStudentEnrollments(1)).thenReturn(7L);

        var result = testService.countTestsForStudentEnrollments(1);

        assertEquals(7L, result);
        verify(testRepository).countTestsForStudentEnrollments(1);
    }

    @Test
    void shouldThrowExceptionWhenInvalidStudentIdForCount() {
        assertThrows(IllegalArgumentException.class, () -> testService.countTestsForStudentEnrollments(0));
        assertThrows(IllegalArgumentException.class, () -> testService.countTestsForStudentEnrollments(null));
    }

    @Test
    void shouldCountTestsForStudent() {
        when(testRepository.countTestsForStudent(1)).thenReturn(4L);

        var result = testService.countTestsForStudent(1);

        assertEquals(4L, result);
        verify(testRepository).countTestsForStudent(1);
    }

    @Test
    void shouldThrowExceptionWhenInvalidStudentIdForCountTests() {
        assertThrows(IllegalArgumentException.class, () -> testService.countTestsForStudent(0));
        assertThrows(IllegalArgumentException.class, () -> testService.countTestsForStudent(null));
    }

    @Test
    void shouldCountTestsByDateBetween() {
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 86400000); // One day later

        when(testRepository.countTestsByDateBetween(startDate, endDate)).thenReturn(2L);

        var result = testService.countTestsByDateBetween(startDate, endDate);

        assertEquals(2L, result);
        verify(testRepository).countTestsByDateBetween(startDate, endDate);
    }

    @Test
    void shouldThrowExceptionWhenInvalidDateRangeForCount() {
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() - 86400000); // One day earlier

        assertThrows(IllegalArgumentException.class, () -> testService.countTestsByDateBetween(startDate, endDate));
        assertThrows(IllegalArgumentException.class, () -> testService.countTestsByDateBetween(null, endDate));
        assertThrows(IllegalArgumentException.class, () -> testService.countTestsByDateBetween(startDate, null));
    }

    @Test
    void shouldDeleteTestById() {
        when(testRepository.existsById(1L)).thenReturn(true);
        doNothing().when(testRepository).deleteById(1L);

        testService.deleteTestById(1L);

        verify(testRepository).existsById(1L);
        verify(testRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentTest() {
        when(testRepository.existsById(999L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> testService.deleteTestById(999L));
    }

    @Test
    void shouldThrowExceptionWhenDeletingWithInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> testService.deleteTestById(0L));
        assertThrows(IllegalArgumentException.class, () -> testService.deleteTestById(null));
    }

    @Test
    void shouldUpdateTest() {
        TestEntity updateData = new TestEntity();
        updateData.setTitle("Updated Test");
        updateData.setDescription("Updated Description");

        when(testRepository.findById(1L)).thenReturn(Optional.of(testEntity));
        when(testRepository.save(any(TestEntity.class))).thenReturn(testEntity);

        var result = testService.updateTest(1L, updateData);

        assertEquals("Updated Test", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        verify(testRepository).findById(1L);
        verify(testRepository).save(testEntity);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentTest() {
        when(testRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> testService.updateTest(999L, new TestEntity()));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> testService.updateTest(0L, new TestEntity()));
        assertThrows(IllegalArgumentException.class, () -> testService.updateTest(null, new TestEntity()));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithNullData() {
        assertThrows(IllegalArgumentException.class, () -> testService.updateTest(1L, null));
    }
} 