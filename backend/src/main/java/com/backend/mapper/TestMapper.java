package com.backend.mapper;

import com.backend.dto.TestDTO;
import com.backend.model.Test;
import com.backend.repository.CourseRepository;
import com.backend.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class TestMapper implements EntityMapper<Test, TestDTO> {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public TestMapper(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TestDTO toDTO(Test test) {
        if (test == null) return null;

        TestDTO dto = new TestDTO();
        dto.setId(test.getId());
        dto.setTitle(test.getTitle());
        dto.setDescription(test.getDescription());
        dto.setDate(test.getDate());

        if (test.getProfessor() != null) {
            dto.setProfessorId(test.getProfessor().getId());
        }

        if (test.getCourse() != null) {
            dto.setCourseId(test.getCourse().getId());
        }
        return dto;
    }

    @Override
    public Test toEntity(TestDTO dto) {
        if (dto == null) return null;

        Test test = new Test();
        test.setId(dto.getId());
        test.setTitle(dto.getTitle());
        test.setDescription(dto.getDescription());
        test.setDate(dto.getDate());

        if (dto.getProfessorId() != null) {
            userRepository.findById(dto.getProfessorId())
                    .ifPresent(test::setProfessor);
        }

        if (dto.getCourseId() != null) {
            courseRepository.findById(dto.getCourseId())
                    .ifPresent(test::setCourse);
        }
        return test;
    }
}