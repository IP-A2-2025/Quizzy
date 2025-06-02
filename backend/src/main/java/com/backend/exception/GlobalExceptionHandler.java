package com.backend.exception;

import com.backend.dto.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<LoginResponse> handleException(Exception e) {
        logger.error("Unhandled exception occurred", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new LoginResponse(
                    null,
                    null,
                    null,
                    "An unexpected error occurred: " + e.getMessage(),
                    false
                ));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<LoginResponse> handleRuntimeException(RuntimeException e) {
        logger.error("Runtime exception occurred", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new LoginResponse(
                    null,
                    null,
                    null,
                    "An error occurred: " + e.getMessage(),
                    false
                ));
    }
} 