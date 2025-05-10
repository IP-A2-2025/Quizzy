package com.backend.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

enum FlashType {
    SINGLE, MULTIPLE
}

@ToString
@Getter
@Setter
@NoArgsConstructor
class FC {
    private String question;
    private int level; // 0 - easy, 1 - medium, 2 - hard
    private FlashType type;
    private List<String> rightAnswers;
    private List<String> wrongAnswers;
}

@Setter
@Getter
public class FlashCardParser {

    private final String content;
    private final List<FC> list = new ArrayList<>();

    public FlashCardParser(String content) {
        this.content = content;
    }

    public List<FC> getParsedText() throws IOException {
        String[] flashCards = content.split("\\s*--FlashCardSeparator--\\s*");

        for (String card : flashCards) {
            if (!card.trim().isEmpty()) {
                Optional<FC> parsedCard = parseCard(card.trim());
                if (parsedCard.isPresent()) {
                    list.add(parsedCard.get());
                } else {
                    throw new IOException("Invalid flash card format: \n" + card);
                }
            }
        }

        return list;
    }

    private Optional<FC> parseCard(String card) {
        String[] sections = card.split("\\s*--InteriorSeparator--\\s*");
        if (sections.length != 3) return Optional.empty();

        FC flashCard = new FC();

        String header = sections[0].trim();
        if (header.startsWith("Single")) {
            flashCard.setType(FlashType.SINGLE);
            flashCard.setQuestion(header.substring("Single".length()).trim());
        } else if (header.startsWith("Multiple")) {
            flashCard.setType(FlashType.MULTIPLE);
            flashCard.setQuestion(header.substring("Multiple".length()).trim());
        } else {
            return Optional.empty();
        }

        List<String> rightAnswers = new ArrayList<>();
        List<String> wrongAnswers = new ArrayList<>();

        if (flashCard.getType() == FlashType.SINGLE) {
            String answer = sections[1].trim();
            if (!answer.isEmpty()) {
                rightAnswers.add(answer);
            }
        } else {
            List<String> allAnswers = Arrays.stream(sections[1].split("\n"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();

            for (String ans : allAnswers) {
                if (ans.contains("(right)")) {
                    rightAnswers.add(ans.substring("(right)  ".length()));
                } else if (ans.contains("(wrong)")) {
                    wrongAnswers.add(ans.substring("(wrong)  ".length()));
                }
            }
        }

        flashCard.setRightAnswers(rightAnswers);
        flashCard.setWrongAnswers(wrongAnswers);

        switch (sections[2].trim().toLowerCase()) {
            case "easy" -> flashCard.setLevel(0);
            case "medium" -> flashCard.setLevel(1);
            case "hard" -> flashCard.setLevel(2);
            default -> {
                return Optional.empty();
            }
        }
        return Optional.of(flashCard);
    }
}
