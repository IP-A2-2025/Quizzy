package com.backend.mapper;

import com.backend.dto.AnswerFCDTO;
import com.backend.model.AnswerFC;
import com.backend.repository.FlashcardRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class AnswerFCMapper implements EntityMapper<AnswerFC, AnswerFCDTO> {

    private final FlashcardRepository flashcardRepository;
    private final FlashcardMapper flashcardMapper;

    public AnswerFCMapper(FlashcardRepository flashcardRepository,
                          @Lazy FlashcardMapper flashcardMapper) {
        this.flashcardRepository = flashcardRepository;
        this.flashcardMapper = flashcardMapper;
    }
    @Override
    public AnswerFCDTO toDTO(AnswerFC answer) {
        if (answer == null) return null;

        AnswerFCDTO dto = new AnswerFCDTO();
        dto.setId(answer.getId());
        dto.setOptionText(answer.getOptionText());
        dto.setCorrect(answer.isCorrect());

        if (answer.getFlashcard() != null) {
            dto.setFlashcardId(answer.getFlashcard().getId());
        }

        return dto;
    }

    @Override
    public AnswerFC toEntity(AnswerFCDTO dto) {
        if (dto == null) return null;

        AnswerFC answer = new AnswerFC();
        answer.setId(dto.getId());
        answer.setOptionText(dto.getOptionText());
        answer.setCorrect(dto.isCorrect());

        if (dto.getFlashcardId() != null) {
            flashcardRepository.findById(dto.getFlashcardId())
                    .ifPresent(answer::setFlashcard);
        }

        return answer;
    }
}