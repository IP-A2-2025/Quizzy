package com.backend.controller;

import com.backend.service.ExampleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class ExampleController {

    private final ExampleService exampleService;

    public ExampleController(ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    @GetMapping("/testget")
    public ResponseEntity<String> testget() {
        return ResponseEntity.ok(exampleService.sayHello());
    }

    @PostMapping("/testpost")
    public ResponseEntity<String> testpost() {
        return ResponseEntity.ok(exampleService.sayHi());
    }
}
