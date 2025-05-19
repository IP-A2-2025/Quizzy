package com.backend.service;

import com.backend.model.Streak;
import com.backend.model.User;
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

    public void updateStreakForUser(Integer userId) {
        Date today = new Date(System.currentTimeMillis());
        Date yesterday = Date.valueOf(today.toLocalDate().minusDays(1));

        Optional<Streak> existingStreakOpt = streakRepository.findLatestByUserId(userId);

        Streak streak;

        if (existingStreakOpt.isPresent()) {
            streak = existingStreakOpt.get();

            // Dacă ultima completare a fost ieri => incrementăm
            if (streak.getLastCompletedDate().equals(yesterday)) {
                streak.setCurrentStreak(streak.getCurrentStreak() + 1);
                streak.setLastCompletedDate(today);
            }
            // Dacă ultima completare a fost azi => nu facem nimic
            else if (streak.getLastCompletedDate().equals(today)) {
                // deja a fost înregistrată activitatea de azi
                return;
            }
            // Dacă s-a sărit o zi => resetăm streak
            else {
                streak.setCurrentStreak(1);
                streak.setLastCompletedDate(today);
            }

        } else {
            // Nu există un streak anterior => creăm unul nou
            streak = new Streak();
            streak.setUser(new User(userId)); // Cannot resolve constructor 'User(Integer)'
            streak.setCurrentStreak(1);
            streak.setLastCompletedDate(today);
        }

        streakRepository.save(streak);
    }
    public Optional<Streak> getLatestStreakForUser(Integer userId) {
        return streakRepository.findTopByUserIdOrderByLastCompletedDateDesc(userId);
    }
}
