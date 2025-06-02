package com.backend.mapper;

import com.backend.dto.StreakDTO;
import com.backend.model.Streak;
import com.backend.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class StreakMapper implements EntityMapper<Streak, StreakDTO> {

    private final UserRepository userRepository;

    public StreakMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public StreakDTO toDTO(Streak streak) {
        if (streak == null) return null;

        StreakDTO dto = new StreakDTO();
        dto.setId(streak.getId());
        dto.setCurrentStreak(streak.getCurrentStreak());
        dto.setLastCompletedDate(streak.getLastCompletedDate());

        if (streak.getUser() != null) {
            dto.setUserId(streak.getUser().getId());
        }

        return dto;
    }

    @Override
    public Streak toEntity(StreakDTO dto) {
        if (dto == null) return null;

        Streak streak = new Streak();
        streak.setId(dto.getId());
        streak.setCurrentStreak(dto.getCurrentStreak());
        streak.setLastCompletedDate(dto.getLastCompletedDate());

        if (dto.getUserId() != null) {
            userRepository.findById(dto.getUserId())
                    .ifPresent(streak::setUser);
        }

        return streak;
    }
}