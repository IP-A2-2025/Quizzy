package com.backend.service;

import com.backend.model.Course;
import com.backend.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Collection<Course> getAllCourses() {
        return courseRepository.allCourses();
    }

    public void processSelectedCourses(List<Course> selectedCourses) {
        for(Course c : selectedCourses) {
            //courseRepository.setCourse((long) 1,(long) c.getId());
            System.out.println(c.toString());
        }
    }
}