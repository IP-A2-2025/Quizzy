package com.backend.service;

import com.backend.model.Streak;
import com.backend.repository.StreakRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StreakService {

    private final StreakRepository streakRepository;

    public StreakService(StreakRepository streakRepository) {
        this.streakRepository = streakRepository;
    }

    public List<Streak> getAllStreaks() {
        return streakRepository.findAll();
    }

    public Optional<Streak> getStreakById(Long id) {
        return streakRepository.findById(id);
    }

    public List<Streak> getStreaksByUserId(Integer userId) {
        return streakRepository.findByUserId(userId);
    }

    public Optional<Streak> getStreakByUserIdAndDate(Integer userId, Date date) {
        return streakRepository.findByUserIdAndDate(userId, date);
    }

    public List<Streak> getStreaksByMinimumStreak(int minStreak) {
        return streakRepository.findByMinimumStreak(minStreak);
    }

    public List<Streak> getTopStreaks() {
        return streakRepository.findTopStreaks();
    }

    public Float getAverageStreak() {
        return streakRepository.getAverageStreak();
    }

    public Streak createStreak(Streak streak) {
        return streakRepository.save(streak);
    }

    public void deleteStreak(Long id) {
        streakRepository.deleteById(id);
    }
}
