package com.backend.controller;

import com.backend.model.Flashcard;
import com.backend.model.User;
import com.backend.service.FlashcardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FlashcardControllerTest {

    @Mock
    private FlashcardService flashcardService;

    @InjectMocks
    private FlashcardController flashcardController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    public FlashcardControllerTest() {
        openMocks(this);
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(flashcardController).build();
        objectMapper = new ObjectMapper();
    }

    // Helper
    private Flashcard createFlashcard(Long id, Integer userId) {
        Flashcard flashcard = new Flashcard();
        flashcard.setId(id);
        User user = new User();
        user.setId(userId);
        flashcard.setUser(user);
        flashcard.setQuestion("Sample Q");
        flashcard.setQuestion("Sample A");
        flashcard.setLevel(1);
        flashcard.setLastStudiedAt(new Date());
        return flashcard;
    }

    // ===============================
    // UNIT TESTS (apelează direct metodele controllerului)
    // ===============================

    @Test
    void shouldReturnAllFlashcards() {
        List<Flashcard> flashcards = Arrays.asList(new Flashcard(), new Flashcard());
        when(flashcardService.getAllFlashcards()).thenReturn(flashcards);

        ResponseEntity<List<Flashcard>> response = flashcardController.getAllFlashcards();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void shouldReturnFlashcardByIdIfExists() {
        Flashcard flashcard = createFlashcard(1L, 1);
        when(flashcardService.getFlashcardById(1L)).thenReturn(Optional.of(flashcard));

        ResponseEntity<Flashcard> response = flashcardController.getFlashcardById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void shouldReturnNotFoundIfFlashcardByIdDoesNotExist() {
        when(flashcardService.getFlashcardById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Flashcard> response = flashcardController.getFlashcardById(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void shouldCreateFlashcardSuccessfully() {
        Flashcard flashcard = createFlashcard(null, 1);
        Flashcard created = createFlashcard(1L, 1);
        when(flashcardService.createFlashcard(flashcard)).thenReturn(created);

        ResponseEntity<Flashcard> response = flashcardController.createFlashcard(flashcard);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void shouldUpdateFlashcardSuccessfully() {
        Flashcard flashcard = createFlashcard(1L, 1);
        when(flashcardService.updateFlashcard(1L, flashcard)).thenReturn(flashcard);

        ResponseEntity<Flashcard> response = flashcardController.updateFlashcard(1L, flashcard);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void shouldDeleteFlashcardSuccessfully() {
        doNothing().when(flashcardService).deleteFlashcard(1L);

        ResponseEntity<Void> response = flashcardController.deleteFlashcard(1L);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldReturnFlashcardsByUserId() {
        List<Flashcard> flashcards = Arrays.asList(new Flashcard(), new Flashcard());
        when(flashcardService.getByUserId(1)).thenReturn(flashcards);

        ResponseEntity<List<Flashcard>> response = flashcardController.getByUserId(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void shouldReturnFlashcardsByMaterialId() {
        List<Flashcard> flashcards = Arrays.asList(new Flashcard());
        when(flashcardService.getByMaterialId(5L)).thenReturn(flashcards);

        ResponseEntity<List<Flashcard>> response = flashcardController.getByMaterialId(5L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnDueFlashcards() {
        Date date = new Date();
        List<Flashcard> flashcards = Arrays.asList(new Flashcard());
        when(flashcardService.getDueFlashcards(date, 1)).thenReturn(flashcards);

        ResponseEntity<List<Flashcard>> response = flashcardController.getDueFlashcards(date, 1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    // ===============================
    // E2E-STYLE TESTS cu MockMvc (simulare apel HTTP)
    // ===============================

    @Test
    void e2eShouldReturnAllFlashcardsHttp() throws Exception {
        List<Flashcard> flashcards = Arrays.asList(
                createFlashcard(1L, 1),
                createFlashcard(2L, 2)
        );
        when(flashcardService.getAllFlashcards()).thenReturn(flashcards);

        mockMvc.perform(get("/Flashcard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void e2eShouldReturnFlashcardByIdHttp() throws Exception {
        Flashcard flashcard = createFlashcard(1L, 1);
        when(flashcardService.getFlashcardById(1L)).thenReturn(Optional.of(flashcard));

        mockMvc.perform(get("/Flashcard/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.level").value(1));
    }

    @Test
    void e2eShouldReturnNotFoundHttp() throws Exception {
        when(flashcardService.getFlashcardById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/Flashcard/999"))
                .andExpect(status().isNotFound());
    }

}
