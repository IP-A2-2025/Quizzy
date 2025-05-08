package com.backend.service;

import com.backend.model.Flashcard;
import com.backend.repository.FlashcardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FlashcardService {

    private final FlashcardRepository flashcardRepository;

    @Autowired
    public FlashcardService(FlashcardRepository flashcardRepository) {
        this.flashcardRepository = flashcardRepository;
    }

    public List<Flashcard> getAllFlashcards() {
        return flashcardRepository.findAll();
    }

    public Optional<Flashcard> getFlashcardById(Long id) {
        return flashcardRepository.findById(id);
    }

    public Flashcard createFlashcard(Flashcard flashcard) {
        return flashcardRepository.save(flashcard);
    }

    public boolean deleteFlashcard(Long id) {
        if (!flashcardRepository.existsById(id)) return false;
        flashcardRepository.deleteById(id);
        return true;
    }

    public List<Flashcard> getFlashcardsByUserId(Integer userId) {
        return flashcardRepository.findByUserId(userId);
    }

    public List<Flashcard> getFlashcardsByMaterialId(Long materialId) {
        return flashcardRepository.findByMaterialId(materialId);
    }

    public List<Flashcard> getDueFlashcards(Integer userId, Date date) {
        return flashcardRepository.findDueFlashcards(date, userId);
    }

    public List<Flashcard> getFlashcardsByLevel(Integer userId, int level) {
        return flashcardRepository.findByLevelAndUserId(level, userId);
    }

    public List<Flashcard> getFlashcardsByCourseId(Integer userId, Long courseId) {
        return flashcardRepository.findByCourseIdAndUserId(courseId, userId);
    }

    public List<Flashcard> getFlashcardsByQuestionType(Integer userId, String type) {
        return flashcardRepository.findByQuestionTypeAndUserId(type, userId);
    }
}
