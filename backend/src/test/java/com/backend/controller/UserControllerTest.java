package com.backend.controller;

import com.backend.dto.LoginRequest;
import com.backend.dto.LoginResponse;
import com.backend.dto.RegisterRequest;
import com.backend.dto.UpdateUserRequest;
import com.backend.model.Streak;
import com.backend.model.User;
import com.backend.service.StreakService;
import com.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class UserControllerTest {
    @Mock
    private UserService userService;
    
    @Mock
    private StreakService streakService;
    
    @InjectMocks
    private UserController userController;
    
    public UserControllerTest() {
        openMocks(this);
    }
    
    private User createSampleUser(Integer id, String email, String role) {
        User user = new User();
        user.setId(id);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail(email);
        user.setRole(role);
        user.setPassword("encodedPassword");
        return user;
    }
    
    @Test
    void shouldReturnAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(createSampleUser(1, "john@example.com", "student"));
        users.add(createSampleUser(2, "jane@example.com", "profesor"));

        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<Collection<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(userService).getAllUsers();
    }

    @Test
    void shouldReturnUserById() {
        Integer userId = 1;
        User user = createSampleUser(userId, "john@example.com", "student");

        when(userService.findById(userId)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userId, response.getBody().getId());
        verify(userService).findById(userId);
    }

    @Test
    void shouldReturnNotFoundForInvalidUserId() {
        Integer userId = 99;

        when(userService.findById(userId)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserById(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService).findById(userId);
    }

    @Test
    void shouldReturnUserByEmailPath() {
        String email = "john@example.com";
        User user = createSampleUser(1, email, "student");

        when(userService.findByEmail(email)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserByEmailPath(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(email, response.getBody().getEmail());
        verify(userService).findByEmail(email);
    }

    @Test
    void shouldReturnNotFoundForInvalidEmailPath() {
        String email = "nonexistent@example.com";

        when(userService.findByEmail(email)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserByEmailPath(email);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService).findByEmail(email);
    }

    @Test
    void shouldReturnUserProfile() {
        String email = "john@example.com";
        User user = createSampleUser(1, email, "student");

        when(userService.findByEmail(email)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserByEmail(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(email, response.getBody().getEmail());
        verify(userService).findByEmail(email);
    }

    @Test
    void shouldReturnLoginPage() {
        ResponseEntity<LoginResponse> response = userController.getLoginPage();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("", response.getBody().getToken());
        assertEquals("Please use POST method with email and password", response.getBody().getMessage());
    }

    @Test
    void shouldLoginSuccessfullyWithUserId() {
        String email = "john@example.com";
        String password = "password";
        String token = "jwt-token";
        String role = "student";
        Integer userId = 1;

        LoginRequest loginRequest = new LoginRequest(email, password);
        User user = createSampleUser(userId, email, role);

        when(userService.checkCredentials(email, password)).thenReturn(Optional.of(user));
        when(userService.generateJwtForUser(email)).thenReturn(token);

        ResponseEntity<LoginResponse> response = userController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals(token, response.getBody().getToken());
        assertEquals(email, response.getBody().getEmail());
        assertEquals(role, response.getBody().getRole());
        assertEquals(userId, response.getBody().getUserId());
        assertEquals("Login successful as " + role, response.getBody().getMessage());
        verify(userService).checkCredentials(email, password);
        verify(userService).generateJwtForUser(email);
    }

    @Test
    void shouldReturnUnauthorizedForInvalidCredentials() {
        String email = "john@example.com";
        String password = "wrongpassword";

        LoginRequest loginRequest = new LoginRequest(email, password);

        when(userService.checkCredentials(email, password)).thenReturn(Optional.empty());

        ResponseEntity<LoginResponse> response = userController.login(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Invalid email or password", response.getBody().getMessage());
        verify(userService).checkCredentials(email, password);
        verify(userService, never()).generateJwtForUser(anyString());
    }

    @Test
    void shouldReturnBadRequestForEmptyEmailOrPassword() {
        LoginRequest loginRequest = new LoginRequest("", "password");

        ResponseEntity<LoginResponse> response = userController.login(loginRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Email and password are required", response.getBody().getMessage());
        verify(userService, never()).checkCredentials(anyString(), anyString());
    }

    @Test
    void shouldRegisterStudentSuccessfully() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("new@example.com");
        request.setPassword("password123");
        request.setRole("student");

        when(userService.checkIfExists(request.getEmail())).thenReturn(false);
        doNothing().when(userService).createUser(request);

        ResponseEntity<LoginResponse> response = userController.register(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals(request.getEmail(), response.getBody().getEmail());
        assertEquals("student", response.getBody().getRole());
        assertEquals("User registered successfully as student", response.getBody().getMessage());
        verify(userService).checkIfExists(request.getEmail());
        verify(userService).createUser(request);
    }

    @Test
    void shouldRegisterProfesorWithValidSecret() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("Jane");
        request.setLastName("Doe");
        request.setEmail("profesor@example.com");
        request.setPassword("password123");
        request.setRole("profesor");
        request.setProfessorSecret("valid-secret-key");

        when(userService.checkIfExists(request.getEmail())).thenReturn(false);
        when(userService.validateProfessorSecret("valid-secret-key")).thenReturn(true);
        doNothing().when(userService).createUser(request);

        ResponseEntity<LoginResponse> response = userController.register(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals(request.getEmail(), response.getBody().getEmail());
        assertEquals("profesor", response.getBody().getRole());
        assertEquals("User registered successfully as profesor", response.getBody().getMessage());
        verify(userService).validateProfessorSecret("valid-secret-key");
        verify(userService).checkIfExists(request.getEmail());
        verify(userService).createUser(request);
    }

    @Test
    void shouldReturnUnauthorizedForInvalidProfesorSecret() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("Jane");
        request.setLastName("Doe");
        request.setEmail("profesor@example.com");
        request.setPassword("password123");
        request.setRole("profesor");
        request.setProfessorSecret("invalid-secret");

        when(userService.validateProfessorSecret("invalid-secret")).thenReturn(false);

        ResponseEntity<LoginResponse> response = userController.register(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Invalid professor secret key", response.getBody().getMessage());
        verify(userService).validateProfessorSecret("invalid-secret");
        verify(userService, never()).checkIfExists(anyString());
        verify(userService, never()).createUser(any());
    }

    @Test
    void shouldReturnBadRequestForMissingProfesorSecret() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("Jane");
        request.setLastName("Doe");
        request.setEmail("profesor@example.com");
        request.setPassword("password123");
        request.setRole("profesor");
        // No professor secret provided

        ResponseEntity<LoginResponse> response = userController.register(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Professor secret key is required for professor registration", response.getBody().getMessage());
        verify(userService, never()).validateProfessorSecret(anyString());
        verify(userService, never()).checkIfExists(anyString());
        verify(userService, never()).createUser(any());
    }

    @Test
    void shouldReturnBadRequestForInvalidRole() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("invalid@example.com");
        request.setPassword("password123");
        request.setRole("admin"); // Invalid role

        ResponseEntity<LoginResponse> response = userController.register(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Role must be either student or profesor", response.getBody().getMessage());
        verify(userService, never()).checkIfExists(anyString());
        verify(userService, never()).createUser(any());
    }

    @Test
    void shouldReturnBadRequestForEmptyRole() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("empty@example.com");
        request.setPassword("password123");
        request.setRole(""); // Empty role

        ResponseEntity<LoginResponse> response = userController.register(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Role is required", response.getBody().getMessage());
        verify(userService, never()).checkIfExists(anyString());
        verify(userService, never()).createUser(any());
    }

    @Test
    void shouldReturnConflictForExistingEmail() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("existing@example.com");
        request.setPassword("password123");
        request.setRole("student");

        when(userService.checkIfExists(request.getEmail())).thenReturn(true);

        ResponseEntity<LoginResponse> response = userController.register(request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Email already exists", response.getBody().getMessage());
        verify(userService).checkIfExists(request.getEmail());
        verify(userService, never()).createUser(any(RegisterRequest.class));
    }
    
    // Tests for update methods
    @Test
    void shouldUpdateFirstName() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setId(1);
        request.setValue("NewFirstName");
        
        when(userService.checkUserById(request.getId())).thenReturn(true);
        when(userService.updateFirstName(request.getId(), request.getValue())).thenReturn(true);
        
        ResponseEntity<String> response = userController.updateFirstName(request);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("First name updated successfully", response.getBody());
        verify(userService).updateFirstName(request.getId(), request.getValue());
    }
    
    @Test
    void shouldReturnNotFoundForInvalidUserIdOnFirstNameUpdate() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setId(99);
        request.setValue("NewFirstName");
        
        when(userService.checkUserById(request.getId())).thenReturn(false);
        
        ResponseEntity<String> response = userController.updateFirstName(request);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(userService, never()).updateFirstName(any(), any());
    }
    
    @Test
    void shouldReturnErrorOnFirstNameUpdateFailure() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setId(1);
        request.setValue("NewFirstName");
        
        when(userService.checkUserById(request.getId())).thenReturn(true);
        when(userService.updateFirstName(request.getId(), request.getValue())).thenReturn(false);
        
        ResponseEntity<String> response = userController.updateFirstName(request);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to update first name", response.getBody());
    }
    
    @Test
    void shouldUpdateLastName() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setId(1);
        request.setValue("NewLastName");
        
        when(userService.checkUserById(request.getId())).thenReturn(true);
        when(userService.updateLastName(request.getId(), request.getValue())).thenReturn(true);
        
        ResponseEntity<String> response = userController.updateLastName(request);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Last name updated successfully", response.getBody());
        verify(userService).updateLastName(request.getId(), request.getValue());
    }
    
    @Test
    void shouldUpdateEmail() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setId(1);
        request.setValue("newemail@example.com");
        
        when(userService.checkUserById(request.getId())).thenReturn(true);
        when(userService.updateEmail(request.getId(), request.getValue())).thenReturn(true);
        
        ResponseEntity<String> response = userController.updateEmail(request);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Email updated successfully", response.getBody());
        verify(userService).updateEmail(request.getId(), request.getValue());
    }
    
    @Test
    void shouldReturnConflictOnExistingEmail() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setId(1);
        request.setValue("existing@example.com");
        
        when(userService.checkUserById(request.getId())).thenReturn(true);
        when(userService.updateEmail(request.getId(), request.getValue())).thenReturn(false);
        
        ResponseEntity<String> response = userController.updateEmail(request);
        
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Email already exists or update failed", response.getBody());
    }
    
    @Test
    void shouldUpdatePassword() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setId(1);
        request.setValue("newPassword123");
        
        when(userService.checkUserById(request.getId())).thenReturn(true);
        when(userService.updatePassword(request.getId(), request.getValue())).thenReturn(true);
        
        ResponseEntity<String> response = userController.updatePassword(request);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password updated successfully", response.getBody());
        verify(userService).updatePassword(request.getId(), request.getValue());
    }
    
    // Tests for streak methods
    @Test
    void shouldGetUserStreaks() {
        Integer userId = 1;
        List<Streak> streaks = new ArrayList<>();
        streaks.add(new Streak());
        streaks.add(new Streak());
        
        when(streakService.getStreaksByUserId(userId)).thenReturn(streaks);
        
        ResponseEntity<List<Streak>> response = userController.getUserStreaks(userId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(streakService).getStreaksByUserId(userId);
    }
    
    @Test
    void shouldUpdateUserStreak() {
        Integer userId = 1;
        
        ResponseEntity<String> response = userController.updateUserStreak(userId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains(userId.toString()));
        verify(streakService).updateStreakForUser(userId);
    }
    
    @Test
    void shouldGetLatestUserStreak() {
        Integer userId = 1;
        Streak streak = new Streak();
        streak.setCurrentStreak(5);
        
        when(streakService.getLatestStreakForUser(userId)).thenReturn(Optional.of(streak));
        
        ResponseEntity<Streak> response = userController.getLatestUserStreak(userId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, response.getBody().getCurrentStreak());
        verify(streakService).getLatestStreakForUser(userId);
    }
    
    @Test
    void shouldReturnNotFoundForNoLatestStreak() {
        Integer userId = 1;
        
        when(streakService.getLatestStreakForUser(userId)).thenReturn(Optional.empty());
        
        ResponseEntity<Streak> response = userController.getLatestUserStreak(userId);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}

