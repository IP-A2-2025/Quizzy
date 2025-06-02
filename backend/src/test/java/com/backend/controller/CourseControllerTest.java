package com.backend.controller;

import com.backend.dto.CourseDTO;
import com.backend.model.Course;
import com.backend.service.CourseService;
import com.backend.service.EnrollmentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.openMocks;

class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @Mock
    private EnrollmentService enrollmentService;

    @Mock
    private com.backend.repository.FlashcardRepository flashcardRepository;

    @InjectMocks
    private CourseController courseController;

    public CourseControllerTest() {
        openMocks(this);
    }

    private CourseDTO createCourse(Long id, String title) {
        CourseDTO course = new CourseDTO();
        course.setId(id);
        course.setTitle(title);
        return course;
    }

    // Test for creating a new course
    @Test
    void shouldCreateCourseSuccessfully() {
        // Arrange
        CourseDTO courseDTO = createCourse(null, "New Course Title");

        Course course = new Course();
        course.setId(1L);
        course.setTitle("New Course Title");

        when(courseService.createCourse(courseDTO)).thenReturn(course);

        // Act
        ResponseEntity<Course> response = courseController.createCourse(courseDTO);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("New Course Title", response.getBody().getTitle());
    }


    // Test for updating a course
    @Test
    void shouldUpdateCourseIfExists() {
        CourseDTO courseDTO = createCourse(1L, "Updated Course");
        Course updatedCourse = new Course();
        updatedCourse.setId(1L);
        updatedCourse.setTitle("Updated Course");

        when(courseService.updateCourse(1L, courseDTO)).thenReturn(Optional.of(updatedCourse));

        ResponseEntity<Course> response = courseController.updateCourse(1L, courseDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated Course", response.getBody().getTitle());
    }

    // Test for updating a course when it doesn't exist
    @Test
    void shouldReturnNotFoundWhenUpdatingCourseThatDoesNotExist() {
        CourseDTO courseDTO = createCourse(1L, "Updated Course");

        when(courseService.updateCourse(1L, courseDTO)).thenReturn(Optional.empty());

        ResponseEntity<Course> response = courseController.updateCourse(1L, courseDTO);

        assertEquals(404, response.getStatusCodeValue());
    }

    // Test for deleting a course
    @Test
    void shouldDeleteCourseIfExists() {
        when(courseService.deleteCourse(1L)).thenReturn(true);

        ResponseEntity<Void> response = courseController.deleteCourse(1L);

        assertEquals(200, response.getStatusCodeValue());
    }

    // Test for deleting a course that does not exist
    @Test
    void shouldReturnNotFoundWhenDeletingCourseThatDoesNotExist() {
        when(courseService.deleteCourse(1L)).thenReturn(false);

        ResponseEntity<Void> response = courseController.deleteCourse(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    // Test for receiving exactly four selected courses
    @Test
    void shouldAcceptExactlyFourCourses() {
        List<CourseDTO> courses = Arrays.asList(
                createCourse(1L, "Java"), createCourse(2L, "Spring"),
                createCourse(3L, "Docker"), createCourse(4L, "Kubernetes")
        );

        when(courseService.checkCourseById(1L)).thenReturn(true);
        when(courseService.checkCourseById(2L)).thenReturn(true);
        when(courseService.checkCourseById(3L)).thenReturn(true);
        when(courseService.checkCourseById(4L)).thenReturn(true);

        ResponseEntity<String> response = courseController.receiveSelectedCourses(courses);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Selected courses received successfully."));
    }

    // Test for receiving more than four courses
    @Test
    void shouldRejectMoreThanFourCourses() {
        List<CourseDTO> courses = Arrays.asList(
                createCourse(1L, "Java"), createCourse(2L, "Spring"),
                createCourse(3L, "Docker"), createCourse(4L, "Kubernetes"),
                createCourse(5L, "AWS")
        );

        ResponseEntity<String> response = courseController.receiveSelectedCourses(courses);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("The user should select exactly four courses"));
    }

    // Test for receiving no courses (empty list)
    @Test
    void shouldRejectEmptySelection() {
        List<CourseDTO> courses = new ArrayList<>();

        ResponseEntity<String> response = courseController.receiveSelectedCourses(courses);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("The user should select exactly four courses"));
    }

    // Test for receiving invalid courses (course not found in database)
    @Test
    void shouldRejectInvalidCourse() {
        List<CourseDTO> courses = Arrays.asList(
                createCourse(1L, "Java"), createCourse(2L, "Spring"),
                createCourse(3L, "Invalid"), createCourse(4L, "Kubernetes")
        );

        when(courseService.checkCourseById(1L)).thenReturn(true);
        when(courseService.checkCourseById(2L)).thenReturn(true);
        when(courseService.checkCourseById(3L)).thenReturn(false); // invalid course
        when(courseService.checkCourseById(4L)).thenReturn(true);

        ResponseEntity<String> response = courseController.receiveSelectedCourses(courses);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Couldn't find course"));
    }

    // Test for retrieving a course by ID when it exists
    @Test
    void shouldReturnCourseByIdIfExists() {
        Course course = new Course();
        course.setId(1L);
        when(courseService.findById(1L)).thenReturn(Optional.of(course));

        ResponseEntity<Course> response = courseController.getCourseById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
    }

    // Test for retrieving a course by ID when it does not exist
    @Test
    void shouldReturnNotFoundIfCourseDoesNotExist() {
        when(courseService.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Course> response = courseController.getCourseById(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    // Test for retrieving all courses
    @Test
    void shouldReturnAllCourses() {
        // Arrange
        Course course1 = new Course();
        course1.setId(1L);
        course1.setTitle("Course 1");
        course1.setDescription("Description 1");
        course1.setSemestru("1");
        
        Course course2 = new Course();
        course2.setId(2L);
        course2.setTitle("Course 2");
        course2.setDescription("Description 2");
        course2.setSemestru("2");
        
        List<Course> courses = Arrays.asList(course1, course2);

        when(courseService.getAllCourses()).thenReturn(courses);
        when(flashcardRepository.countByCourseId(1L)).thenReturn(5L);
        when(flashcardRepository.countByCourseId(2L)).thenReturn(10L);

        // Act
        ResponseEntity<List<Map<String, Object>>> response = courseController.getAllCourses();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        
        Map<String, Object> courseMap1 = response.getBody().get(0);
        assertEquals(1L, courseMap1.get("id"));
        assertEquals("Course 1", courseMap1.get("title"));
        assertEquals("Description 1", courseMap1.get("description"));
        assertEquals("1", courseMap1.get("semestru"));
        assertEquals(5L, courseMap1.get("flashcardCount"));
        
        Map<String, Object> courseMap2 = response.getBody().get(1);
        assertEquals(2L, courseMap2.get("id"));
        assertEquals("Course 2", courseMap2.get("title"));
        assertEquals("Description 2", courseMap2.get("description"));
        assertEquals("2", courseMap2.get("semestru"));
        assertEquals(10L, courseMap2.get("flashcardCount"));
    }

    // Test for retrieving all courses when no courses exist
    @Test
    void shouldReturnEmptyListIfNoCourses() {
        when(courseService.getAllCourses()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Map<String, Object>>> response = courseController.getAllCourses();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, response.getBody().size());
    }
}