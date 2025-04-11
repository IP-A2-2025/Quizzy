package com.backend.repository;

import com.backend.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {

    @Query("SELECT m FROM Material m WHERE m.course.id = :courseId")
    List<Material> findByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT m FROM Material m WHERE m.name LIKE %:name%")
    List<Material> findByNameContaining(@Param("name") String name);

    @Query("SELECT m FROM Material m WHERE m.path LIKE %:path%")
    List<Material> findByPathContaining(@Param("path") String path);

    @Query("SELECT m FROM Material m WHERE m.course.professor.id = :professorId")
    List<Material> findByProfessorId(@Param("professorId") Integer professorId);
}