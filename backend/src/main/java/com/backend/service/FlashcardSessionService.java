package com.backend.service;

import com.backend.model.FlashcardSession;
import com.backend.repository.FlashcardSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FlashcardSessionService {

    private final FlashcardSessionRepository sessionRepository;

    @Autowired
    public FlashcardSessionService(FlashcardSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public List<FlashcardSession> getAllSessions() {
        return sessionRepository.findAll();
    }

    public List<FlashcardSession> getSessionsByUserId(Integer userId) {
        return sessionRepository.findByUserId(userId);
    }

    public List<FlashcardSession> getSessionsByCourseId(Long courseId) {
        return sessionRepository.findByCourseId(courseId);
    }

    public List<FlashcardSession> getSessionsByDateRangeAndUserId(Date startDate, Date endDate, Integer userId) {
        return sessionRepository.findByDateRangeAndUserId(startDate, endDate, userId);
    }

    public Integer getTotalFlashcardsStudiedSince(Integer userId, Date startDate) {
        return sessionRepository.getTotalFlashcardsStudiedSince(userId, startDate);
    }

    public List<FlashcardSession> getSessionsByUserAndCourse(Integer userId, Long courseId) {
        return sessionRepository.findByUserIdAndCourseId(userId, courseId);
    }

    public FlashcardSession createSession(FlashcardSession session) {
        return sessionRepository.save(session);
    }

    public void deleteSession(Long id) {
        sessionRepository.deleteById(id);
    }

}
