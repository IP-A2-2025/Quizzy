package com.backend.utils;

import com.backend.model.FlashcardProgress;

import java.util.Calendar;
import java.util.Date;

public class SpacedRepetitionAlgorithm {

    public static void updateProgress(FlashcardProgress progress, int responseQuality) {
        responseQuality = Math.max(0, Math.min(5, responseQuality));
        
        Double easeFactorObj = progress.getEaseFactor();
        double easeFactor = (easeFactorObj == null) ? 2.5 : easeFactorObj;
        
        Integer repetitionsObj = progress.getRepetitions();
        int repetitions = (repetitionsObj == null) ? 0 : repetitionsObj;
        
        Integer intervalObj = progress.getInterval();
        int interval = (intervalObj == null) ? 0 : intervalObj;
        
        if (responseQuality < 3) {
            repetitions = 0;
            interval = 1;
        } else {
            repetitions++;
            
            if (repetitions == 1) {
                interval = 1;
            } else if (repetitions == 2) {
                interval = 6;
            } else {
                interval = (int) Math.round(interval * easeFactor);
            }
        }
        
        // Calculează noul easeFactor
        easeFactor = calculateNewEaseFactor(easeFactor, responseQuality);
        
        // Setează noile valori în obiectul de progres
        progress.setEaseFactor(easeFactor);
        progress.setRepetitions(repetitions);
        progress.setInterval(interval);
        progress.setLastReviewed(new Date());
        progress.setDueDate(calculateNextDueDate(interval));
    }
    
    private static double calculateNewEaseFactor(double oldEaseFactor, int responseQuality) {
        double newEaseFactor = oldEaseFactor + (0.1 - (5 - responseQuality) * (0.08 + (5 - responseQuality) * 0.02));
        return Math.max(1.3, newEaseFactor);
    }
    
    private static Date calculateNextDueDate(int interval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, interval);
        return calendar.getTime();
    }
}
