package com.backend.mapper;

import com.backend.dto.MaterialDTO;
import com.backend.model.Material;
import com.backend.repository.CourseRepository;
import org.springframework.stereotype.Component;

@Component
public class MaterialMapper implements EntityMapper<Material, MaterialDTO> {

    private final CourseRepository courseRepository;

    public MaterialMapper(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public MaterialDTO toDTO(Material material) {
        if (material == null) return null;

        MaterialDTO dto = new MaterialDTO();
        dto.setId(material.getId());
        dto.setName(material.getName());
        dto.setPath(material.getPath());
        if (material.getCourse() != null) {
            dto.setCourseId(material.getCourse().getId());
        }
        return dto;
    }

    @Override
    public Material toEntity(MaterialDTO dto) {
        if (dto == null) return null;

        Material material = new Material();
        material.setId(dto.getId());
        material.setName(dto.getName());
        material.setPath(dto.getPath());

        if (dto.getCourseId() != null) {
            courseRepository.findById(dto.getCourseId())
                    .ifPresent(material::setCourse);
        }
        return material;
    }
}