package com.backend.service;

import com.backend.model.Enrollment;
import com.backend.model.EnrollmentId;
import com.backend.model.User;
import com.backend.repository.EnrollmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseService courseService;
    private final UserService userService;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository, CourseService courseService, UserService userService) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseService = courseService;
        this.userService = userService;
    }

    public Collection<Enrollment> getAllEnrollments() {
        return enrollmentRepository.getAllEnrollments();
    }

    public Collection<Enrollment> getALLEnrollmentsByUserId(Integer id) {
        return enrollmentRepository.findByUserId(id);
    }

    public long getEnrollmentCountForUser(Integer userId) {
        return enrollmentRepository.countEnrollmentsByUserId(userId);
    }

    @Transactional
    public boolean addEnrollment(Integer userId, Long courseId) {
        long currentEnrollments = getEnrollmentCountForUser(userId);
        if (currentEnrollments >= 4) {
            return false;
        }
        enrollmentRepository.insertEnrollment(userId, courseId);
        return true;
    }

    public boolean updateEnrollment(Integer userId, Long courseId, Long newCourseId) {
        Optional<Enrollment> existingEnrollment = enrollmentRepository.findById(new EnrollmentId(userId, courseId));
        if (existingEnrollment.isEmpty()) {
            return false;
        }
        if (!courseService.checkCourseById(newCourseId)) {
            return false;
        }
        enrollmentRepository.deleteById(new EnrollmentId(userId, courseId));
        enrollmentRepository.insertEnrollment(userId, newCourseId);
        return true;
    }

    public boolean deleteEnrollment(Integer userId, Long courseId) {
        if( !userService.checkUserById(userId) || !courseService.checkCourseById(courseId) ) {
            return false;
        }
        enrollmentRepository.deleteById(new EnrollmentId(userId, courseId));
        return true;

    }

    public List<User> getUsersByCourseId(Long courseId) {
        List<User> list = new ArrayList<>();
        enrollmentRepository.findByCourseId(courseId).forEach(e -> list.add(e.getUser()));
        return list;
    }

    public void deleteAllEnrollmentsForUser(Integer userId) {
        enrollmentRepository.deleteByUserId(userId);
    }
}
