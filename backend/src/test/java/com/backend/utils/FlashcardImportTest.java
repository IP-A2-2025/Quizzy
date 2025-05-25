package com.backend.utils;

import java.util.Objects;
import com.backend.model.Flashcard;
import com.backend.model.AnswerFC;
import com.backend.model.Material;
import com.backend.model.User;
import com.backend.repository.AnswerFCRepository;
import com.backend.repository.FlashcardRepository;
import com.backend.repository.MaterialRepository;
import com.backend.repository.UserRepository;
import com.backend.service.MaterialService;

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

    @Mock(lenient = true)
    private MaterialService materialService;

    @Mock(lenient = true)
    private AnswerFCRepository answerFCRepository;

    @InjectMocks
    private FlashcardImport flashcardImport;

    @TempDir
    Path tempDir;

    private User testUser;
    private Material testMaterial;
    private List<Flashcard> savedFlashcards;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);

        testMaterial = new Material();
        testMaterial.setId(1L);
        testMaterial.setName("Test Material");
        testMaterial.setPath("test.pdf");

        savedFlashcards = new ArrayList<>();

        when(userRepository.getReferenceById(1)).thenReturn(testUser);
        when(materialRepository.getReferenceById(1L)).thenReturn(testMaterial);
        when(materialService.findByPath("test.pdf")).thenReturn(testMaterial);
        when(materialService.findByPathContaining("test.pdf")).thenReturn(Arrays.asList(testMaterial));
        when(materialService.findByPathContaining("test")).thenReturn(Arrays.asList(testMaterial));

        when(flashcardRepository.save(any(Flashcard.class))).thenAnswer(invocation -> {
            Flashcard fc = invocation.getArgument(0);
            if (fc.getId() == null) {
                fc.setId((long) (savedFlashcards.size() + 1));
            }

            boolean exists = savedFlashcards.stream()
                    .anyMatch(existing -> Objects.equals(existing.getQuestion(), fc.getQuestion()));

            if (!exists) {
                savedFlashcards.add(fc);
            }
            return fc;
        });

        when(answerFCRepository.save(any(AnswerFC.class))).thenAnswer(invocation -> {
            AnswerFC answer = invocation.getArgument(0);
            if (answer.getId() == null) {
                answer.setId(new Random().nextLong());
            }
            return answer;
        });

        when(flashcardRepository.findByQuestion(anyString())).thenReturn(null);
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

        Path flashcardFile = tempDir.resolve("test_flashcards.txt");
        Files.writeString(flashcardFile, content);

        int importedCount = flashcardImport.importFlashcardsFromDirectory(tempDir.toString(), 1);

        assertEquals(3, importedCount, "Ar trebui sa importe 3 flashcard-uri");

        verify(flashcardRepository, atLeast(3)).save(any(Flashcard.class));

        assertEquals(3, savedFlashcards.size());
    }

    @Test
    void testFullFlashcardImportDetails() throws IOException {
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

        Path flashcardFile = tempDir.resolve("test_flashcards.txt");
        Files.writeString(flashcardFile, content);

        int imported = flashcardImport.importFlashcardsFromDirectory(tempDir.toString(), 1);
        assertEquals(3, imported);
        assertEquals(3, savedFlashcards.size());

        // FLASH CARD 1 TEST
        Flashcard fc1 = savedFlashcards.get(0);
        assertEquals("What is the definition of a stable set in a graph G = (V, E)?", fc1.getQuestion());
        assertEquals("Single", fc1.getQuestionType());
        assertEquals(0, fc1.getLevel()); // easy = 0
        assertEquals(testUser, fc1.getUser());
        assertEquals(testMaterial, fc1.getMaterial());
        assertEquals(24, fc1.getPageIndex());

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
        assertEquals(1, fc2.getLevel()); // medium = 1
        assertEquals(testUser, fc2.getUser());
        assertEquals(testMaterial, fc2.getMaterial());
        assertEquals(1, fc2.getPageIndex());

        Set<AnswerFC> fc2Answers = fc2.getAnswers();
        assertNotNull(fc2Answers);
        assertEquals(4, fc2Answers.size());

        Map<String, Boolean> answersMap = new HashMap<>();
        for (AnswerFC ans : fc2Answers) {
            answersMap.put(ans.getOptionText().trim(), ans.isCorrect());
        }

        assertTrue(answersMap.get("Coloring maps (faces of planar graphs)"));
        assertTrue(answersMap.get("Exam scheduling for university departments"));
        assertFalse(answersMap.get("Finding maximum flow in transportation networks"));
        assertFalse(answersMap.get("Assignment problems in computational chemistry"));

        // FLASH CARD 3 TEST
        Flashcard fc3 = savedFlashcards.get(2);
        assertEquals("What does |X| denote for a given finite set X?", fc3.getQuestion());
        assertEquals("Single", fc3.getQuestionType());
        assertEquals(0, fc3.getLevel()); // easy = 0
        assertEquals(testUser, fc3.getUser());
        assertEquals(testMaterial, fc3.getMaterial());
        assertEquals(22, fc3.getPageIndex());

        Set<AnswerFC> fc3Answers = fc3.getAnswers();
        assertNotNull(fc3Answers);
        assertEquals(1, fc3Answers.size());
        AnswerFC answer3 = fc3Answers.iterator().next();
        assertEquals("The cardinality of X.", answer3.getOptionText());
        assertTrue(answer3.isCorrect());
    }

    @Test
    void testImportWithComplexJsonContent() throws IOException {
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

        Path flashcardFile = tempDir.resolve("test_flashcards.txt");
        Files.writeString(flashcardFile, content);

        int imported = flashcardImport.importFlashcardsFromDirectory(tempDir.toString(), 1);
        assertEquals(3, imported);
        assertEquals(3, savedFlashcards.size());
    }

    @Test
    void testImportInvalidFileFormat() throws IOException {
        String badContent = """
            This is not a valid flashcard structure because there are no flashcard separators in it
            """;

        Path flashcardFile = tempDir.resolve("test_flashcards.txt");
        Files.writeString(flashcardFile, badContent);

        int imported = flashcardImport.importFlashcardsFromDirectory(tempDir.toString(), 1);
        assertEquals(0, imported);
    }

    @Test
    void testImportWithNonexistentDirectoryThrowsException() {
        String invalidPath = tempDir.resolve("nonexistent").toString();

        IOException exception = assertThrows(IOException.class, () ->
                flashcardImport.importFlashcardsFromDirectory(invalidPath, 1)
        );

        assertTrue(exception.getMessage().contains("nu exista"));
    }

    @Test
    void testImportWithNoMatchingMaterial() throws IOException {
        String content = """
            --FlashCardSeparator--
            Single
            --InteriorSeparator--
            Test question?
            --InteriorSeparator--
            Test answer
            --InteriorSeparator--
            easy
            --InteriorSeparator--
            1
            --FlashCardSeparator--
            """;

        Path flashcardFile = tempDir.resolve("nonexistent_flashcards.txt");
        Files.writeString(flashcardFile, content);

        when(materialService.findByPath("nonexistent.pdf")).thenReturn(null);
        when(materialService.findByPathContaining("nonexistent.pdf")).thenReturn(new ArrayList<>());
        when(materialService.findByPathContaining("nonexistent")).thenReturn(new ArrayList<>());

        int imported = flashcardImport.importFlashcardsFromDirectory(tempDir.toString(), 1);
        assertEquals(0, imported); // Nu ar trebui să importe nimic dacă nu găsește materialul
    }

    @Test
    void testImportWithExistingFlashcard() throws IOException {
        String content = """
            --FlashCardSeparator--
            Single
            --InteriorSeparator--
            Existing question?
            --InteriorSeparator--
            Existing answer
            --InteriorSeparator--
            easy
            --InteriorSeparator--
            1
            --FlashCardSeparator--
            """;

        Path flashcardFile = tempDir.resolve("test_flashcards.txt");
        Files.writeString(flashcardFile, content);

        Flashcard existingFlashcard = new Flashcard();
        existingFlashcard.setId(1L);
        existingFlashcard.setQuestion("Existing question?");
        existingFlashcard.setMaterial(testMaterial);
        existingFlashcard.setAnswers(new HashSet<>());

        when(flashcardRepository.findByQuestion("Existing question?")).thenReturn(existingFlashcard);

        int imported = flashcardImport.importFlashcardsFromDirectory(tempDir.toString(), 1);
        assertEquals(0, imported); // Nu ar trebui să importe flashcard-uri existente ca noi
    }

    @Test
    void testImportWithDifferentDifficultyLevels() throws IOException {
        String content = """
            --FlashCardSeparator--
            Single
            --InteriorSeparator--
            Easy question?
            --InteriorSeparator--
            Easy answer
            --InteriorSeparator--
            easy
            --InteriorSeparator--
            1
            --FlashCardSeparator--
            Single
            --InteriorSeparator--
            Medium question?
            --InteriorSeparator--
            Medium answer
            --InteriorSeparator--
            medium
            --InteriorSeparator--
            2
            --FlashCardSeparator--
            Single
            --InteriorSeparator--
            Hard question?
            --InteriorSeparator--
            Hard answer
            --InteriorSeparator--
            hard
            --InteriorSeparator--
            3
            --FlashCardSeparator--
            """;

        Path flashcardFile = tempDir.resolve("test_flashcards.txt");
        Files.writeString(flashcardFile, content);

        int imported = flashcardImport.importFlashcardsFromDirectory(tempDir.toString(), 1);
        assertEquals(3, imported);
        assertEquals(3, savedFlashcards.size());

        assertEquals(0, savedFlashcards.get(0).getLevel()); // easy
        assertEquals(1, savedFlashcards.get(1).getLevel()); // medium
        assertEquals(2, savedFlashcards.get(2).getLevel()); // hard
    }
}