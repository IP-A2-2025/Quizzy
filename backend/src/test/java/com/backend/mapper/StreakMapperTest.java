package com.backend.mapper;

import com.backend.dto.StreakDTO;
import com.backend.model.Streak;
import com.backend.model.User;
import com.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StreakMapperTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StreakMapper streakMapper;

    private Streak streak;
    private StreakDTO streakDTO;
    private User user;
    private final Date lastCompletedDate = Date.valueOf(LocalDate.now());

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Doe");

        streak = new Streak();
        streak.setId(1L);
        streak.setCurrentStreak(5);
        streak.setLastCompletedDate(lastCompletedDate);
        streak.setUser(user);

        streakDTO = new StreakDTO();
        streakDTO.setId(1L);
        streakDTO.setCurrentStreak(5);
        streakDTO.setLastCompletedDate(lastCompletedDate);
        streakDTO.setUserId(1);
    }

    @Test
    void toDTO_WithValidStreak_ShouldMapAllFields() {
        // Act
        StreakDTO result = streakMapper.toDTO(streak);

        // Assert
        assertNotNull(result);
        assertEquals(streak.getId(), result.getId());
        assertEquals(streak.getCurrentStreak(), result.getCurrentStreak());
        assertEquals(streak.getLastCompletedDate(), result.getLastCompletedDate());
        assertEquals(user.getId(), result.getUserId());
    }

    @Test
    void toDTO_WithNullStreak_ShouldReturnNull() {
        // Act
        StreakDTO result = streakMapper.toDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toDTO_WithNullUser_ShouldMapOtherFields() {
        // Arrange
        streak.setUser(null);

        // Act
        StreakDTO result = streakMapper.toDTO(streak);

        // Assert
        assertNotNull(result);
        assertEquals(streak.getId(), result.getId());
        assertEquals(streak.getCurrentStreak(), result.getCurrentStreak());
        assertEquals(streak.getLastCompletedDate(), result.getLastCompletedDate());
        assertNull(result.getUserId());
    }

    @Test
    void toEntity_WithValidDTO_ShouldMapAllFields() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Act
        Streak result = streakMapper.toEntity(streakDTO);

        // Assert
        assertNotNull(result);
        assertEquals(streakDTO.getId(), result.getId());
        assertEquals(streakDTO.getCurrentStreak(), result.getCurrentStreak());
        assertEquals(streakDTO.getLastCompletedDate(), result.getLastCompletedDate());
        assertEquals(user, result.getUser());

        // Verify repository interactions
        verify(userRepository).findById(1);
    }

    @Test
    void toEntity_WithNullDTO_ShouldReturnNull() {
        // Act
        Streak result = streakMapper.toEntity(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_WithNullUserId_ShouldMapOtherFields() {
        // Arrange
        streakDTO.setUserId(null);

        // Act
        Streak result = streakMapper.toEntity(streakDTO);

        // Assert
        assertNotNull(result);
        assertEquals(streakDTO.getId(), result.getId());
        assertEquals(streakDTO.getCurrentStreak(), result.getCurrentStreak());
        assertEquals(streakDTO.getLastCompletedDate(), result.getLastCompletedDate());
        assertNull(result.getUser());
    }

    @Test
    void toEntity_WithNonexistentUser_ShouldMapOtherFields() {
        // Arrange
        streakDTO.setUserId(999);

        when(userRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Streak result = streakMapper.toEntity(streakDTO);

        // Assert
        assertNotNull(result);
        assertEquals(streakDTO.getId(), result.getId());
        assertEquals(streakDTO.getCurrentStreak(), result.getCurrentStreak());
        assertEquals(streakDTO.getLastCompletedDate(), result.getLastCompletedDate());
        assertNull(result.getUser());

        // Verify repository interactions
        verify(userRepository).findById(999);
    }
}