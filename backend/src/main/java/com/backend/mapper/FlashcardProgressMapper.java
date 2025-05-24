package com.backend.mapper;

import com.backend.dto.FlashcardProgressDTO;
import com.backend.model.FlashcardProgress;
import com.backend.repository.FlashcardRepository;
import com.backend.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class FlashcardProgressMapper implements EntityMapper<FlashcardProgress, FlashcardProgressDTO> {

    private final UserRepository userRepository;
    private final FlashcardRepository flashcardRepository;

    public FlashcardProgressMapper(UserRepository userRepository,
                                   FlashcardRepository flashcardRepository) {
        this.userRepository = userRepository;
        this.flashcardRepository = flashcardRepository;
    }

    @Override
    public FlashcardProgressDTO toDTO(FlashcardProgress flashcardProgress) {
        if (flashcardProgress == null) return null;

        FlashcardProgressDTO dto = new FlashcardProgressDTO();
        dto.setId(flashcardProgress.getId());

        if (flashcardProgress.getUser() != null) {
            dto.setUserId(flashcardProgress.getUser().getId());
        }

        if (flashcardProgress.getFlashcard() != null) {
            dto.setFlashcardId(flashcardProgress.getFlashcard().getId());
        }

        dto.setEaseFactor(flashcardProgress.getEaseFactor());
        dto.setInterval(flashcardProgress.getInterval());
        dto.setRepetitions(flashcardProgress.getRepetitions());
        dto.setDueDate(flashcardProgress.getDueDate());
        dto.setLastReviewed(flashcardProgress.getLastReviewed());

        return dto;
    }

    @Override
    public FlashcardProgress toEntity(FlashcardProgressDTO dto) {
        if (dto == null) return null;

        FlashcardProgress progress = new FlashcardProgress();
        progress.setId(dto.getId());

        if (dto.getUserId() != null) {
            userRepository.findById(dto.getUserId())
                    .ifPresent(progress::setUser);
        }

        if (dto.getFlashcardId() != null) {
            flashcardRepository.findById(dto.getFlashcardId())
                    .ifPresent(progress::setFlashcard);
        }

        progress.setEaseFactor(dto.getEaseFactor());
        progress.setInterval(dto.getInterval());
        progress.setRepetitions(dto.getRepetitions());
        progress.setDueDate(dto.getDueDate());
        progress.setLastReviewed(dto.getLastReviewed());

        return progress;
    }
}
