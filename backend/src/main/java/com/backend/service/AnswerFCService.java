package com.backend.service;

import com.backend.model.AnswerFC;
import com.backend.repository.AnswerFCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerFCService {

    private final AnswerFCRepository answerFCRepository;

    @Autowired
    public AnswerFCService(AnswerFCRepository answerFCRepository) {
        this.answerFCRepository = answerFCRepository;
    }

    public List<AnswerFC> getAllAnswers() {
        return answerFCRepository.findAll();
    }

    public AnswerFC getAnswerById(Long id) {
        return answerFCRepository.findById(id)
                .orElse(null);
    }

    public List<AnswerFC> getAnswersByFlashcardId(Long flashcardId) {
        return answerFCRepository.findByFlashcardId(flashcardId);
    }

    public List<AnswerFC> getCorrectAnswersByFlashcardId(Long flashcardId) {
        return answerFCRepository.findCorrectAnswersByFlashcardId(flashcardId);
    }

    public AnswerFC createAnswer(AnswerFC answerFC) {
        return answerFCRepository.save(answerFC);
    }

    public AnswerFC updateAnswer(Long id, AnswerFC updatedAnswer) {
        return answerFCRepository.findById(id)
                .map(existingAnswer -> {
                    existingAnswer.setOptionText(updatedAnswer.getOptionText());
                    existingAnswer.setCorrect(updatedAnswer.isCorrect());
                    existingAnswer.setFlashcard(updatedAnswer.getFlashcard());
                    return answerFCRepository.save(existingAnswer);
                })
                .orElse(null);
    }


    public void deleteAnswer(Long id) {
        answerFCRepository.deleteById(id);
    }
}
