package com.backend.controller;

import com.backend.model.AnswerFC;
import com.backend.model.Course;
import com.backend.model.Flashcard;
import com.backend.model.Material;
import com.backend.model.User;
import com.backend.repository.FlashcardRepository;
import com.backend.service.FlashcardService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class FlashcardControlE2ETest {

    @Autowired
    private FlashcardService flashcardService;

    @Autowired
    private FlashcardRepository flashcardRepository;

    @Autowired
    private EntityManager entityManager;

    // Test data placeholder
    private Long testFlashcardId;
    private Integer testUserId;
    private Long testMaterialId;
    private Long testCourseId;

    @BeforeEach
    public void setup() {
        // Create a test user if not exists
        User testUser = new User();
        testUser.setFirstName("test_flashcard_user_" + System.currentTimeMillis());
        testUser.setLastName("test_flashcard_user_" + System.currentTimeMillis());
        testUser.setEmail("test_flashcard_" + System.currentTimeMillis() + "@example.com");
        testUser.setPassword("test_flashcard_user_" + System.currentTimeMillis());
        testUser.setRole("student");
        entityManager.persist(testUser);
        testUserId = testUser.getId();

        // Create a test course
        Course testCourse = new Course();
        testCourse.setTitle("Test Course " + System.currentTimeMillis());
        testCourse.setDescription("Test Course Description");
        testCourse.setProfessor(testUser);
        entityManager.persist(testCourse);
        testCourseId = testCourse.getId();

        // Create a test material
        Material testMaterial = new Material();
        testMaterial.setName("Test Material " + System.currentTimeMillis());
        testMaterial.setPath("/test/path");
        testMaterial.setCourse(testCourse);
        entityManager.persist(testMaterial);
        testMaterialId = testMaterial.getId();

        entityManager.flush();
    }

    @Test
    @Rollback(true)
    public void testCreateAndRetrieveFlashcard() {
        // Retrieve the test user and material
        User testUser = entityManager.find(User.class, testUserId);
        Material testMaterial = entityManager.find(Material.class, testMaterialId);

        // Create a new flashcard
        Flashcard flashcard = new Flashcard();
        flashcard.setQuestion("Test Flashcard Question " + System.currentTimeMillis());
        flashcard.setLevel(1);
        flashcard.setLastStudiedAt(new Date());
        flashcard.setQuestionType("Test");
        flashcard.setUser(testUser);
        flashcard.setMaterial(testMaterial);

        // Create answers
        Set<AnswerFC> answers = new HashSet<>();
        AnswerFC answer = new AnswerFC();
        answer.setOptionText("Test Answer " + System.currentTimeMillis());
        answer.setFlashcard(flashcard);
        answers.add(answer);
        flashcard.setAnswers(answers);

        // Save the flashcard
        Flashcard savedFlashcard = flashcardService.createFlashcard(flashcard);
        testFlashcardId = savedFlashcard.getId();

        // Verify the saved flashcard
        assertNotNull(savedFlashcard.getId());
        assertEquals(flashcard.getQuestion(), savedFlashcard.getQuestion());
        assertEquals(1, savedFlashcard.getLevel());
        assertEquals("Test", savedFlashcard.getQuestionType());
        assertEquals(testUser, savedFlashcard.getUser());
        assertEquals(testMaterial, savedFlashcard.getMaterial());

        // Verify answers
        assertThat(savedFlashcard.getAnswers()).hasSize(1);
    }

    @Test
    @Rollback(true)
    public void testUpdateFlashcard() {
        // First, create a flashcard to update
        User testUser = entityManager.find(User.class, testUserId);
        Material testMaterial = entityManager.find(Material.class, testMaterialId);

        Flashcard originalFlashcard = new Flashcard();
        originalFlashcard.setQuestion("Original Question");
        originalFlashcard.setLevel(1);
        originalFlashcard.setUser(testUser);
        originalFlashcard.setMaterial(testMaterial);

        Flashcard savedFlashcard = flashcardService.createFlashcard(originalFlashcard);
        Long flashcardId = savedFlashcard.getId();

        // Prepare update
        Flashcard updateFlashcard = new Flashcard();
        updateFlashcard.setQuestion("Updated Question");
        updateFlashcard.setLevel(2);
        updateFlashcard.setUser(testUser);
        updateFlashcard.setMaterial(testMaterial);

        // Perform update
        Flashcard updatedFlashcard = flashcardService.updateFlashcard(flashcardId, updateFlashcard);

        // Verify updates
        assertEquals("Updated Question", updatedFlashcard.getQuestion());
        assertEquals(2, updatedFlashcard.getLevel());
        assertEquals(flashcardId, updatedFlashcard.getId());
    }

    @Test
    @Rollback(true)
    public void testGetFlashcardsByUserId() {
        // Retrieve the test user
        User testUser = entityManager.find(User.class, testUserId);
        Material testMaterial = entityManager.find(Material.class, testMaterialId);

        // Create multiple flashcards for the test user
        Flashcard flashcard1 = new Flashcard();
        flashcard1.setQuestion("Test Question 1");
        flashcard1.setUser(testUser);
        flashcard1.setMaterial(testMaterial);
        flashcardService.createFlashcard(flashcard1);

        Flashcard flashcard2 = new Flashcard();
        flashcard2.setQuestion("Test Question 2");
        flashcard2.setUser(testUser);
        flashcard2.setMaterial(testMaterial);
        flashcardService.createFlashcard(flashcard2);

        // Retrieve flashcards by user ID
        List<Flashcard> userFlashcards = flashcardService.getByUserId(testUserId);

        // Verify results
        assertThat(userFlashcards).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @Rollback(true)
    public void testDeleteFlashcard() {
        // Retrieve the test user
        User testUser = entityManager.find(User.class, testUserId);
        Material testMaterial = entityManager.find(Material.class, testMaterialId);

        // Create a flashcard to delete
        Flashcard flashcard = new Flashcard();
        flashcard.setQuestion("Flashcard to Delete");
        flashcard.setUser(testUser);
        flashcard.setMaterial(testMaterial);
        Flashcard savedFlashcard = flashcardService.createFlashcard(flashcard);

        // Delete the flashcard
        flashcardService.deleteFlashcard(savedFlashcard.getId());

        // Verify deletion
        Optional<Flashcard> deletedFlashcard = flashcardService.getFlashcardById(savedFlashcard.getId());
        assertTrue(deletedFlashcard.isEmpty(), "Flashcard-ul nu a fost șters");
    }

    @Test
    @Transactional
    public void testFlashcardUpdate() {
        // Retrieve the test user and material
        User testUser = entityManager.find(User.class, testUserId);
        Material testMaterial = entityManager.find(Material.class, testMaterialId);

        // Creare flashcard inițial
        Flashcard initialFlashcard = new Flashcard();
        initialFlashcard.setQuestion("Întrebare Inițială");
        initialFlashcard.setUser(testUser);
        initialFlashcard.setMaterial(testMaterial);
        Flashcard savedFlashcard = flashcardRepository.save(initialFlashcard);

        // Curățare context de persistență
        entityManager.flush(); // Asigură-te că modificările sunt scrise în baza de date înainte de curățare
        entityManager.clear();

        // Recuperare flashcard din baza de date
        Optional<Flashcard> retrievedFlashcard = flashcardRepository.findById(savedFlashcard.getId());
        assertTrue(retrievedFlashcard.isPresent(), "Flashcard-ul nu a fost găsit după salvare");

        // Actualizare flashcard
        Flashcard flashcardToUpdate = retrievedFlashcard.get();
        flashcardToUpdate.setQuestion("Întrebare Actualizată");
        flashcardToUpdate.setLevel(2);  // Modificăm nivelul pentru a testa actualizarea
        Flashcard updatedFlashcard = flashcardRepository.save(flashcardToUpdate);

        // Curățare context de persistență
        entityManager.flush(); // Asigură-te că modificările sunt scrise în baza de date înainte de curățare
        entityManager.clear();

        // Verificare actualizare flashcard din baza de date
        Optional<Flashcard> finalRetrievedFlashcard = flashcardRepository.findById(updatedFlashcard.getId());
        assertTrue(finalRetrievedFlashcard.isPresent(), "Flashcard-ul actualizat nu a fost găsit");

        // Verificăm valorile actualizate
        assertEquals("Întrebare Actualizată", finalRetrievedFlashcard.get().getQuestion(),
                "Întrebarea flashcard-ului nu a fost actualizată corect");
        assertEquals(2, finalRetrievedFlashcard.get().getLevel(),
                "Nivelul flashcard-ului nu a fost actualizat corect");
    }

    @AfterEach
    public void cleanup() {
        // Optional: Additional cleanup if needed
        if (testFlashcardId != null) {
            try {
                flashcardRepository.deleteById(testFlashcardId);
            } catch (Exception ignored) {}
        }
    }
}
