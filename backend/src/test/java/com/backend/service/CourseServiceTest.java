package com.backend.service;

import com.backend.dto.CourseDTO;
import com.backend.model.Course;
import com.backend.model.User;
import com.backend.repository.CourseRepository;
import com.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CourseService courseService;

    private Course createSampleCourse() {
        Course course = new Course();
        course.setId(1L);
        course.setTitle("Test Course");
        course.setDescription("Test Description");
        course.setSemestru("1");
        return course;
    }

    private CourseDTO createSampleCourseDTO() {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setTitle("Test Course DTO");
        courseDTO.setDescription("Test Description DTO");
        courseDTO.setSemestru("2");
        courseDTO.setProfessorId(1);
        return courseDTO;
    }

    private User createSampleUser() {
        User user = new User();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setRole("profesor");
        return user;
    }

    @Test
    void shouldCreateCourseSuccessfully() {
        // Given
        CourseDTO courseDTO = createSampleCourseDTO();
        Course savedCourse = createSampleCourse();
        User professor = createSampleUser();

        when(userRepository.findById(1)).thenReturn(Optional.of(professor));
        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);

        // When
        Course result = courseService.createCourse(courseDTO);

        // Then
        assertNotNull(result);
        assertEquals(savedCourse.getId(), result.getId());
        assertEquals(savedCourse.getTitle(), result.getTitle());
        verify(courseRepository).save(any(Course.class));
        verify(userRepository).findById(1);
        assertNull(courseDTO.getId()); // Should be set to null
    }

    @Test
    void shouldCreateCourseWithoutProfessor() {
        // Given
        CourseDTO courseDTO = createSampleCourseDTO();
        courseDTO.setProfessorId(null);
        Course savedCourse = createSampleCourse();

        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);

        // When
        Course result = courseService.createCourse(courseDTO);

        // Then
        assertNotNull(result);
        assertEquals(savedCourse.getId(), result.getId());
        verify(courseRepository).save(any(Course.class));
        verify(userRepository, never()).findById(any());
    }

    @Test
    void shouldGetAllCourses() {
        // Given
        List<Course> expectedCourses = Arrays.asList(
            createSampleCourse(),
            createSampleCourse()
        );
        when(courseRepository.allCourses()).thenReturn(expectedCourses);

        // When
        Collection<Course> result = courseService.getAllCourses();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(courseRepository).allCourses();
    }

    @Test
    void shouldFindCourseById() {
        // Given
        Long courseId = 1L;
        Course expectedCourse = createSampleCourse();
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(expectedCourse));

        // When
        Optional<Course> result = courseService.findById(courseId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(expectedCourse.getId(), result.get().getId());
        verify(courseRepository).findById(courseId);
    }

    @Test
    void shouldReturnEmptyWhenCourseNotFoundById() {
        // Given
        Long courseId = 999L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // When
        Optional<Course> result = courseService.findById(courseId);

        // Then
        assertFalse(result.isPresent());
        verify(courseRepository).findById(courseId);
    }

    @Test
    void shouldCheckCourseExistsById() {
        // Given
        Long courseId = 1L;
        when(courseRepository.existsById(courseId)).thenReturn(true);

        // When
        boolean result = courseService.checkCourseById(courseId);

        // Then
        assertTrue(result);
        verify(courseRepository).existsById(courseId);
    }

    @Test
    void shouldReturnFalseWhenCourseDoesNotExist() {
        // Given
        Long courseId = 999L;
        when(courseRepository.existsById(courseId)).thenReturn(false);

        // When
        boolean result = courseService.checkCourseById(courseId);

        // Then
        assertFalse(result);
        verify(courseRepository).existsById(courseId);
    }

    @Test
    void shouldUpdateCourseSuccessfully() {
        // Given
        Long courseId = 1L;
        CourseDTO updateDTO = createSampleCourseDTO();
        Course existingCourse = createSampleCourse();
        User professor = createSampleUser();

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(existingCourse));
        when(userRepository.findById(updateDTO.getProfessorId())).thenReturn(Optional.of(professor));
        when(courseRepository.save(any(Course.class))).thenReturn(existingCourse);

        // When
        Optional<Course> result = courseService.updateCourse(courseId, updateDTO);

        // Then
        assertTrue(result.isPresent());
        assertEquals(updateDTO.getTitle(), existingCourse.getTitle());
        assertEquals(updateDTO.getDescription(), existingCourse.getDescription());
        assertEquals(updateDTO.getSemestru(), existingCourse.getSemestru());
        verify(courseRepository).save(existingCourse);
    }

    @Test
    void shouldReturnEmptyWhenUpdatingNonExistentCourse() {
        // Given
        Long courseId = 999L;
        CourseDTO updateDTO = createSampleCourseDTO();
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // When
        Optional<Course> result = courseService.updateCourse(courseId, updateDTO);

        // Then
        assertFalse(result.isPresent());
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void shouldUpdateCourseWithoutProfessor() {
        // Given
        Long courseId = 1L;
        CourseDTO updateDTO = createSampleCourseDTO();
        updateDTO.setProfessorId(null);
        Course existingCourse = createSampleCourse();

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.save(any(Course.class))).thenReturn(existingCourse);

        // When
        Optional<Course> result = courseService.updateCourse(courseId, updateDTO);

        // Then
        assertTrue(result.isPresent());
        verify(userRepository, never()).findById(any());
        verify(courseRepository).save(existingCourse);
    }

    @Test
    void shouldDeleteCourseSuccessfully() {
        // Given
        Long courseId = 1L;
        when(courseRepository.existsById(courseId)).thenReturn(true);

        // When
        boolean result = courseService.deleteCourse(courseId);

        // Then
        assertTrue(result);
        verify(courseRepository).deleteById(courseId);
    }

    @Test
    void shouldReturnFalseWhenDeletingNonExistentCourse() {
        // Given
        Long courseId = 999L;
        when(courseRepository.existsById(courseId)).thenReturn(false);

        // When
        boolean result = courseService.deleteCourse(courseId);

        // Then
        assertFalse(result);
        verify(courseRepository, never()).deleteById(any());
    }

    @Test
    void shouldFindCourseByTitle() {
        // Given
        String title = "Test Course";
        Course expectedCourse = createSampleCourse();
        when(courseRepository.findByTitle(title)).thenReturn(expectedCourse);

        // When
        Course result = courseService.findByTitle(title);

        // Then
        assertNotNull(result);
        assertEquals(expectedCourse.getTitle(), result.getTitle());
        verify(courseRepository).findByTitle(title);
    }

    @Test
    void shouldGetCourseById() {
        // Given
        Long courseId = 1L;
        Course expectedCourse = createSampleCourse();
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(expectedCourse));

        // When
        Optional<Course> result = courseService.getCourseById(courseId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(expectedCourse.getId(), result.get().getId());
        verify(courseRepository).findById(courseId);
    }

    @Test
    void shouldGetEnrolledCoursesByStudentId() {
        // Given
        Integer studentId = 1;
        List<Course> expectedCourses = Arrays.asList(
            createSampleCourse(),
            createSampleCourse()
        );
        when(courseRepository.findEnrolledCoursesByStudentId(studentId)).thenReturn(expectedCourses);

        // When
        List<Course> result = courseService.getEnrolledCoursesByStudentId(studentId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(courseRepository).findEnrolledCoursesByStudentId(studentId);
    }

    @Test
    void shouldReturnEmptyListWhenNoEnrolledCourses() {
        // Given
        Integer studentId = 999;
        when(courseRepository.findEnrolledCoursesByStudentId(studentId)).thenReturn(Collections.emptyList());

        // When
        List<Course> result = courseService.getEnrolledCoursesByStudentId(studentId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(courseRepository).findEnrolledCoursesByStudentId(studentId);
    }

    @Test
    void shouldHandleNullCourseDTO() {
        // When & Then
        assertThrows(NullPointerException.class, () -> courseService.createCourse(null));
    }

    @Test
    void shouldHandleNullCourseId() {
        // When
        Optional<Course> result = courseService.findById(null);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void shouldHandleNullTitle() {
        // When
        Course result = courseService.findByTitle(null);

        // Then
        verify(courseRepository).findByTitle(null);
    }

    @Test
    void shouldCreateCourseWithNonExistentProfessor() {
        // Given
        CourseDTO courseDTO = createSampleCourseDTO();
        courseDTO.setProfessorId(999); // Non-existent professor
        Course savedCourse = createSampleCourse();

        when(userRepository.findById(999)).thenReturn(Optional.empty());
        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);

        // When
        Course result = courseService.createCourse(courseDTO);

        // Then
        assertNotNull(result);
        verify(courseRepository).save(any(Course.class));
        verify(userRepository).findById(999);
    }

    @Test
    void shouldUpdateCourseWithNonExistentProfessor() {
        // Given
        Long courseId = 1L;
        CourseDTO updateDTO = createSampleCourseDTO();
        updateDTO.setProfessorId(999); // Non-existent professor
        Course existingCourse = createSampleCourse();

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(existingCourse));
        when(userRepository.findById(999)).thenReturn(Optional.empty());
        when(courseRepository.save(any(Course.class))).thenReturn(existingCourse);

        // When
        Optional<Course> result = courseService.updateCourse(courseId, updateDTO);

        // Then
        assertTrue(result.isPresent());
        verify(courseRepository).save(existingCourse);
        verify(userRepository).findById(999);
    }
} 