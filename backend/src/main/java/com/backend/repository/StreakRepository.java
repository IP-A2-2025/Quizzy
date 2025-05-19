package com.backend.repository;

import com.backend.model.Streak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface StreakRepository extends JpaRepository<Streak, Long> {

    @Query("SELECT s FROM Streak s WHERE s.user.id = :userId")
    List<Streak> findByUserId(@Param("userId") Integer userId);

    @Query("SELECT s FROM Streak s WHERE s.user.id = :userId AND s.lastCompletedDate = :date")
    Optional<Streak> findByUserIdAndDate(@Param("userId") Integer userId, @Param("date") Date date);

    @Query("SELECT s FROM Streak s WHERE s.currentStreak >= :minStreak ORDER BY s.currentStreak DESC")
    List<Streak> findByMinimumStreak(@Param("minStreak") int minStreak);

    @Query("SELECT s FROM Streak s ORDER BY s.currentStreak DESC")
    List<Streak> findTopStreaks();

    @Query("SELECT AVG(s.currentStreak) FROM Streak s")
    Float getAverageStreak();

    @Query("SELECT s FROM Streak s WHERE s.user.id = :userId ORDER BY s.lastCompletedDate DESC")
    Optional<Streak> findLatestByUserId(@Param("userId") Integer userId);

    Optional<Streak> findTopByUserIdOrderByLastCompletedDateDesc(Integer userId);

}