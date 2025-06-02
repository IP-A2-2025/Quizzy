package com.backend.service;

import com.backend.model.Grade;
import com.backend.model.GradeId;
import com.backend.repository.GradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;

    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public Grade addGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    public Optional<Grade> getGrade(Long testId, Integer userId) {
        GradeId gradeId = new GradeId(testId, userId);
        return gradeRepository.findById(gradeId);
    }

    public Optional<Grade> updateGrade(Long testId, Integer userId, Grade updatedData) {
        GradeId gradeId = new GradeId(testId, userId);

        return gradeRepository.findById(gradeId).map(existingGrade -> {
            if (updatedData.getGrade() != 0) {
                existingGrade.setGrade(updatedData.getGrade());
            }
            if (updatedData.getSubmissionDate() != null) {
                existingGrade.setSubmissionDate(updatedData.getSubmissionDate());
            }
            return gradeRepository.save(existingGrade);
        });
    }

    public void deleteGrade(Long testId, Integer userId) {
        GradeId gradeId = new GradeId(testId, userId);
        gradeRepository.findById(gradeId).ifPresent(gradeRepository::delete);
    }

    public List<Grade> getGradesByUserId(Integer userId) {
        return gradeRepository.findByUserId(userId);
    }

    public double calculateAverageForTest(Long testId) {
        return gradeRepository.findAverageGradeByTestId(testId).orElse(0.0);
    }

    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }
}
