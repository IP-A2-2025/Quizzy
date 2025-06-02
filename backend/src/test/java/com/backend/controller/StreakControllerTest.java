package com.backend.controller;

import com.backend.model.Streak;
import com.backend.model.User;
import com.backend.service.StreakService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class StreakControllerTest {

    @Mock
    private StreakService streakService;

    @InjectMocks
    private StreakController streakController;

    public StreakControllerTest() {
        openMocks(this);
    }

    private Streak createStreak(Long id, Integer userId, Date date, int streakCount) {
        Streak streak = new Streak();
        streak.setId(id);
        streak.setCurrentStreak(streakCount);
        streak.setLastCompletedDate(date);

        User user = new User();
        user.setId(userId);
        streak.setUser(user);

        return streak;
    }


    @Test
    void shouldReturnAllStreaks() {
        List<Streak> streaks = Arrays.asList(new Streak(), new Streak());
        when(streakService.getAllStreaks()).thenReturn(streaks);

        ResponseEntity<List<Streak>> response = streakController.getAllStreaks();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void shouldReturnStreakByIdIfExists() {
        Streak streak = createStreak(1L, 1, new Date(System.currentTimeMillis()), 5);
        when(streakService.getStreakById(1L)).thenReturn(Optional.of(streak));

        ResponseEntity<Streak> response = streakController.getStreakById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void shouldReturnNotFoundIfStreakByIdDoesNotExist() {
        when(streakService.getStreakById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Streak> response = streakController.getStreakById(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void shouldReturnStreaksByUserId() {
        List<Streak> streaks = Arrays.asList(new Streak(), new Streak());
        when(streakService.getStreaksByUserId(1)).thenReturn(streaks);

        ResponseEntity<List<Streak>> response = streakController.getStreaksByUserId(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void shouldReturnStreakByUserIdAndDateIfExists() {
        Date date = new Date(System.currentTimeMillis());
        Streak streak = createStreak(1L, 1, date, 5);
        when(streakService.getStreakByUserIdAndDate(1, date)).thenReturn(Optional.of(streak));

        ResponseEntity<Streak> response = streakController.getStreakByUserIdAndDate(1, date);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(date, response.getBody().getLastCompletedDate());
    }

    @Test
    void shouldReturnNotFoundIfStreakByUserIdAndDateDoesNotExist() {
        Date date = new Date(System.currentTimeMillis());
        when(streakService.getStreakByUserIdAndDate(1, date)).thenReturn(Optional.empty());

        ResponseEntity<Streak> response = streakController.getStreakByUserIdAndDate(1, date);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void shouldReturnStreaksByMinimumStreak() {
        List<Streak> streaks = Arrays.asList(new Streak(), new Streak());
        when(streakService.getStreaksByMinimumStreak(5)).thenReturn(streaks);

        ResponseEntity<List<Streak>> response = streakController.getStreaksByMinimumStreak(5);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void shouldReturnTopStreaks() {
        List<Streak> topStreaks = Arrays.asList(new Streak(), new Streak(), new Streak());
        when(streakService.getTopStreaks()).thenReturn(topStreaks);

        ResponseEntity<List<Streak>> response = streakController.getTopStreaks();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(3, response.getBody().size());
    }

    @Test
    void shouldReturnAverageStreak() {
        when(streakService.getAverageStreak()).thenReturn(5.5f);

        ResponseEntity<Float> response = streakController.getAverageStreak();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(5.5f, response.getBody());
    }

    @Test
    void shouldCreateStreakSuccessfully() {
        Streak streak = createStreak(null, 1, new Date(System.currentTimeMillis()), 3);
        Streak createdStreak = createStreak(1L, 1, new Date(System.currentTimeMillis()), 3);

        when(streakService.createStreak(streak)).thenReturn(createdStreak);

        ResponseEntity<Streak> response = streakController.createStreak(streak);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void shouldDeleteStreakSuccessfully() {
        doNothing().when(streakService).deleteStreak(1L);

        ResponseEntity<Void> response = streakController.deleteStreak(1L);

        assertEquals(200, response.getStatusCodeValue());
    }
}
