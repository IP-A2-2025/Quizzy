package com.backend.utils;

import com.backend.model.Flashcard;
import com.backend.model.AnswerFC;
import com.backend.model.Material;
import com.backend.model.User;
import com.backend.repository.FlashcardRepository;
import com.backend.repository.MaterialRepository;
import com.backend.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlashcardImportTest {

    @Mock(lenient = true)
    private FlashcardRepository flashcardRepository;

    @Mock(lenient = true)
    private MaterialRepository materialRepository;

    @Mock(lenient = true)
    private UserRepository userRepository;

    @InjectMocks
    private FlashcardImport flashcardImport;

    @TempDir
    Path tempDir;

    private User testUser;
    private Material testMaterial;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);

        testMaterial = new Material();
        testMaterial.setId(1L);

        when(userRepository.getReferenceById(1)).thenReturn(testUser);
        when(materialRepository.getReferenceById(1L)).thenReturn(testMaterial);
        when(flashcardRepository.save(any(Flashcard.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void testImportValidFlashcardsFile() throws IOException {
        String content = """
            --FlashCardSeparator--
            Single
            --InteriorSeparator--
            What is the definition of a stable set in a graph G = (V, E)?
            --InteriorSeparator--
            A subset S of V such that no two vertices in S are adjacent (no edge between them).
            --InteriorSeparator--
            easy
            --InteriorSeparator--
            24
            --FlashCardSeparator--
            Multiple
            --InteriorSeparator--
            Which of the following are applications of graph colorings? (Select all that apply)
            --InteriorSeparator--
            (right) Coloring maps (faces of planar graphs)
            (right) Exam scheduling for university departments
            (wrong) Finding maximum flow in transportation networks
            (wrong) Assignment problems in computational chemistry
            --InteriorSeparator--
            medium
            --InteriorSeparator--
            1
            --FlashCardSeparator--
            Single
            --InteriorSeparator--
            What does |X| denote for a given finite set X?
            --InteriorSeparator--
            The cardinality of X.
            --InteriorSeparator--
            easy
            --InteriorSeparator--
            22
            --FlashCardSeparator--
            """;


        Path flashcardFile = tempDir.resolve("flashcards.txt");
        Files.writeString(flashcardFile, content);

        int importedCount = flashcardImport.importFlashcardsFromDirectory(tempDir.toString(), 1);

        assertEquals(3, importedCount, "Ar trebui sa importe 3 flashcard-uri");

        verify(flashcardRepository, atLeast(3)).save(any(Flashcard.class));
    }

    @BeforeEach
    void clearTempDir() throws IOException {
        try (Stream<Path> files = Files.list(tempDir)) {
            files.forEach(f -> {
                try {
                    Files.delete(f);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Test
    void testFullFlashcardImportDetails() throws IOException {
        String content = """
            [{content={parts=[{text=```
            --FlashCardSeparator--
            Single
            --InteriorSeparator--
            What is the definition of a stable set in a graph G = (V, E)?
            --InteriorSeparator--
            A subset S of V such that no two vertices in S are adjacent (no edge between them).
            --InteriorSeparator--
            easy
            --InteriorSeparator--
            24
            --FlashCardSeparator--
            Multiple
            --InteriorSeparator--
            Which of the following are applications of graph colorings? (Select all that apply)
            --InteriorSeparator--
            (right) Coloring maps (faces of planar graphs)
            (right) Exam scheduling for university departments
            (wrong) Finding maximum flow in transportation networks
            (wrong) Assignment problems in computational chemistry
            --InteriorSeparator--
            medium
            --InteriorSeparator--
            1
            --FlashCardSeparator--
            Single
            --InteriorSeparator--
            What does |X| denote for a given finite set X?
            --InteriorSeparator--
            The cardinality of X.
            --InteriorSeparator--
            easy
            --InteriorSeparator--
            22
            --FlashCardSeparator--
            ```}], role=model}, finishReason=STOP, citationMetadata={citationSources=[{startIndex=10077, endIndex=10198}
            """;

        Path flashcardFile = tempDir.resolve("_flashcards.txt");
        Files.writeString(flashcardFile, content);

        List<Flashcard> savedFlashcards = new ArrayList<>();
        when(flashcardRepository.save(any(Flashcard.class))).thenAnswer(invocation -> {
            Flashcard fc = invocation.getArgument(0);
            fc.setId(new Random().nextLong());
            if (!savedFlashcards.contains(fc)) {
                savedFlashcards.add(fc);
            }
            return fc;
        });

        int imported = flashcardImport.importFlashcardsFromDirectory(tempDir.toString(), 1);
        assertEquals(3, imported);
        assertEquals(3, savedFlashcards.size());

        // FLASH CARD 1 TEST

        Flashcard fc1 = savedFlashcards.getFirst();
        assertEquals("What is the definition of a stable set in a graph G = (V, E)?", fc1.getQuestion());
        assertEquals("Single", fc1.getQuestionType());
        assertEquals(0, fc1.getLevel());
        assertEquals(testUser, fc1.getUser());
        assertEquals(24,fc1.getPageIndex());

        Set<AnswerFC> fc1Answers = fc1.getAnswers();
        assertNotNull(fc1Answers);
        assertEquals(1, fc1Answers.size());
        AnswerFC answer1 = fc1Answers.iterator().next();
        assertEquals("A subset S of V such that no two vertices in S are adjacent (no edge between them).", answer1.getOptionText());
        assertTrue(answer1.isCorrect());

        // FLASH CARD 2 TEST

        Flashcard fc2 = savedFlashcards.get(1);
        assertEquals("Which of the following are applications of graph colorings? (Select all that apply)", fc2.getQuestion());
        assertEquals("Multiple", fc2.getQuestionType());
        assertEquals(1, fc2.getLevel());
        assertEquals(testUser, fc2.getUser());
        assertEquals(1,fc2.getPageIndex());

        Set<AnswerFC> fc2Answers = fc2.getAnswers();
        assertNotNull(fc2Answers);
        assertEquals(4, fc2Answers.size());

        Map<String, Boolean> answersMap = new HashMap<>();
        for (AnswerFC ans : fc2Answers) {
            answersMap.put(ans.getOptionText().trim(), ans.isCorrect());
        }
        System.out.println("Answers in map: " + answersMap.keySet());

        assertTrue(answersMap.get("Coloring maps (faces of planar graphs)"));
        assertTrue(answersMap.get("Exam scheduling for university departments"));
        assertFalse(answersMap.get("Finding maximum flow in transportation networks"));
        assertFalse(answersMap.get("Assignment problems in computational chemistry"));

        // FLASH CARD 3 TEST

        Flashcard fc3 = savedFlashcards.get(2);
        assertEquals("What does |X| denote for a given finite set X?", fc3.getQuestion());
        assertEquals("Single", fc3.getQuestionType());
        assertEquals(0, fc3.getLevel());
        assertEquals(testUser, fc3.getUser());
        assertEquals(22,fc3.getPageIndex());

        Set<AnswerFC> fc3Answers = fc3.getAnswers();
        assertNotNull(fc3Answers);
        assertEquals(1, fc3Answers.size());
        AnswerFC answer3 = fc3Answers.iterator().next();
        assertEquals("The cardinality of X.", answer3.getOptionText());
        assertTrue(answer3.isCorrect());

    }

    @Test
    void testImportInvalidFileThrowsException() throws IOException {
        String badContent = """
            This is not a valid flashcard structure because there are no flashcard sepatators in it
        """;

        Path flashcardFile = tempDir.resolve("flashcards.txt");
        Files.writeString(flashcardFile, badContent);

        IOException exception = assertThrows(IOException.class, () ->
                flashcardImport.importFlashcardsFromDirectory(tempDir.toString(), 1)
        );

        assertTrue(exception.getMessage().contains("Invalid flash card format"));
    }

    @Test
    void testImportWithNonexistentDirectoryThrowsException() {
        String invalidPath = tempDir.resolve("nonexistent").toString();

        IOException exception = assertThrows(IOException.class, () ->
                flashcardImport.importFlashcardsFromDirectory(invalidPath, 1)
        );

        assertTrue(exception.getMessage().contains("nu exista"));
    }

}
