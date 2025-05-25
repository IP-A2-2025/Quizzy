package com.backend.controller;

import com.backend.model.Course;
import com.backend.model.Enrollment;
import com.backend.model.User;
import com.backend.repository.CourseRepository;
import com.backend.repository.FlashcardRepository;
import com.backend.service.CourseService;
import com.backend.service.EnrollmentService;
import com.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.foreign.MemorySegment;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final UserService userService;
    private final CourseService courseService;
    private final FlashcardRepository flashcardRepository;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService,UserService userService, CourseService courseService, FlashcardRepository flashcardRepository) { this.enrollmentService = enrollmentService; this.userService = userService; this.courseService = courseService; this.flashcardRepository = flashcardRepository; }

    @GetMapping
    public ResponseEntity<Collection<Enrollment>> getAllEnrollments(){
        return ResponseEntity.ok(enrollmentService.getAllEnrollments());
    }

    @PostMapping
    public ResponseEntity<String> createEnrollment(@RequestParam Integer userId, @RequestParam Long courseId) {
        if (!userService.checkUserById(userId) || !courseService.checkCourseById(courseId)) {
            return ResponseEntity.badRequest().body("Invalid user or course ID.");
        }
        boolean enrolled = enrollmentService.addEnrollment(userId, courseId);
        if (!enrolled) {
            return ResponseEntity.badRequest().body("You have reached the maximum limit of 4 enrolled courses.");
        }
        return ResponseEntity.ok("Enrollment created successfully.");
    }

    @PutMapping("/{userId}/course/{courseId}")
    public ResponseEntity<String> updateEnrollment(@PathVariable Integer userId, @PathVariable Long courseId, @RequestParam Long newCourseId) {
        if (!userService.checkUserById(userId) || !courseService.checkCourseById(newCourseId)) {
            return ResponseEntity.badRequest().body("Invalid user or course ID.");
        }
        boolean updated = enrollmentService.updateEnrollment(userId, courseId, newCourseId);
        return updated ? ResponseEntity.ok("Enrollment updated successfully.") : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{userId}/course/{courseId}")
    public ResponseEntity<String> deleteEnrollment(@PathVariable Integer userId, @PathVariable Long courseId) {
        boolean deleted = enrollmentService.deleteEnrollment(userId, courseId);
        return deleted ? ResponseEntity.ok("Enrollment deleted successfully.") : ResponseEntity.notFound().build();
    }

    @GetMapping("/course/{courseId}/users")
    public ResponseEntity<Collection<User>> getUsersByCourseId(@PathVariable Long courseId) {
        if (!courseService.checkCourseById(courseId)) {
            return ResponseEntity.notFound().build();
        }
        List<User> users = enrollmentService.getUsersByCourseId(courseId);
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteAllEnrollmentsForUser(@PathVariable Integer userId) {
        if (!userService.checkUserById(userId)) {
            return ResponseEntity.notFound().build();
        }
        enrollmentService.deleteAllEnrollmentsForUser(userId);
        return ResponseEntity.ok("All enrollments for user deleted successfully.");
    }

    @GetMapping("/my_selection")
    public ResponseEntity<Collection<Course>> getSelected() {
        Integer currentId = 1;

        Collection<Enrollment> foundEnrollments = enrollmentService.getALLEnrollmentsByUserId(currentId);
        if (foundEnrollments.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        List<Course> list = new ArrayList<>();

        for( Enrollment enrollment : foundEnrollments ) {
            Optional<Course> course = courseService.findById(enrollment.getId().getCourseID());
            course.ifPresent(list::add);
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/user_{id}")
    public ResponseEntity<Collection<Enrollment>> getEnrollmentsByUserId(@PathVariable Integer id) {
        if(!userService.checkUserById(id)){
            return ResponseEntity.notFound().build();
        }
        Collection<Enrollment> foundEnrollments = enrollmentService.getALLEnrollmentsByUserId(id);
        if (foundEnrollments.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(foundEnrollments);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Map<String, Object>>> getEnrolledCoursesByStudent(@PathVariable Integer studentId) {
        List<Course> courses = courseService.getEnrolledCoursesByStudentId(studentId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Course c : courses) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", c.getId());
            map.put("title", c.getTitle());
            map.put("description", c.getDescription());
            map.put("semestru", c.getSemestru());
            map.put("flashcardCount", flashcardRepository.countByCourseId(c.getId()));
            map.put("materials", c.getMaterials());
            result.add(map);
        }
        return ResponseEntity.ok(result);
    }

}
