package com.backend.controller;

import com.backend.model.Course;
import com.backend.model.Enrollment;
import com.backend.model.EnrollmentId;
import com.backend.service.CourseService;
import com.backend.service.EnrollmentService;
import com.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.openMocks;

class EnrollmentControllerTest {

    @Mock
    private EnrollmentService enrollmentService;

    @Mock
    private UserService userService;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private EnrollmentController enrollmentController;

    public EnrollmentControllerTest() {
        openMocks(this);
    }

    private Enrollment createEnrollment(Long courseId) {
        Enrollment enrollment = new Enrollment();
        EnrollmentId id = new EnrollmentId();
        id.setCourseID(courseId);
        enrollment.setId(id);
        return enrollment;
    }

    @Test
    void shouldReturnAllEnrollments() {
        List<Enrollment> enrollments = List.of(new Enrollment(), new Enrollment());
        when(enrollmentService.getAllEnrollments()).thenReturn(enrollments);

        ResponseEntity<Collection<Enrollment>> response = enrollmentController.getAllEnrollments();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void shouldReturnEnrollmentsForUserId() {
        Integer userId = 1;
        when(userService.checkUserById(userId)).thenReturn(true);
        when(enrollmentService.getALLEnrollmentsByUserId(userId)).thenReturn(List.of(new Enrollment()));

        ResponseEntity<Collection<Enrollment>> response = enrollmentController.getEnrollmentsByUserId(userId);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldReturnNotFoundForInvalidUserId() {
        when(userService.checkUserById(anyInt())).thenReturn(false);

        ResponseEntity<Collection<Enrollment>> response = enrollmentController.getEnrollmentsByUserId(99);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void shouldReturnNoContentIfUserHasNoEnrollments() {
        when(userService.checkUserById(anyInt())).thenReturn(true);
        when(enrollmentService.getALLEnrollmentsByUserId(anyInt())).thenReturn(Collections.emptyList());

        ResponseEntity<Collection<Enrollment>> response = enrollmentController.getEnrollmentsByUserId(1);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void shouldReturnSelectedCourses() {
        Enrollment enrollment1 = createEnrollment(1L);
        Enrollment enrollment2 = createEnrollment(2L);

        Course course1 = new Course();
        course1.setId(1L);
        course1.setTitle("Math");

        Course course2 = new Course();
        course2.setId(2L);
        course2.setTitle("Physics");

        when(enrollmentService.getALLEnrollmentsByUserId(1)).thenReturn(List.of(enrollment1, enrollment2));
        when(courseService.findById(1L)).thenReturn(Optional.of(course1));
        when(courseService.findById(2L)).thenReturn(Optional.of(course2));

        ResponseEntity<Collection<Course>> response = enrollmentController.getSelected();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void shouldReturnNoContentIfUserHasNoSelection() {
        when(enrollmentService.getALLEnrollmentsByUserId(anyInt())).thenReturn(Collections.emptyList());

        ResponseEntity<Collection<Course>> response = enrollmentController.getSelected();
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void shouldIgnoreMissingCoursesInSelection() {
        Enrollment enrollment1 = createEnrollment(1L);
        Enrollment enrollment2 = createEnrollment(2L);

        Course course1 = new Course();
        course1.setId(1L);
        course1.setTitle("Math");

        when(enrollmentService.getALLEnrollmentsByUserId(1)).thenReturn(List.of(enrollment1, enrollment2));
        when(courseService.findById(1L)).thenReturn(Optional.of(course1));
        when(courseService.findById(2L)).thenReturn(Optional.empty());

        ResponseEntity<Collection<Course>> response = enrollmentController.getSelected();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }
}
