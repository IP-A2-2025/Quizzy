package com.backend.service;

import com.backend.model.Material;
import com.backend.repository.MaterialRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    public Material getMaterialById(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material not found with id: " + id));
    }

    public Material createMaterial(Material material) {
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



}
