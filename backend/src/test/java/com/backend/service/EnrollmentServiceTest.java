package com.backend.service;

import com.backend.model.Course;
import com.backend.model.Enrollment;
import com.backend.model.EnrollmentId;
import com.backend.model.User;
import com.backend.repository.EnrollmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private CourseService courseService;

    @Mock
    private UserService userService;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Enrollment testEnrollment;
    private User testUser;
    private Course testCourse;
    private EnrollmentId testEnrollmentId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1);
        testUser.setEmail("test@example.com");

        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setTitle("Test Course");

        testEnrollmentId = new EnrollmentId(1, 1L);

        testEnrollment = new Enrollment();
        testEnrollment.setId(testEnrollmentId);
        testEnrollment.setUser(testUser);
        testEnrollment.setCourse(testCourse);
        testEnrollment.setEnrollmentDate(new Date());
    }

    @Test
    void shouldGetAllEnrollments() {
        List<Enrollment> enrollments = List.of(testEnrollment);
        when(enrollmentRepository.getAllEnrollments()).thenReturn(enrollments);

        var result = enrollmentService.getAllEnrollments();

        assertEquals(enrollments, result);
        verify(enrollmentRepository).getAllEnrollments();
    }

    @Test
    void shouldGetAllEnrollmentsByUserId() {
        List<Enrollment> enrollments = List.of(testEnrollment);
        when(enrollmentRepository.findByUserId(1)).thenReturn(enrollments);

        var result = enrollmentService.getALLEnrollmentsByUserId(1);

        assertEquals(enrollments, result);
        verify(enrollmentRepository).findByUserId(1);
    }

    @Test
    void shouldAddEnrollment() {
        doNothing().when(enrollmentRepository).insertEnrollment(1, 1L);

        enrollmentService.addEnrollment(1, 1L);

        verify(enrollmentRepository).insertEnrollment(1, 1L);
    }

    @Test
    void shouldUpdateEnrollmentSuccessfully() {
        Long newCourseId = 2L;
        when(enrollmentRepository.findById(testEnrollmentId)).thenReturn(Optional.of(testEnrollment));
        when(courseService.checkCourseById(newCourseId)).thenReturn(true);
        doNothing().when(enrollmentRepository).deleteById(testEnrollmentId);
        doNothing().when(enrollmentRepository).insertEnrollment(1, newCourseId);

        boolean result = enrollmentService.updateEnrollment(1, 1L, newCourseId);

        assertTrue(result);
        verify(enrollmentRepository).findById(testEnrollmentId);
        verify(courseService).checkCourseById(newCourseId);
        verify(enrollmentRepository).deleteById(testEnrollmentId);
        verify(enrollmentRepository).insertEnrollment(1, newCourseId);
    }

    @Test
    void shouldNotUpdateEnrollmentWhenOriginalEnrollmentDoesNotExist() {
        Long newCourseId = 2L;
        when(enrollmentRepository.findById(any(EnrollmentId.class))).thenReturn(Optional.empty());

        boolean result = enrollmentService.updateEnrollment(1, 1L, newCourseId);

        assertFalse(result);
        verify(enrollmentRepository).findById(any(EnrollmentId.class));
        verify(courseService, never()).checkCourseById(anyLong());
        verify(enrollmentRepository, never()).deleteById(any(EnrollmentId.class));
        verify(enrollmentRepository, never()).insertEnrollment(anyInt(), anyLong());
    }

    @Test
    void shouldNotUpdateEnrollmentWhenNewCourseDoesNotExist() {
        Long newCourseId = 2L;
        when(enrollmentRepository.findById(testEnrollmentId)).thenReturn(Optional.of(testEnrollment));
        when(courseService.checkCourseById(newCourseId)).thenReturn(false);

        boolean result = enrollmentService.updateEnrollment(1, 1L, newCourseId);

        assertFalse(result);
        verify(enrollmentRepository).findById(testEnrollmentId);
        verify(courseService).checkCourseById(newCourseId);
        verify(enrollmentRepository, never()).deleteById(any(EnrollmentId.class));
        verify(enrollmentRepository, never()).insertEnrollment(anyInt(), anyLong());
    }

    @Test
    void shouldDeleteEnrollmentSuccessfully() {
        when(userService.checkUserById(1)).thenReturn(true);
        when(courseService.checkCourseById(1L)).thenReturn(true);
        doNothing().when(enrollmentRepository).deleteById(testEnrollmentId);

        boolean result = enrollmentService.deleteEnrollment(1, 1L);

        assertTrue(result);
        verify(userService).checkUserById(1);
        verify(courseService).checkCourseById(1L);
        verify(enrollmentRepository).deleteById(testEnrollmentId);
    }

    @Test
    void shouldNotDeleteEnrollmentWhenUserDoesNotExist() {
        when(userService.checkUserById(1)).thenReturn(false);

        boolean result = enrollmentService.deleteEnrollment(1, 1L);

        assertFalse(result);
        verify(userService).checkUserById(1);
        verify(courseService, never()).checkCourseById(anyLong());
        verify(enrollmentRepository, never()).deleteById(any(EnrollmentId.class));
    }

    @Test
    void shouldNotDeleteEnrollmentWhenCourseDoesNotExist() {
        when(userService.checkUserById(1)).thenReturn(true);
        when(courseService.checkCourseById(1L)).thenReturn(false);

        boolean result = enrollmentService.deleteEnrollment(1, 1L);

        assertFalse(result);
        verify(userService).checkUserById(1);
        verify(courseService).checkCourseById(1L);
        verify(enrollmentRepository, never()).deleteById(any(EnrollmentId.class));
    }

    @Test
    void shouldGetUsersByCourseId() {
        List<Enrollment> enrollments = List.of(testEnrollment);
        when(enrollmentRepository.findByCourseId(1L)).thenReturn(enrollments);

        List<User> result = enrollmentService.getUsersByCourseId(1L);

        assertEquals(1, result.size());
        assertEquals(testUser, result.get(0));
        verify(enrollmentRepository).findByCourseId(1L);
    }

    @Test
    void shouldDeleteAllEnrollmentsForUser() {
        doNothing().when(enrollmentRepository).deleteByUserId(1);

        enrollmentService.deleteAllEnrollmentsForUser(1);

        verify(enrollmentRepository).deleteByUserId(1);
    }
} 