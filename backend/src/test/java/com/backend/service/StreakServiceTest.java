package com.backend.service;

import com.backend.model.Streak;
import com.backend.model.User;
import com.backend.repository.StreakRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StreakServiceTest {

    private StreakRepository repository;
    private StreakService service;
    private Streak streak;
    private User user;

    @BeforeEach
    void setUp() {
        repository = mock(StreakRepository.class);
        service = new StreakService(repository); // folosești constructorul!
        user = new User();
        user.setId(1);

        streak = new Streak();
        streak.setId(1L);
        streak.setCurrentStreak(7);
        streak.setLastCompletedDate(Date.valueOf("2025-04-23"));
        streak.setUser(user);
    }

    @Test
    void testGetAllStreaks() {
        when(repository.findAll()).thenReturn(List.of(streak));

        List<Streak> result = service.getAllStreaks();

        assertEquals(1, result.size());
        verify(repository).findAll();
    }

    @Test
    void testGetStreakById() {
        when(repository.findById(1L)).thenReturn(Optional.of(streak));

        Optional<Streak> result = service.getStreakById(1L);

        assertTrue(result.isPresent());
        assertEquals(7, result.get().getCurrentStreak());
        verify(repository).findById(1L);
    }

    @Test
    void testGetStreaksByUserId() {
        when(repository.findByUserId(1)).thenReturn(List.of(streak));

        List<Streak> result = service.getStreaksByUserId(1);

        assertEquals(1, result.size());
        verify(repository).findByUserId(1);
    }

    @Test
    void testGetStreakByUserIdAndDate() {
        Date date = Date.valueOf("2025-04-23");
        when(repository.findByUserIdAndDate(1, date)).thenReturn(Optional.of(streak));

        Optional<Streak> result = service.getStreakByUserIdAndDate(1, date);

        assertTrue(result.isPresent());
        verify(repository).findByUserIdAndDate(1, date);
    }

    @Test
    void testGetStreaksByMinimumStreak() {
        when(repository.findByMinimumStreak(5)).thenReturn(List.of(streak));

        List<Streak> result = service.getStreaksByMinimumStreak(5);

        assertEquals(1, result.size());
        verify(repository).findByMinimumStreak(5);
    }

    @Test
    void testGetTopStreaks() {
        when(repository.findTopStreaks()).thenReturn(List.of(streak));

        List<Streak> result = service.getTopStreaks();

        assertEquals(1, result.size());
        verify(repository).findTopStreaks();
    }

    @Test
    void testGetAverageStreak() {
        when(repository.getAverageStreak()).thenReturn(5.75f);

        Float avg = service.getAverageStreak();

        assertEquals(5.75f, avg);
        verify(repository).getAverageStreak();
    }

    @Test
    void testCreateStreak() {
        when(repository.save(streak)).thenReturn(streak);

        Streak result = service.createStreak(streak);

        assertNotNull(result);
        assertEquals(7, result.getCurrentStreak());
        verify(repository).save(streak);
    }

    @Test
    void testDeleteStreak() {
        doNothing().when(repository).deleteById(1L);

        service.deleteStreak(1L);

        verify(repository).deleteById(1L);
    }
}
