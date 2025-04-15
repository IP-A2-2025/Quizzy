package com.backend.repository;

import com.backend.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TestRepository extends JpaRepository<Test, Long> {

    @Query("SELECT t FROM Test t WHERE t.professor.id = :professorId")
    List<Test> findByProfessorId(@Param("professorId") Integer professorId);

    @Query("SELECT t FROM Test t WHERE t.course.id = :courseId")
    List<Test> findByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT t FROM Test t WHERE t.date >= :startDate AND t.date <= :endDate")
    List<Test> findByDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT t FROM Test t WHERE t.date >= CURRENT_DATE ORDER BY t.date ASC")
    List<Test> findUpcomingTests();

    @Query("SELECT t FROM Test t WHERE t.course.id IN (SELECT e.course.id FROM Enrollment e WHERE e.user.id = :studentId)")
    List<Test> findTestsForStudentEnrollments(@Param("studentId") Integer studentId);
}