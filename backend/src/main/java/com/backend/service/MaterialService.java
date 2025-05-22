package com.backend.service;

import com.backend.dto.MaterialDTO;
import com.backend.model.Course;
import com.backend.model.Material;
import com.backend.repository.MaterialRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final CourseService courseService;

    public MaterialService(MaterialRepository materialRepository, CourseService courseService) {
        this.materialRepository = materialRepository;
        this.courseService = courseService;
    }

    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    public Material getMaterialById(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material not found with id: " + id));
    }

    public Material createMaterial(MaterialDTO materialDTO) {
        if (materialDTO.getCourseId() == null) {
            throw new IllegalArgumentException("Course ID cannot be null.");
        }
        if (materialDTO.getName() == null || materialDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Material name cannot be empty.");
        }
        if (materialDTO.getPath() == null || materialDTO.getPath().trim().isEmpty()) {
            throw new IllegalArgumentException("Material path cannot be empty.");
        }

        Course course = courseService.getCourseById(materialDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + materialDTO.getCourseId()));

        Material material = new Material();
        material.setId(null);
        material.setName(materialDTO.getName());
        material.setPath(materialDTO.getPath());
        material.setCourse(course);

        return materialRepository.save(material);
    }


    public Material updateMaterial(Long id, Material updated) {
        return materialRepository.findById(id)
                .map(mat -> {
                    mat.setName(updated.getName());
                    mat.setPath(updated.getPath());
                    mat.setCourse(updated.getCourse());
                    return materialRepository.save(mat);
                })
                .orElseThrow(() -> new RuntimeException("Material not found with id: " + id));
    }

    public void deleteMaterial(Long id) {
        materialRepository.deleteById(id);
    }

    // Custom queries
    public List<Material> findByCourseId(Long courseId) {
        return materialRepository.findByCourseId(courseId);
    }

    public List<Material> findByNameContaining(String name) {
        return materialRepository.findByNameContaining(name);
    }

    public List<Material> findByPathContaining(String path) {
        return materialRepository.findByPathContaining(path);
    }

    public List<Material> findByProfessorId(Integer professorId) {
        return materialRepository.findByProfessorId(professorId);
    }

    public Material getMaterialByIndex(Long index) {
        return materialRepository.getReferenceById(index);
    }

    public Material findById(Long index) {
        return materialRepository.findById(index).orElse(null);
    }



}
