package com.backend;

import com.backend.controller.UserController;
import com.backend.dto.LoginRequest;
import com.backend.dto.LoginResponse;
import com.backend.dto.RegisterRequest;
import com.backend.model.User;
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
        users.add(createSampleUser(1, "john@example.com", "STUDENT"));
        users.add(createSampleUser(2, "jane@example.com", "PROFESSOR"));

        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<Collection<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(userService).getAllUsers();
    }

    @Test
    void shouldReturnUserById() {
        Integer userId = 1;
        User user = createSampleUser(userId, "john@example.com", "STUDENT");

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
    void shouldReturnLoginPage() {
        ResponseEntity<LoginResponse> response = userController.getLoginPage();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("", response.getBody().getToken());
    }

    @Test
    void shouldLoginSuccessfully() {
        String email = "john@example.com";
        String password = "password";
        String token = "jwt-token";
        String role = "STUDENT";

        LoginRequest loginRequest = new LoginRequest(email, password);
        User user = createSampleUser(1, email, role);

        when(userService.checkCredentials(email, password)).thenReturn(Optional.of(user));
        when(userService.generateJwtForUser(email)).thenReturn(token);

        ResponseEntity<LoginResponse> response = userController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals(token, response.getBody().getToken());
        assertEquals(email, response.getBody().getEmail());
        assertEquals(role, response.getBody().getRole());
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
        assertEquals("Invalid credentials", response.getBody().getMessage());
        verify(userService).checkCredentials(email, password);
        verify(userService, never()).generateJwtForUser(anyString());
    }

    @Test
    void shouldRegisterSuccessfully() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("new@example.com");
        request.setPassword("password123");
        request.setRole("STUDENT");

        String token = "new-jwt-token";

        when(userService.checkIfExists(request.getEmail())).thenReturn(false);
        doNothing().when(userService).createUser(request);
        when(userService.generateJwtForUser(request.getEmail())).thenReturn(token);

        ResponseEntity<LoginResponse> response = userController.register(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        //assertEquals(token, response.getBody().getToken());
        assertEquals(request.getEmail(), response.getBody().getEmail());
        assertEquals(request.getRole(), response.getBody().getRole());
        verify(userService).checkIfExists(request.getEmail());
        verify(userService).createUser(request);
        //verify(userService).generateJwtForUser(request.getEmail());
    }

    @Test
    void shouldReturnConflictForExistingEmail() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("existing@example.com");
        request.setPassword("password123");
        request.setRole("STUDENT");

        when(userService.checkIfExists(request.getEmail())).thenReturn(true);

        ResponseEntity<LoginResponse> response = userController.register(request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Email already exists", response.getBody().getMessage());
        verify(userService).checkIfExists(request.getEmail());
        verify(userService, never()).createUser(any(RegisterRequest.class));
        verify(userService, never()).generateJwtForUser(anyString());
    }
}

