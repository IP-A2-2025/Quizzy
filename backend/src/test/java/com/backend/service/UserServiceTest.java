package com.backend.service;

import com.backend.dto.RegisterRequest;
import com.backend.model.User;
import com.backend.repository.UserRepository;
import com.backend.config.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private JwtUtil jwtUtil;
    
    @InjectMocks
    private UserService userService;
    
    private User testUser;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testUser = new User();
        testUser.setId(1);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john.doe@example.com");
        testUser.setRole("STUDENT");
        testUser.setPassword("encodedPassword");
    }
    
    @Test
    void shouldCheckCredentialsWithValidEmailAndPassword() {
        String email = "john.doe@example.com";
        String rawPassword = "password123";
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(rawPassword, testUser.getPassword())).thenReturn(true);
        
        Optional<User> result = userService.checkCredentials(email, rawPassword);
        
        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches(rawPassword, testUser.getPassword());
    }
    
    @Test
    void shouldNotReturnUserWithInvalidPassword() {
        String email = "john.doe@example.com";
        String rawPassword = "wrongPassword";
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(rawPassword, testUser.getPassword())).thenReturn(false);
        
        Optional<User> result = userService.checkCredentials(email, rawPassword);
        
        assertTrue(result.isEmpty());
    }
    
    @Test
    void shouldNotReturnUserWithInvalidEmail() {
        String email = "nonexistent@example.com";
        String rawPassword = "password123";
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        
        Optional<User> result = userService.checkCredentials(email, rawPassword);
        
        assertTrue(result.isEmpty());
        verify(passwordEncoder, never()).matches(any(), any());
    }
    
    @Test
    void shouldGetAllUsers() {
        Collection<User> users = Arrays.asList(testUser, new User());
        when(userRepository.allUsers()).thenReturn(users);
        
        Collection<User> result = userService.getAllUsers();
        
        assertEquals(users, result);
        verify(userRepository).allUsers();
    }
    
    @Test
    void shouldReturnTrueWhenUserExists() {
        Integer userId = 1;
        when(userRepository.existsById(userId)).thenReturn(true);
        
        boolean result = userService.checkUserById(userId);
        
        assertTrue(result);
        verify(userRepository).existsById(userId);
    }
    
    @Test
    void shouldReturnFalseWhenUserDoesNotExist() {
        Integer userId = 99;
        when(userRepository.existsById(userId)).thenReturn(false);
        
        boolean result = userService.checkUserById(userId);
        
        assertFalse(result);
    }
    
    @Test
    void shouldFindUserById() {
        Integer userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        
        Optional<User> result = userService.findById(userId);
        
        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepository).findById(userId);
    }
    
    @Test
    void shouldGenerateJwtToken() {
        String email = "john.doe@example.com";
        String token = "jwt-token-value";
        
        when(jwtUtil.generateToken(email)).thenReturn(token);
        
        String result = userService.generateJwtForUser(email);
        
        assertEquals(token, result);
        verify(jwtUtil).generateToken(email);
    }
    
    @Test
    void shouldCreateUser() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("Jane");
        request.setLastName("Smith");
        request.setEmail("jane.smith@example.com");
        request.setPassword("password123");
        request.setRole("STUDENT");
        
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        
        userService.createUser(request);
        
        verify(passwordEncoder).encode(request.getPassword());
        verify(userRepository).save(any(User.class));
    }
    
    @Test
    void shouldUpdateFirstName() {
        Integer userId = 1;
        String newFirstName = "NewName";
        
        when(userRepository.updateFirstName(userId, newFirstName)).thenReturn(1);
        
        boolean result = userService.updateFirstName(userId, newFirstName);
        
        assertTrue(result);
        verify(userRepository).updateFirstName(userId, newFirstName);
    }
    
    @Test
    void shouldReturnFalseWhenFirstNameUpdateFails() {
        Integer userId = 1;
        String newFirstName = "NewName";
        
        when(userRepository.updateFirstName(userId, newFirstName)).thenReturn(0);
        
        boolean result = userService.updateFirstName(userId, newFirstName);
        
        assertFalse(result);
    }
    
    @Test
    void shouldUpdateLastName() {
        Integer userId = 1;
        String newLastName = "NewLastName";
        
        when(userRepository.updateLastName(userId, newLastName)).thenReturn(1);
        
        boolean result = userService.updateLastName(userId, newLastName);
        
        assertTrue(result);
        verify(userRepository).updateLastName(userId, newLastName);
    }
    
    @Test
    void shouldUpdateEmail() {
        Integer userId = 1;
        String newEmail = "new.email@example.com";
        
        when(userRepository.findByEmail(newEmail)).thenReturn(Optional.empty());
        when(userRepository.updateEmail(userId, newEmail)).thenReturn(1);
        
        boolean result = userService.updateEmail(userId, newEmail);
        
        assertTrue(result);
        verify(userRepository).updateEmail(userId, newEmail);
    }
    
    @Test
    void shouldNotUpdateEmailWhenAlreadyExists() {
        Integer userId = 1;
        String newEmail = "existing@example.com";
        
        User existingUser = new User();
        existingUser.setId(2); // Different user with that email
        
        when(userRepository.findByEmail(newEmail)).thenReturn(Optional.of(existingUser));
        
        boolean result = userService.updateEmail(userId, newEmail);
        
        assertFalse(result);
        verify(userRepository, never()).updateEmail(any(), any());
    }
    
    @Test
    void shouldUpdatePassword() {
        Integer userId = 1;
        String newPassword = "newPassword123";
        String encodedPassword = "encodedNewPassword";
        
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);
        when(userRepository.updatePassword(userId, encodedPassword)).thenReturn(1);
        
        boolean result = userService.updatePassword(userId, newPassword);
        
        assertTrue(result);
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).updatePassword(userId, encodedPassword);
    }
    
    @Test
    void shouldCheckIfUserExists() {
        String email = "john.doe@example.com";
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        
        boolean result = userService.checkIfExists(email);
        
        assertTrue(result);
        verify(userRepository).findByEmail(email);
    }
    
    @Test
    void shouldReturnFalseWhenUserEmailDoesNotExist() {
        String email = "nonexistent@example.com";
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        
        boolean result = userService.checkIfExists(email);
        
        assertFalse(result);
    }
}