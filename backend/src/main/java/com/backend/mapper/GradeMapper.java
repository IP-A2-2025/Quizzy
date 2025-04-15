package com.backend.mapper;

import com.backend.dto.GradeDTO;
import com.backend.model.Grade;
import com.backend.model.GradeId;
import com.backend.repository.TestRepository;
import com.backend.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class GradeMapper implements EntityMapper<Grade, GradeDTO> {

    private final TestRepository testRepository;
    private final UserRepository userRepository;

    public GradeMapper(TestRepository testRepository, UserRepository userRepository) {
        this.testRepository = testRepository;
        this.userRepository = userRepository;
    }

    public GradeDTO toDTO(Grade grade) {
        if (grade == null) return null;

        GradeDTO dto = new GradeDTO();
        dto.setTestId(grade.getId().getTestID());
        dto.setUserId(grade.getId().getUserID());
        dto.setGrade(grade.getGrade());
        dto.setSubmissionDate(grade.getSubmissionDate());

        return dto;
    }

    public Grade toEntity(GradeDTO dto) {
        if (dto == null) return null;

        Grade grade = new Grade();
        GradeId id = new GradeId();
        id.setTestID(dto.getTestId());
        id.setUserID(dto.getUserId());
        grade.setId(id);
        grade.setGrade(dto.getGrade());
        grade.setSubmissionDate(dto.getSubmissionDate());

        testRepository.findById(dto.getTestId())
                .ifPresent(grade::setTest);
        userRepository.findById(dto.getUserId())
                .ifPresent(grade::setUser);

        return grade;
    }
}