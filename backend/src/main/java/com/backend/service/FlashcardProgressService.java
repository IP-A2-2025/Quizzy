package com.backend.service;

import com.backend.model.FlashcardProgress;
import com.backend.repository.FlashcardProgressRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FlashcardProgressService {

    private final FlashcardProgressRepository flashcardProgressRepository;

    public FlashcardProgressService(FlashcardProgressRepository flashcardProgressRepository) {
        this.flashcardProgressRepository = flashcardProgressRepository;
    }

    public List<FlashcardProgress> getAllFlashcardProgress() {
        return flashcardProgressRepository.findAll();
    }

    public Optional<FlashcardProgress> getFlashcardProgressById(Long id) {
        return flashcardProgressRepository.findById(id);
    }

    public FlashcardProgress createFlashcardProgress(FlashcardProgress flashcardProgress) {
        return flashcardProgressRepository.save(flashcardProgress);
    }

    public FlashcardProgress updateFlashcardProgress(Long id, FlashcardProgress updatedProgress) {
        return flashcardProgressRepository.findById(id).map(progress -> {
            progress.setUser(updatedProgress.getUser());
            progress.setFlashcard(updatedProgress.getFlashcard());
            progress.setEaseFactor(updatedProgress.getEaseFactor());
            progress.setInterval(updatedProgress.getInterval());
            progress.setRepetitions(updatedProgress.getRepetitions());
            progress.setDueDate(updatedProgress.getDueDate());
            progress.setLastReviewed(updatedProgress.getLastReviewed());
            return flashcardProgressRepository.save(progress);
        }).orElseThrow(() -> new RuntimeException("FlashcardProgress not found with id: " + id));
    }

    public void deleteFlashcardProgress(Long id) {
        flashcardProgressRepository.deleteById(id);
    }

    public List<FlashcardProgress> getByUserId(Integer userId) {
        return flashcardProgressRepository.findByUserId(userId);
    }

    public List<FlashcardProgress> getByFlashcardId(Long flashcardId) {
        return flashcardProgressRepository.findByFlashcardId(flashcardId);
    }

    public List<FlashcardProgress> getDueProgress(Date date, Integer userId) {
        return flashcardProgressRepository.findDueProgress(date, userId);
    }

    public List<FlashcardProgress> getByUserIdAndMaterialId(Integer userId, Long materialId) {
        return flashcardProgressRepository.findByUserIdAndMaterialId(userId, materialId);
    }

    public List<FlashcardProgress> getByUserIdAndLevel(Integer userId, int level) {
        return flashcardProgressRepository.findByUserIdAndLevel(userId, level);
    }

    public List<FlashcardProgress> getByUserIdAndPageIndex(Integer userId, Integer pageIndex) {
        return flashcardProgressRepository.findByUserIdAndPageIndex(userId, pageIndex);
    }

    public List<FlashcardProgress> getByUserIdAndCourseId(Integer userId, Long courseId) {
        return flashcardProgressRepository.findByUserIdAndCourseId(userId, courseId);
    }

    public List<FlashcardProgress> getByUserIdAndQuestionType(Integer userId, String type) {
        return flashcardProgressRepository.findByUserIdAndQuestionType(userId, type);
    }

    public FlashcardProgress getByFlashcardIdAndUserId(Long flashcardId, Integer userId) {
        return flashcardProgressRepository.findByFlashcardIdAndUserId(flashcardId, userId);
    }
}

