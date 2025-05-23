package com.backend.controller;

import com.backend.dto.LoginRequest;
import com.backend.dto.LoginResponse;
import com.backend.dto.RegisterRequest;
import com.backend.dto.UpdateUserRequest;
import com.backend.model.Streak;
import com.backend.model.User;
import com.backend.repository.UserRepository;
import com.backend.service.StreakService;
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
import java.util.List;
import java.util.Optional;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CONFLICT;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final StreakService streakService;

    @Autowired
    public UserController(UserService userService, StreakService streakService) {
        this.userService = userService;
        this.streakService = streakService;
    }

    @GetMapping("/users")
    public ResponseEntity<Collection<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userService.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmailPath(@PathVariable String email) {
        logger.info("Getting user by email path: {}", email);
        try {
            // Decode the email since it may be URL encoded
            String decodedEmail = java.net.URLDecoder.decode(email, "UTF-8");
            logger.info("Decoded email: {}", decodedEmail);
            return userService.findByEmail(decodedEmail)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> {
                        logger.warn("User not found for email: {}", decodedEmail);
                        return ResponseEntity.notFound().build();
                    });
        } catch (java.io.UnsupportedEncodingException e) {
            logger.error("Error decoding email parameter: {}", email, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        logger.info("Getting user profile by email: {}", email);
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("User not found for email: {}", email);
                    return ResponseEntity.notFound().build();
                });
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

    @PostMapping("/update/firstname")
    public ResponseEntity<String> updateFirstName(@RequestBody UpdateUserRequest request) {
        try {
            logger.info("Updating firstName for user with id: {}", request.getId());
            if (!userService.checkUserById(request.getId())) {
                logger.warn("User with id {} not found", request.getId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            boolean updated = userService.updateFirstName(request.getId(), request.getValue());
            if (updated) {
                logger.info("FirstName updated successfully for user with id: {}", request.getId());
                return ResponseEntity.ok("First name updated successfully");
            } else {
                logger.warn("Failed to update firstName for user with id: {}", request.getId());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update first name");
            }
        } catch (Exception e) {
            logger.error("Error updating firstName", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating first name");
        }
    }

    @PostMapping("/update/lastname")
    public ResponseEntity<String> updateLastName(@RequestBody UpdateUserRequest request) {
        try {
            logger.info("Updating lastName for user with id: {}", request.getId());
            if (!userService.checkUserById(request.getId())) {
                logger.warn("User with id {} not found", request.getId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            boolean updated = userService.updateLastName(request.getId(), request.getValue());
            if (updated) {
                logger.info("LastName updated successfully for user with id: {}", request.getId());
                return ResponseEntity.ok("Last name updated successfully");
            } else {
                logger.warn("Failed to update lastName for user with id: {}", request.getId());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update last name");
            }
        } catch (Exception e) {
            logger.error("Error updating lastName", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating last name");
        }
    }

    @PostMapping("/update/email")
    public ResponseEntity<String> updateEmail(@RequestBody UpdateUserRequest request) {
        try {
            logger.info("Updating email for user with id: {}", request.getId());
            if (!userService.checkUserById(request.getId())) {
                logger.warn("User with id {} not found", request.getId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            boolean updated = userService.updateEmail(request.getId(), request.getValue());
            if (updated) {
                logger.info("Email updated successfully for user with id: {}", request.getId());
                return ResponseEntity.ok("Email updated successfully");
            } else {
                logger.warn("Failed to update email for user with id: {}", request.getId());
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists or update failed");
            }
        } catch (Exception e) {
            logger.error("Error updating email", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating email");
        }
    }

    @PostMapping("/update/password")
    public ResponseEntity<String> updatePassword(@RequestBody UpdateUserRequest request) {
        try {
            logger.info("Updating password for user with id: {}", request.getId());
            if (!userService.checkUserById(request.getId())) {
                logger.warn("User with id {} not found", request.getId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            boolean updated = userService.updatePassword(request.getId(), request.getValue());
            if (updated) {
                logger.info("Password updated successfully for user with id: {}", request.getId());
                return ResponseEntity.ok("Password updated successfully");
            } else {
                logger.warn("Failed to update password for user with id: {}", request.getId());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update password");
            }
        } catch (Exception e) {
            logger.error("Error updating password", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating password");
        }
    }


    // === STREAK ENDPOINTS ===

    // GET /users/streak?userId=X
    @GetMapping("/streak")
    public ResponseEntity<List<Streak>> getUserStreaks(@RequestParam Integer userId) {
        List<Streak> streaks = streakService.getStreaksByUserId(userId);
        return ResponseEntity.ok(streaks);
    }

    // POST /users/streak?userId=X
    @PostMapping("/streak")
    public ResponseEntity<String> updateUserStreak(@RequestParam Integer userId) {
        streakService.updateStreakForUser(userId);
        return ResponseEntity.ok("Streak actualizat pentru utilizatorul cu ID: " + userId);
    }

    // GET /users/streak/latest?userId=X
    @GetMapping("/streak/latest")
    public ResponseEntity<Streak> getLatestUserStreak(@RequestParam Integer userId) {
        return streakService.getLatestStreakForUser(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
