package com.backend.mapper;

import com.backend.dto.TestAnswerDTO;
import com.backend.model.TestAnswer;
import com.backend.repository.TestQuestionRepository;
import org.springframework.stereotype.Component;

@Component
public class TestAnswerMapper implements EntityMapper<TestAnswer, TestAnswerDTO> {

    private final TestQuestionRepository testQuestionRepository;

    public TestAnswerMapper(TestQuestionRepository testQuestionRepository) {
        this.testQuestionRepository = testQuestionRepository;
    }

    @Override
    public TestAnswerDTO toDTO(TestAnswer answer) {
        if (answer == null) return null;

        TestAnswerDTO dto = new TestAnswerDTO();
        dto.setId(answer.getId());
        dto.setOptionText(answer.getOptionText());
        dto.setCorrect(answer.isCorrect());

        if (answer.getTestQuestion() != null) {
            dto.setQuestionId(answer.getTestQuestion().getId());
        }
        return dto;
    }

    @Override
    public TestAnswer toEntity(TestAnswerDTO dto) {
        if (dto == null) return null;

        TestAnswer answer = new TestAnswer();
        answer.setId(dto.getId());
        answer.setOptionText(dto.getOptionText());
        answer.setCorrect(dto.isCorrect());

        if (dto.getQuestionId() != null) {
            testQuestionRepository.findById(dto.getQuestionId())
                    .ifPresent(answer::setTestQuestion);
        }
        return answer;
    }
}