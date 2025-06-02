package com.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class   DatabaseCheckController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseCheckController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/db-check")
    public String checkDatabaseConnection() {
        try {
            return jdbcTemplate.queryForObject("SELECT 'Connection successful!' as status", String.class);
        } catch (Exception e) {
            return "Database connection failed: " + e.getMessage();
        }
    }
}