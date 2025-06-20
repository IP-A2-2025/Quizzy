package com.backend.service;

import com.backend.model.Streak;
import com.backend.model.User;
import com.backend.repository.StreakRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
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
                streak.setCurrentStreak(0);
                streak.setLastCompletedDate(today);
            }

        } else {
            // Nu există un streak anterior => creăm unul nou
            streak = new Streak();
            streak.setUser(new User(userId)); // Cannot resolve constructor 'User(Integer)'
            streak.setCurrentStreak(0);
            streak.setLastCompletedDate(today);
        }

        streakRepository.save(streak);
    }
    public Optional<Streak> getLatestStreakForUser(Integer userId) {
        Optional<Streak> optionalStreak = streakRepository.findTopByUserIdOrderByLastCompletedDateDesc(userId);

        if (optionalStreak.isPresent()) {
            Streak streak = optionalStreak.get();

            // Calculează diferența de zile
            long millisInADay = 24 * 60 * 60 * 1000L;
            long diff = System.currentTimeMillis() - streak.getLastCompletedDate().getTime();
            long daysDiff = diff / millisInADay;

            if (daysDiff > 2) {
                streak.setCurrentStreak(0);
                streakRepository.save(streak); // salvează resetarea
            }

            return Optional.of(streak);
        } else {
            // Creează și salvează un streak nou cu data de ieri
            Streak newStreak = new Streak();
            newStreak.setUser(new User(userId));  // presupune că userul există deja
            newStreak.setCurrentStreak(0);

            // Setează data de ieri
            LocalDate yesterday = LocalDate.now().minusDays(1);
            newStreak.setLastCompletedDate(Date.valueOf(yesterday));

            Streak savedStreak = streakRepository.save(newStreak);
            return Optional.of(savedStreak);
        }
    }


    public void updateStreakIfYesterday(Integer userId) {
        List<Streak> streaks = streakRepository.findByUserId(userId);

        if (streaks == null || streaks.isEmpty()) {
            return; // Nu există streak-uri pentru utilizator
        }

        for (Streak streak : streaks) {
            LocalDate lastCompleted = streak.getLastCompletedDate().toLocalDate();
            LocalDate yesterday = LocalDate.now().minusDays(1);

            if (lastCompleted.equals(yesterday)) {
                streak.setCurrentStreak(streak.getCurrentStreak() + 1);
                streak.setLastCompletedDate(Date.valueOf(LocalDate.now()));
                streakRepository.save(streak);
            }
        }
    }


}
