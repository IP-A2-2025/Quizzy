package com.backend.controller;

import com.backend.model.Course;
import com.backend.model.User;
import com.backend.repository.UserRepository;
import com.backend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {


    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<Collection<Course>> getAllUsers() {
        System.out.println("dasdas");
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @PostMapping("/selected")
    public ResponseEntity<String> receiveSelectedCourses(@RequestBody List<Course> selectedCourses) {
        courseService.processSelectedCourses(selectedCourses);
        return ResponseEntity.ok("Selected courses received successfully.");
    }

}