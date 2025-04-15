package com.backend.repository;

import com.backend.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Collection;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c FROM Course c WHERE c.professor.id = :professorId")
    List<Course> findByProfessorId(@Param("professorId") Integer professorId);

    @Query("SELECT c FROM Course c JOIN c.enrollments e WHERE e.user.id = :studentId")
    List<Course> findEnrolledCoursesByStudentId(@Param("studentId") Integer studentId);

    @Query("SELECT c FROM Course c WHERE c.semestru = :semester")
    List<Course> findBySemester(@Param("semester") String semester);

    @Query("SELECT c FROM Course c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Course> searchCourses(@Param("keyword") String keyword);
  
    @Query("SELECT c FROM Course c")
    Collection<Course> allCourses();
}
