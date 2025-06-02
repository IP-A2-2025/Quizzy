package com.backend.utils;

import com.backend.model.Flashcard;
import com.backend.model.AnswerFC;
import com.backend.model.FlashcardProgress;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlashcardPrioritizer {
    
    private static final Logger logger = LoggerFactory.getLogger(FlashcardPrioritizer.class);


    public static List<Flashcard> getPrioritizedFlashcards(List<Flashcard> flashcards, int limit) {
        return getPrioritizedFlashcards(flashcards, null, null, limit);
    }
    

    public static List<Flashcard> getPrioritizedFlashcards(List<Flashcard> flashcards, List<FlashcardProgress> progressData,Integer userId, int limit) {
        FibonacciHeap<Flashcard> priorityHeap = new FibonacciHeap<>();
        Date currentDate = new Date();
        
        Map<Long, FlashcardProgress> progressMap = new HashMap<>();
        if (progressData != null) {
            for (FlashcardProgress progress : progressData) {
                progressMap.put(progress.getFlashcard().getId(), progress);
            }
        }
        for (Flashcard flashcard : flashcards) {
            FlashcardProgress progress = progressMap.get(flashcard.getId());
            double priority = (progress != null) 
                ? calculatePriorityWithProgress(flashcard, progress, currentDate)
                : calculatePriority(flashcard, currentDate);
                
            logger.debug("Card ID: {}, Question: {}, Priority: {}, Progress: {}", 
                flashcard.getId(), 
                flashcard.getQuestion(), 
                priority,
                progress != null ? "available" : "none");
                
            priorityHeap.insert(flashcard, priority);
        }
        
        List<Flashcard> prioritizedFlashcards = new ArrayList<>();
        int count = Math.min(limit, flashcards.size());
        
        logger.debug("Extracting top {} cards from priority heap", count);
        
        for (int i = 0; i < count; i++) {
            Flashcard nextCard = priorityHeap.extractMin();
            if (nextCard != null) {
                prioritizedFlashcards.add(nextCard);
                
                Long flashcardId = nextCard.getId();
                logger.debug("Selected card #{}: ID={}, Question={}", 
                    i+1, flashcardId, nextCard.getQuestion());
                
                if (progressMap != null && progressMap.containsKey(flashcardId)) {
                    FlashcardProgress progress = progressMap.get(flashcardId);
                    logger.debug("  Progress data for card #{}: DueDate={}, EaseFactor={}, Repetitions={}, Interval={}",
                        i+1, progress.getDueDate(), progress.getEaseFactor(), 
                        progress.getRepetitions(), progress.getInterval());
                } else {
                    logger.debug("  No progress data available for card #{}", i+1);
                }
            } else {
                break;
            }
        }
        
        return prioritizedFlashcards;
    }
    

    private static double calculatePriorityWithProgress(Flashcard flashcard, FlashcardProgress progress, Date currentDate) {
        double priority = 1000.0;
        Date dueDate = progress.getDueDate();
        if (dueDate != null) {
            if (dueDate.before(currentDate)) {
                long diffInMillies = Math.abs(currentDate.getTime() - dueDate.getTime());
                long daysOverdue = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                priority -= Math.min(daysOverdue * 50, 500);
            } else {
                long diffInMillies = Math.abs(dueDate.getTime() - currentDate.getTime());
                long daysUntilDue = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                if (daysUntilDue <= 5) {
                    priority -= (5 - daysUntilDue) * 50;
                }
            }
        }
        
        Double easeFactor = progress.getEaseFactor();
        if (easeFactor != null) {
            double easeAdjustment = (easeFactor - 1.3) * 200 / 1.2;
            priority -= Math.max(0, 200 - easeAdjustment);
        }
        
        Integer repetitions = progress.getRepetitions();
        if (repetitions != null) {
            if (repetitions < 3) {
                priority -= (3 - repetitions) * 30;
            }
        }
        
        Integer interval = progress.getInterval();
        if (interval != null && interval < 7) {
            priority -= (7 - interval) * 10;
        }
        
        return priority;
    }
    

    private static double calculatePriority(Flashcard flashcard, Date currentDate) {
        double priority = 1000.0;
        
        Integer level = flashcard.getLevel();
        if (level != null) {
            priority -= (5 - level) * 100;
        }
        
        Date lastStudied = flashcard.getLastStudiedAt();
        if (lastStudied != null) {
            long diffInMillies = Math.abs(currentDate.getTime() - lastStudied.getTime());
            long daysSinceLastStudied = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            
            long daysBonus = Math.min(daysSinceLastStudied, 30);
            priority -= daysBonus * 10;
        } else {
            priority -= 350;
        }
        
        Set<AnswerFC> answers = flashcard.getAnswers();
        if (answers != null && !answers.isEmpty()) {
            int correctAnswers = 0;
            
            for (AnswerFC answer : answers) {
                if (answer.isCorrect()) {
                    correctAnswers++;
                }
            }
            
            double correctRatio = (double) correctAnswers / answers.size();
            priority -= (1 - correctRatio) * 300;
        }
        
        return priority;
    }
}
