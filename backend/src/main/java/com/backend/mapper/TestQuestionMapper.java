package com.backend.mapper;

import com.backend.dto.TestQuestionDTO;
import com.backend.model.TestQuestion;
import com.backend.repository.TestRepository;
import org.springframework.stereotype.Component;

@Component
public class TestQuestionMapper implements EntityMapper<TestQuestion, TestQuestionDTO> {

    private final TestRepository testRepository;

    public TestQuestionMapper(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    public TestQuestionDTO toDTO(TestQuestion question) {
        if (question == null) return null;

        TestQuestionDTO dto = new TestQuestionDTO();
        dto.setId(question.getId());
        dto.setQuestionText(question.getQuestionText());
        dto.setPointValue(question.getPointValue());

        if (question.getTest() != null) {
            dto.setTestId(question.getTest().getId());
        }
        return dto;
    }

    @Override
    public TestQuestion toEntity(TestQuestionDTO dto) {
        if (dto == null) return null;

        TestQuestion question = new TestQuestion();
        question.setId(dto.getId());
        question.setQuestionText(dto.getQuestionText());
        question.setPointValue(dto.getPointValue());

        if (dto.getTestId() != null) {
            testRepository.findById(dto.getTestId())
                    .ifPresent(question::setTest);
        }
        return question;
    }
}