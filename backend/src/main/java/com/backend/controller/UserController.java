package com.backend.controller;

import com.backend.dto.LoginRequest;
import com.backend.dto.LoginResponse;
import com.backend.dto.RegisterRequest;
import com.backend.model.User;
import com.backend.repository.UserRepository;
import com.backend.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CONFLICT;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<Collection<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userService.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/login")
    public ResponseEntity<LoginResponse> getLoginPage() {
        logger.info("GET request to /login");
        return ResponseEntity.ok(new LoginResponse(
            null,
            null,
            null,
            "Please use POST method with email and password",
            false
        ));
    }

    @PostMapping(value = "/login",
                produces = MediaType.APPLICATION_JSON_VALUE,
                consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            logger.info("Login attempt for email: {}", loginRequest.getEmail());
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            if (email == null || password == null) {
                logger.warn("Login failed - email or password is null");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new LoginResponse(
                            null,
                            null,
                            null,
                            "Email and password are required",
                            false
                        ));
            }

            Optional<User> userOpt = userService.checkCredentials(email, password);

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                String token = userService.generateJwtForUser(email);
                logger.info("Login successful for user: {}", email);
                return ResponseEntity.ok(new LoginResponse(
                    token,
                    user.getEmail(),
                    user.getRole(),
                    "Login successful",
                    true
                ));
            } else {
                logger.warn("Login failed for user: {}", email);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse(
                            null,
                            email,
                            null,
                            "Invalid credentials",
                            false
                        ));
            }
        } catch (Exception e) {
            logger.error("Error during login process", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse(
                        null,
                        loginRequest.getEmail(),
                        null,
                        "An error occurred during login",
                        false
                    ));
        }
    }

    @PostMapping(value = "/register",
                produces = MediaType.APPLICATION_JSON_VALUE,
                consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest req) {
        try {
            logger.info("Registration attempt for email: {}", req.getEmail());
            if (userService.checkIfExists(req.getEmail())) {
                logger.warn("Registration failed - email already exists: {}", req.getEmail());
                return ResponseEntity.status(CONFLICT)
                        .body(new LoginResponse(
                            null,
                            req.getEmail(),
                            null,
                            "Email already exists",
                            false
                        ));
            }
            userService.createUser(req);
            logger.info("Registration successful for user: {}", req.getEmail());
            return ResponseEntity.ok(new LoginResponse(
                null,
                req.getEmail(),
                req.getRole(),
                "User registered successfully",
                true
            ));
        } catch (Exception e) {
            logger.error("Error during registration process", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse(
                        null,
                        req.getEmail(),
                        null,
                        "An error occurred during registration",
                        false
                    ));
        }
    }
}
