package com.backend.mapper;

import com.backend.dto.CourseDTO;
import com.backend.model.Course;
import com.backend.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper implements EntityMapper<Course, CourseDTO> {

    private final UserRepository userRepository;

    public CourseMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CourseDTO toDTO(Course course) {
        if (course == null) return null;

        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setSemestru(course.getSemestru());
        if (course.getProfessor() != null) {
            dto.setProfessorId(course.getProfessor().getId());
        }
        return dto;
    }

    @Override
    public Course toEntity(CourseDTO dto) {
        if (dto == null) return null;

        Course course = new Course();
        course.setId(dto.getId());
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setSemestru(dto.getSemestru());

        // Only attempt to set professor if professorId is present
        Integer professorId = dto.getProfessorId();
        if (professorId != null) {
            userRepository.findById(professorId)
                    .ifPresent(course::setProfessor);
        }
        return course;
    }
}