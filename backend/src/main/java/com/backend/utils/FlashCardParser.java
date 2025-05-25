package com.backend.utils;


import com.backend.dto.AnswerFCDTO;
import com.backend.dto.FlashcardDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.IOException;
import java.util.*;

enum FlashType {
    SINGLE, MULTIPLE
}

@Setter
@Getter
class FlashCardParser {

    private final String content;
    private final List<FlashcardDTO> list = new ArrayList<>();

    public FlashCardParser(String content) {
        this.content = content;
    }

    public List<FlashcardDTO> getParsedText() throws IOException {
        int firstIndex = content.indexOf("--FlashCardSeparator--");
        int lastIndex = content.lastIndexOf("--FlashCardSeparator--");

        if (firstIndex == -1 || lastIndex == -1 || firstIndex == lastIndex) {
            throw new IOException("Invalid flash card format.");
        }

        String trimmedContent = content.substring(firstIndex, lastIndex + "--FlashCardSeparator--".length());

        String[] flashCards = trimmedContent.split("\\s*--FlashCardSeparator--\\s*");

        for (String card : flashCards) {
            if (!card.trim().isEmpty()) {
                Optional<FlashcardDTO> parsedCard = parseCard(card.trim());
                if (parsedCard.isPresent()) {
                    list.add(parsedCard.get());
                } else {
                    throw new IOException("Invalid flash card format: \n" + card);
                }
            }
        }

        return list;
    }


    private Optional<FlashcardDTO> parseCard(String card) {
        String[] sections = card.split("\\s*--InteriorSeparator--\\s*");

        FlashcardDTO flashCard = new FlashcardDTO();
        FlashType type = FlashType.SINGLE;

        String header = sections[0].trim();
        if (header.startsWith("Single")) {
            flashCard.setQuestionType(header);
        } else if (header.startsWith("Multiple")) {
            flashCard.setQuestionType(header);
            type = FlashType.MULTIPLE;
        } else {
            return Optional.empty();
        }

        flashCard.setQuestion(sections[1].trim());

        Set<AnswerFCDTO> answers = new HashSet<>();
        if (type == FlashType.SINGLE) {
            AnswerFCDTO answer = new AnswerFCDTO();
            answer.setOptionText(sections[2].trim());
            answer.setCorrect(true);
            answer.setFlashcardId(flashCard.getId());
            answers.add(answer);
        } else {
            String[] options = sections[2].lines().toArray(String[]::new);
            for (String option : options) {
                AnswerFCDTO answer = getAnswerFCDTO(option, flashCard);
                answers.add(answer);
            }
        }
        flashCard.setAnswers(answers);

        String fixedDifficulty = sections[3].trim().toLowerCase();
        switch(fixedDifficulty){
            case "easy":
                flashCard.setLevel(0);
                break;
            case "medium":
                flashCard.setLevel(1);
                break;
            case "hard":
                flashCard.setLevel(2);
                break;
            default:
                return Optional.empty();
        }

        try {
            flashCard.setPageIndex(Integer.parseInt(sections[4].trim()));
        } catch (NumberFormatException e) {
            try {
                String fallback = sections[4].trim().split(",")[0].trim();
                flashCard.setPageIndex(Integer.parseInt(fallback));
            } catch (Exception ex) {
                return Optional.empty();
            }
        }

        return Optional.of(flashCard);
    }

    private static AnswerFCDTO getAnswerFCDTO(String option, FlashcardDTO flashCard) {
        AnswerFCDTO answer = new AnswerFCDTO();
        String substring = option.trim().substring(8, option.length());
        if (option.startsWith("(right)")) {
            answer.setCorrect(true);
            answer.setFlashcardId(flashCard.getId());
            answer.setOptionText(substring);
        } else if (option.startsWith("(wrong)")) {
            answer.setCorrect(false);
            answer.setFlashcardId(flashCard.getId());
            answer.setOptionText(substring);
        }
        return answer;
    }
}
