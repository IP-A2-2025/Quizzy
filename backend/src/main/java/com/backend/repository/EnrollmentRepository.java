package com.backend.repository;

import com.backend.model.Enrollment;
import com.backend.model.EnrollmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentId> {

    @Query("SELECT e FROM Enrollment e WHERE e.user.id = :userId")
    List<Enrollment> findByUserId(@Param("userId") Integer userId);

    @Query("SELECT e FROM Enrollment e WHERE e.course.id = :courseId")
    List<Enrollment> findByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT e FROM Enrollment e WHERE e.enrollmentDate >= :startDate AND e.enrollmentDate <= :endDate")
    List<Enrollment> findByEnrollmentDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId")
    Long countEnrollmentsByCourseId(@Param("courseId") Long courseId);
}