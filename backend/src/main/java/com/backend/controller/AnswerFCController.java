package com.backend.controller;

import com.backend.model.AnswerFC;
import com.backend.service.AnswerFCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/AnswerFC")
public class AnswerFCController {

    private final AnswerFCService answerFCService;

    @Autowired
    public AnswerFCController(AnswerFCService answerFCService) {
        this.answerFCService = answerFCService;
    }

    @GetMapping
    public ResponseEntity<List<AnswerFC>> getAllAnswers() {
        return ResponseEntity.ok(answerFCService.getAllAnswers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnswerFC> getAnswerById(@PathVariable Long id) {
        AnswerFC answer = answerFCService.getAnswerById(id);
        if (answer != null) {
            return ResponseEntity.ok(answer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<AnswerFC> createAnswer(@RequestBody AnswerFC answerFC) {
        return ResponseEntity.ok(answerFCService.createAnswer(answerFC));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnswerFC> updateAnswer(@PathVariable Long id, @RequestBody AnswerFC answerFC) {
        AnswerFC updated = answerFCService.updateAnswer(id, answerFC);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long id) {
        answerFCService.deleteAnswer(id);
        return ResponseEntity.ok().build();
    }
}
