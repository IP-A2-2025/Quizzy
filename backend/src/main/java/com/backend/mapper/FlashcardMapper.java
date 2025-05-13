package com.backend.mapper;

import com.backend.dto.FlashcardDTO;
import com.backend.model.Flashcard;
import com.backend.repository.MaterialRepository;
import com.backend.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;
import java.util.HashSet;

@Component
public class FlashcardMapper implements EntityMapper<Flashcard, FlashcardDTO> {

    private final UserRepository userRepository;
    private final MaterialRepository materialRepository;
    private final AnswerFCMapper answerFCMapper;

    public FlashcardMapper(UserRepository userRepository,
                           MaterialRepository materialRepository,
                           @Lazy AnswerFCMapper answerFCMapper) {
        this.userRepository = userRepository;
        this.materialRepository = materialRepository;
        this.answerFCMapper = answerFCMapper;
    }

    @Override
    public FlashcardDTO toDTO(Flashcard flashcard) {
        if (flashcard == null) return null;

        FlashcardDTO dto = new FlashcardDTO();
        dto.setId(flashcard.getId());
        dto.setQuestion(flashcard.getQuestion());
        dto.setLevel(flashcard.getLevel());
        dto.setLastStudiedAt(flashcard.getLastStudiedAt());
        dto.setQuestionType(flashcard.getQuestionType());
        dto.setPageIndex(flashcard.getPageIndex());

        if (flashcard.getUser() != null) {
            dto.setUserId(flashcard.getUser().getId());
        }

        if (flashcard.getMaterial() != null) {
            dto.setMaterialId(flashcard.getMaterial().getId());
        }

        if (flashcard.getAnswers() != null && !flashcard.getAnswers().isEmpty()) {
            dto.setAnswers(flashcard.getAnswers().stream()
                    .map(answerFCMapper::toDTO)
                    .collect(Collectors.toSet()));
        }

        return dto;
    }

    @Override
    public Flashcard toEntity(FlashcardDTO dto) {
        if (dto == null) return null;

        Flashcard flashcard = new Flashcard();
        flashcard.setId(dto.getId());
        flashcard.setQuestion(dto.getQuestion());
        flashcard.setLevel(dto.getLevel());
        flashcard.setLastStudiedAt(dto.getLastStudiedAt());
        flashcard.setQuestionType(dto.getQuestionType());
        flashcard.setPageIndex(dto.getPageIndex());

        if (dto.getUserId() != null) {
            userRepository.findById(dto.getUserId())
                    .ifPresent(flashcard::setUser);
        }

        if (dto.getMaterialId() != null) {
            materialRepository.findById(dto.getMaterialId())
                    .ifPresent(flashcard::setMaterial);
        }

        // Initialize empty set for answers
        flashcard.setAnswers(new HashSet<>());

        return flashcard;
    }
}