package com.backend.service;

import com.backend.model.Course;
import com.backend.model.FlashcardSession;
import com.backend.model.User;
import com.backend.repository.FlashcardSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FlashcardSessionServiceTest {

    private FlashcardSessionRepository repository;
    private FlashcardSessionService service;

    private FlashcardSession session;

    @BeforeEach
    void setUp() {
        repository = mock(FlashcardSessionRepository.class);
        service = new FlashcardSessionService(repository);

        User user = new User();
        user.setId(1);

        Course course = new Course();
        course.setId(2L);

        session = new FlashcardSession();
        session.setId(1L);
        session.setTimestamp(new Date());
        session.setFlashcardCount(5);
        session.setUser(user);
        session.setCourse(course);
    }

    @Test
    void testGetAllSessions() {
        List<FlashcardSession> sessions = List.of(session);
        when(repository.findAll()).thenReturn(sessions);

        List<FlashcardSession> result = service.getAllSessions();

        assertEquals(1, result.size());
        assertEquals(session.getId(), result.get(0).getId());
        verify(repository).findAll();
    }

    @Test
    void testGetSessionsByUserId() {
        when(repository.findByUserId(1)).thenReturn(List.of(session));

        List<FlashcardSession> result = service.getSessionsByUserId(1);

        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getFlashcardCount());
        verify(repository).findByUserId(1);
    }

    @Test
    void testCreateSession() {
        when(repository.save(session)).thenReturn(session);

        FlashcardSession saved = service.createSession(session);

        assertNotNull(saved);
        assertEquals(session.getFlashcardCount(), saved.getFlashcardCount());
        verify(repository).save(session);
    }

    @Test
    void testGetSessionsByDateRangeAndUserId() {
        Date start = new Date(System.currentTimeMillis() - 86400000); // 1 day ago
        Date end = new Date();
        when(repository.findByDateRangeAndUserId(start, end, 1)).thenReturn(List.of(session));

        List<FlashcardSession> result = service.getSessionsByDateRangeAndUserId(start, end, 1);

        assertEquals(1, result.size());
        verify(repository).findByDateRangeAndUserId(start, end, 1);
    }

    @Test
    void testGetTotalFlashcardsStudiedSince() {
        Date start = new Date(System.currentTimeMillis() - 604800000); // 7 days ago
        when(repository.getTotalFlashcardsStudiedSince(1, start)).thenReturn(20);

        Integer total = service.getTotalFlashcardsStudiedSince(1, start);

        assertEquals(20, total);
        verify(repository).getTotalFlashcardsStudiedSince(1, start);
    }

    @Test
    void testGetSessionsByCourseId() {
        when(repository.findByCourseId(2L)).thenReturn(List.of(session));

        List<FlashcardSession> result = service.getSessionsByCourseId(2L);

        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getCourse().getId());
        verify(repository).findByCourseId(2L);
    }

    @Test
    void testGetSessionsByUserIdAndCourseId() {
        when(repository.findByUserIdAndCourseId(1, 2L)).thenReturn(List.of(session));

        List<FlashcardSession> result = service.getSessionsByUserAndCourse(1, 2L);

        assertEquals(1, result.size());
        verify(repository).findByUserIdAndCourseId(1, 2L);
    }
}
