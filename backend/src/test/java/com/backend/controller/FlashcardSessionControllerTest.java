package com.backend.controller;

import com.backend.model.FlashcardSession;
import com.backend.model.User;
import com.backend.service.FlashcardSessionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class FlashcardSessionControllerTest {

    @Mock
    private FlashcardSessionService sessionService;

    @InjectMocks
    private FlashcardSessionController controller;

    public FlashcardSessionControllerTest() {
        openMocks(this);
    }

    private FlashcardSession createSession(Long id, Integer userId, Long courseId, Date date, int flashcardsStudied) {
        FlashcardSession session = new FlashcardSession();
        session.setId(id);
        session.setTimestamp(date);
        session.setFlashcardCount(flashcardsStudied);


        User user = new User();
        user.setId(userId);
        session.setUser(user);

        // Presupunem că ai și un obiect Course, dacă e cazul îl poți seta aici

        return session;
    }

    @Test
    void shouldReturnAllSessions() {
        List<FlashcardSession> sessions = Arrays.asList(new FlashcardSession(), new FlashcardSession());
        when(sessionService.getAllSessions()).thenReturn(sessions);

        ResponseEntity<List<FlashcardSession>> response = controller.getAllSessions();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void shouldReturnSessionsByUserId() {
        when(sessionService.getSessionsByUserId(1)).thenReturn(List.of(new FlashcardSession(), new FlashcardSession()));

        ResponseEntity<List<FlashcardSession>> response = controller.getByUser(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void shouldReturnSessionsByCourseId() {
        when(sessionService.getSessionsByCourseId(10L)).thenReturn(List.of(new FlashcardSession()));

        ResponseEntity<List<FlashcardSession>> response = controller.getByCourse(10L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnSessionsByUserAndCourse() {
        when(sessionService.getSessionsByUserAndCourse(1, 10L)).thenReturn(List.of(new FlashcardSession()));

        ResponseEntity<List<FlashcardSession>> response = controller.getByUserAndCourse(1, 10L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnSessionsByDateRangeAndUserId() {
        Date start = new Date(System.currentTimeMillis() - 86400000L);
        Date end = new Date();
        when(sessionService.getSessionsByDateRangeAndUserId(start, end, 1)).thenReturn(List.of(new FlashcardSession()));

        ResponseEntity<List<FlashcardSession>> response = controller.getByDateRangeAndUser(1, start, end);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnTotalFlashcardsStudiedSince() {
        int userId = 1;
        Date since = new Date(); // sau poți pune o dată fixă pentru un test mai stabil

        when(sessionService.getTotalFlashcardsStudiedSince(userId, since)).thenReturn(42);

        ResponseEntity<Integer> response = controller.getTotalFlashcardsStudiedSince(userId, since);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(42, response.getBody());
    }


    @Test
    void shouldCreateSessionSuccessfully() {
        FlashcardSession toCreate = createSession(null, 1, 10L, new Date(), 15);
        FlashcardSession created = createSession(1L, 1, 10L, new Date(), 15);

        when(sessionService.createSession(toCreate)).thenReturn(created);

        ResponseEntity<FlashcardSession> response = controller.createSession(toCreate);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void shouldDeleteSessionSuccessfully() {
        doNothing().when(sessionService).deleteSession(1L);

        ResponseEntity<Void> response = controller.deleteSession(1L);

        assertEquals(200, response.getStatusCodeValue());
    }
}
