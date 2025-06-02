package com.backend.controller;

import com.backend.model.AnswerFC;
import com.backend.service.AnswerFCService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class AnswerFCControllerTest {

    @Mock
    private AnswerFCService answerFCService;

    @InjectMocks
    private AnswerFCController answerFCController;

    public AnswerFCControllerTest() {
        openMocks(this);
    }

    private AnswerFC createAnswer(Long id, String content) {
        AnswerFC answer = new AnswerFC();
        answer.setId(id);
        answer.setOptionText(content);
        return answer;
    }

    @Test
    void shouldReturnAllAnswers() {
        List<AnswerFC> answers = Arrays.asList(createAnswer(1L, "Yes"), createAnswer(2L, "No"));
        when(answerFCService.getAllAnswers()).thenReturn(answers);

        ResponseEntity<List<AnswerFC>> response = answerFCController.getAllAnswers();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void shouldReturnAnswerByIdIfExists() {
        AnswerFC answer = createAnswer(1L, "Yes");
        when(answerFCService.getAnswerById(1L)).thenReturn(answer);

        ResponseEntity<AnswerFC> response = answerFCController.getAnswerById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Yes", response.getBody().getOptionText());
    }

    @Test
    void shouldReturnNotFoundIfAnswerDoesNotExist() {
        when(answerFCService.getAnswerById(1L)).thenReturn(null);

        ResponseEntity<AnswerFC> response = answerFCController.getAnswerById(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void shouldCreateAnswer() {
        AnswerFC input = createAnswer(null, "Yes");
        AnswerFC saved = createAnswer(1L, "Yes");

        when(answerFCService.createAnswer(input)).thenReturn(saved);

        ResponseEntity<AnswerFC> response = answerFCController.createAnswer(input);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void shouldUpdateAnswer() {
        AnswerFC input = createAnswer(null, "Updated");
        AnswerFC updated = createAnswer(1L, "Updated");

        when(answerFCService.updateAnswer(1L, input)).thenReturn(updated);

        ResponseEntity<AnswerFC> response = answerFCController.updateAnswer(1L, input);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated", response.getBody().getOptionText());
    }

    @Test
    void shouldDeleteAnswer() {
        doNothing().when(answerFCService).deleteAnswer(1L);

        ResponseEntity<Void> response = answerFCController.deleteAnswer(1L);

        assertEquals(200, response.getStatusCodeValue());
    }
}
