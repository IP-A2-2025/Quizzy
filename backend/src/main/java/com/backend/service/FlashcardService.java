package com.backend.service;

import com.backend.model.Flashcard;
import com.backend.repository.FlashcardRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FlashcardService {

    private final FlashcardRepository flashcardRepository;

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

    public Flashcard updateFlashcard(Long id, Flashcard updatedFlashcard) {
        return flashcardRepository.findById(id).map(flashcard -> {
            flashcard.setQuestion(updatedFlashcard.getQuestion());
            flashcard.setLevel(updatedFlashcard.getLevel());
            flashcard.setLastStudiedAt(updatedFlashcard.getLastStudiedAt());
            flashcard.setQuestionType(updatedFlashcard.getQuestionType());
            flashcard.setUser(updatedFlashcard.getUser());
            flashcard.setMaterial(updatedFlashcard.getMaterial());
            flashcard.setAnswers(updatedFlashcard.getAnswers());
            return flashcardRepository.save(flashcard);
        }).orElseThrow(() -> new RuntimeException("Flashcard not found with id: " + id));
    }

    public void deleteFlashcard(Long id) {
        flashcardRepository.deleteById(id);
    }

    // extra methods based on your repository
    public List<Flashcard> getByUserId(Integer userId) {
        return flashcardRepository.findByUserId(userId);
    }

    public List<Flashcard> getByMaterialId(Long materialId) {
        return flashcardRepository.findByMaterialId(materialId);
    }

    public List<Flashcard> getDueFlashcards(Date date, Integer userId) {
        return flashcardRepository.findDueFlashcards(date, userId);
    }

    public List<Flashcard> getByLevelAndUserId(int level, Integer userId) {
        return flashcardRepository.findByLevelAndUserId(level, userId);
    }

    public List<Flashcard> getByCourseIdAndUserId(Long courseId, Integer userId) {
        return flashcardRepository.findByCourseIdAndUserId(courseId, userId);
    }

    public List<Flashcard> getByQuestionTypeAndUserId(String type, Integer userId) {
        return flashcardRepository.findByQuestionTypeAndUserId(type, userId);
    }
}