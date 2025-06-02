package com.backend.repository;

import com.backend.model.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TestRepository extends JpaRepository<TestEntity, Long> {

    @Query("SELECT t FROM TestEntity t WHERE t.professor.id = :professorId")
    List<TestEntity> findByProfessorId(@Param("professorId") Integer professorId);

    @Query("SELECT t FROM TestEntity t WHERE t.course.id = :courseId")
    List<TestEntity> findByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT t FROM TestEntity t WHERE t.date >= :startDate AND t.date <= :endDate")
    List<TestEntity> findByDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT t FROM TestEntity t WHERE t.date >= CURRENT_DATE ORDER BY t.date ASC")
    List<TestEntity> findUpcomingTests();

    @Query("SELECT t FROM TestEntity t WHERE t.course.id IN (SELECT e.course.id FROM Enrollment e WHERE e.user.id = :studentId)")
    List<TestEntity> findTestsForStudentEnrollments(@Param("studentId") Integer studentId);

    @Query("SELECT t FROM TestEntity t WHERE t.date = :date")
    List<TestEntity> findTestsByExactDate(@Param("date") Date date);

    @Query("SELECT COUNT(t) FROM TestEntity t WHERE t.professor.id = :professorId")
    Long countTestsByProfessor(@Param("professorId") int professorId);

    @Query("SELECT COUNT(t) FROM TestEntity t WHERE t.course.id = :courseId")
    Long countTestsByCourse(@Param("courseId") int courseId);

    @Query("SELECT COUNT(t) FROM TestEntity t WHERE t.date >= :startDate AND t.date <= :endDate")
    Long countTestsByDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT COUNT(t) FROM TestEntity t WHERE t.date >= CURRENT_DATE ORDER BY t.date ASC")
    Long countUpcomingTests();

    @Query("SELECT COUNT(t) FROM TestEntity t WHERE t.course.id IN (SELECT e.course.id FROM Enrollment e WHERE e.user.id = :studentId)")
    Long countTestsForStudentEnrollments(@Param("studentId") Integer studentId);

    @Query("SELECT COUNT(t) FROM TestEntity t WHERE t.course.id IN " +
            "(SELECT e.course.id FROM Enrollment e WHERE e.user.id = :studentId)")
    Long countTestsForStudent(@Param("studentId") Integer studentId);

    @Query("SELECT t FROM TestEntity t WHERE FUNCTION('MONTH', t.date) = :month AND FUNCTION('YEAR', t.date) = :year")
    List<TestEntity> findByMonthAndYear(@Param("month") int month, @Param("year") int year);

    @Query("SELECT t FROM TestEntity t WHERE FUNCTION('MONTH', t.date) = :month")
    List<TestEntity> findByMonth(@Param("month") int month);

    @Query("SELECT t FROM TestEntity t WHERE FUNCTION('YEAR', t.date) = :year")
    List<TestEntity> findByYear(@Param("year") int year);

    @Query("SELECT t FROM TestEntity t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<TestEntity> findByTitle(@Param("keyword") String keyword);

    @Query("SELECT t FROM TestEntity t WHERE LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<TestEntity> findByDescription(@Param("keyword") String keyword);
}