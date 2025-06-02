package com.backend.mapper;

import com.backend.dto.EnrollmentDTO;
import com.backend.model.Enrollment;
import com.backend.model.EnrollmentId;
import com.backend.repository.CourseRepository;
import com.backend.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentMapper implements EntityMapper<Enrollment, EnrollmentDTO> {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public EnrollmentMapper(UserRepository userRepository, CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public EnrollmentDTO toDTO(Enrollment enrollment) {
        if (enrollment == null) return null;

        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setUserId(enrollment.getId().getUserID());
        dto.setCourseId(enrollment.getId().getCourseID());
        dto.setEnrollmentDate(enrollment.getEnrollmentDate());
        dto.setGrade(enrollment.getGrade());
        return dto;
    }

    @Override
    public Enrollment toEntity(EnrollmentDTO dto) {
        if (dto == null) return null;

        Enrollment enrollment = new Enrollment();
        EnrollmentId id = new EnrollmentId();
        id.setUserID(dto.getUserId());
        id.setCourseID(dto.getCourseId());
        enrollment.setId(id);
        enrollment.setEnrollmentDate(dto.getEnrollmentDate());
        enrollment.setGrade(dto.getGrade());

        userRepository.findById(dto.getUserId().intValue())
                .ifPresent(enrollment::setUser);
        courseRepository.findById(dto.getCourseId())
                .ifPresent(enrollment::setCourse);

        return enrollment;
    }
}