package com.backend.service;

import com.backend.model.Flashcard;
import com.backend.repository.FlashcardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Scanner;

@Service
public class FlashCardSetLevelService {
    private final FlashcardRepository flashcardRepository;

    @Autowired
    public FlashCardSetLevelService(FlashcardRepository flashcardRepository) {
        this.flashcardRepository = flashcardRepository;
    }

    public void setFlashcardLevel() {
        Long flashcardId = 5L;
        Optional<Flashcard> optionalFlashcard = flashcardRepository.findById(flashcardId);

        if (optionalFlashcard.isPresent()) {
            Flashcard flashcard = optionalFlashcard.get();
            Scanner scanner = new Scanner(System.in);
            int level = 0;

            while (true) {
                System.out.print("Introdu nivelul flashcardului (1-4): ");
                if (scanner.hasNextInt()) {
                    level = scanner.nextInt();
                    if (level >= 1 && level <= 4) {
                        break;
                    } else {
                        System.out.println("Nivel invalid. Te rog să introduci o valoare între 1 și 4.");
                    }
                } else {
                    System.out.println("Input invalid. Te rog să introduci un număr întreg.");
                    scanner.next();
                }
            }

            flashcard.setLevel(level);
            flashcardRepository.save(flashcard);
            System.out.println("Level setat cu succes pentru flashcardul cu ID " + flashcardId);
        } else {
            System.out.println("Nu s-a găsit flashcard cu ID " + flashcardId);
        }
    }
}