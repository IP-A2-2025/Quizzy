package com.backend.utils;

import com.backend.dto.AnswerFCDTO;
import com.backend.dto.FlashcardDTO;
import com.backend.model.Flashcard;
import com.backend.model.Material;
import com.backend.model.User;
import com.backend.model.AnswerFC;
import com.backend.repository.AnswerFCRepository;
import com.backend.repository.FlashcardRepository;
import com.backend.repository.MaterialRepository;
import com.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Component
public class FlashcardImport {

    private final FlashcardRepository flashcardRepository;
    private final MaterialRepository materialRepository;
    private final UserRepository userRepository;

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Long DEFAULT_MATERIAL_ID = 1L;
    private final AnswerFCRepository answerFCRepository;

    @Autowired
    public FlashcardImport(
            FlashcardRepository flashcardRepository,
            MaterialRepository materialRepository,
            UserRepository userRepository, AnswerFCRepository answerFCRepository) {
        this.flashcardRepository = flashcardRepository;
        this.materialRepository = materialRepository;
        this.userRepository = userRepository;
        this.answerFCRepository = answerFCRepository;
    }

    @Transactional
    public int importFlashcardsFromDirectory(String baseDir, Integer userId) throws IOException {
        Integer actualUserId = (userId != null) ? userId : DEFAULT_USER_ID;
        User user = userRepository.getReferenceById(actualUserId);

        int totalImported = 0;

        List<Path> flashcardFiles = findFlashcardFiles(Paths.get(baseDir));

        /*
        for (Path flashcardFile : flashcardFiles) {
            System.out.println(flashcardFile.getFileName());
        }
         */
        System.out.println("Se citeste din flashcardul: " + flashcardFiles.get(totalImported));
        for (Path filePath : flashcardFiles) {
            Path relativePath = Paths.get(baseDir).relativize(filePath);
            String fileContent = Files.readString(filePath);

            FlashCardParser parser = new FlashCardParser(fileContent);
            List<FlashcardDTO> flashcards = parser.getParsedText();

            Material material = materialRepository.getReferenceById(DEFAULT_MATERIAL_ID);

            int imported = importFlashcardsForMaterial(flashcards, material, user);
            totalImported += imported;

            System.out.println("Importate " + imported + " flashcard-uri din " + filePath);
        }

        return totalImported;
    }

    private List<Path> findFlashcardFiles(Path baseDir) throws IOException {
        List<Path> result = new ArrayList<>();

        if (!Files.exists(baseDir)) {
            throw new IOException("Directorul specificat nu exista: " + baseDir);
        }

        Files.walk(baseDir)
                .filter(path -> path.getFileName().toString().endsWith("flashcards.txt"))
                .forEach(result::add);

        return result;
    }

    private int importFlashcardsForMaterial(List<FlashcardDTO> flashcards, Material material, User user) {
        int count = 0;

        for (FlashcardDTO fc : flashcards) {
            String questionText = extractQuestionText(fc.getQuestion());
            Optional<Flashcard> existingOpt = Optional.ofNullable(flashcardRepository.findByQuestion(questionText));
            Flashcard flashcard;

            if (existingOpt.isPresent()) {
                flashcard = existingOpt.get();
            } else {
                flashcard = new Flashcard();
                flashcard.setQuestion(questionText);
                flashcard.setLevel(fc.getLevel());
                flashcard.setLastStudiedAt(null);
                flashcard.setQuestionType(fc.getQuestionType());
                flashcard.setUser(user);
                flashcard.setPageIndex(fc.getPageIndex());

                if (flashcard.getAnswers() == null) {
                    flashcard.setAnswers(new HashSet<>());
                }

                flashcard = flashcardRepository.save(flashcard);
                count++;
            }

            updateAnswers(fc, flashcard);

            flashcardRepository.save(flashcard);
        }

        return count;
    }

    private void updateAnswers(FlashcardDTO fcDTO, Flashcard flashcard) {
        if (flashcard.getAnswers() == null) {
            flashcard.setAnswers(new HashSet<>());
        }

        if (fcDTO.getAnswers() == null) return;

        for (AnswerFCDTO answerDTO : fcDTO.getAnswers()) {
            if (!answerExists(flashcard, answerDTO.getOptionText())) {
                AnswerFC answer = new AnswerFC();
                answer.setFlashcard(flashcard);
                answer.setOptionText(answerDTO.getOptionText());
                answer.setCorrect(answerDTO.isCorrect());
                flashcard.getAnswers().add(answer);
            }
        }
    }

    private String extractQuestionText(String rawQuestion) {
        if (rawQuestion.startsWith("[") && rawQuestion.endsWith("]")) {
            return rawQuestion.substring(1, rawQuestion.length() - 1);
        }
        return rawQuestion;
    }

    private Set<AnswerFC> createAnswers(FlashcardDTO fcDTO, Flashcard flashcard) {
        Set<AnswerFC> answers = new HashSet<>();

        if (fcDTO.getAnswers() == null) return answers;

        for (AnswerFCDTO answerDTO : fcDTO.getAnswers()) {
            if (!answerExists(flashcard, answerDTO.getOptionText())) {
                AnswerFC answer = new AnswerFC();
                answer.setFlashcard(flashcard);
                answer.setOptionText(answerDTO.getOptionText());
                answer.setCorrect(answerDTO.isCorrect());
                answers.add(answer);
            }
        }

        return answers;
    }


    private boolean answerExists(Flashcard flashcard, String optionText) {
        if (flashcard.getAnswers() == null || flashcard.getAnswers().isEmpty()) {
            return false;
        }

        return flashcard.getAnswers().stream()
                .anyMatch(a -> a.getOptionText() != null && a.getOptionText().equals(optionText));
    }

}