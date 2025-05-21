package com.backend.service;

import com.backend.dto.RegisterRequest;
import com.backend.model.User;
import com.backend.repository.UserRepository;
import com.backend.config.JwtUtil; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Collection;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;  // Injectam JwtUtil

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Optional<User> checkCredentials(String email, String rawPassword) {
        try {
            logger.info("Attempting to check credentials for email: {}", email);
            Optional<User> userOpt = userRepository.findByEmail(email);

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                boolean passwordMatches = passwordEncoder.matches(rawPassword, user.getPassword());
                logger.info("Password match result for {}: {}", email, passwordMatches);
                return passwordMatches ? userOpt : Optional.empty();
            }
            logger.info("No user found for email: {}", email);
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error checking credentials for email: {}", email, e);
            throw e;
        }
    }

    public Collection<User> getAllUsers() {
        return userRepository.allUsers();
    }

    public boolean checkUserById(Integer id) {
        return userRepository.existsById(id);
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }


    public String generateJwtForUser(String email)
    {
        try {
            logger.info("Generating JWT token for user: {}", email);
            return jwtUtil.generateToken(email);
        } catch (Exception e) {
            logger.error("Error generating JWT token for user: {}", email, e);
            throw e;
        }
    }

    public boolean checkIfExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public void createUser(RegisterRequest req) {
        try {
            logger.info("Creating new user with email: {}", req.getEmail());
            User u = new User();
            u.setFirstName(req.getFirstName());
            u.setLastName(req.getLastName());
            u.setEmail(req.getEmail());
            u.setRole(req.getRole());
            u.setPassword(passwordEncoder.encode(req.getPassword()));
            userRepository.save(u);
            logger.info("User created successfully: {}", req.getEmail());
        } catch (Exception e) {
            logger.error("Error creating user: {}", req.getEmail(), e);
            throw e;
        }
    }
    
    public boolean updateFirstName(Integer id, String firstName) {
        try {
            logger.info("Updating firstName for user with id: {}", id);
            int rowsUpdated = userRepository.updateFirstName(id, firstName);
            boolean success = rowsUpdated > 0;
            if (success) {
                logger.info("FirstName updated successfully for user with id: {}", id);
            } else {
                logger.warn("Failed to update firstName for user with id: {}", id);
            }
            return success;
        } catch (Exception e) {
            logger.error("Error updating firstName for user with id: {}", id, e);
            throw e;
        }
    }
    
    public boolean updateLastName(Integer id, String lastName) {
        try {
            logger.info("Updating lastName for user with id: {}", id);
            int rowsUpdated = userRepository.updateLastName(id, lastName);
            boolean success = rowsUpdated > 0;
            if (success) {
                logger.info("LastName updated successfully for user with id: {}", id);
            } else {
                logger.warn("Failed to update lastName for user with id: {}", id);
            }
            return success;
        } catch (Exception e) {
            logger.error("Error updating lastName for user with id: {}", id, e);
            throw e;
        }
    }
    
    public boolean updateEmail(Integer id, String email) {
        try {
            // First check if the email is already used by another user
            Optional<User> existingUser = userRepository.findByEmail(email);
            if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
                logger.warn("Email {} is already in use by another user", email);
                return false;
            }
            
            logger.info("Updating email for user with id: {}", id);
            int rowsUpdated = userRepository.updateEmail(id, email);
            boolean success = rowsUpdated > 0;
            if (success) {
                logger.info("Email updated successfully for user with id: {}", id);
            } else {
                logger.warn("Failed to update email for user with id: {}", id);
            }
            return success;
        } catch (Exception e) {
            logger.error("Error updating email for user with id: {}", id, e);
            throw e;
        }
    }
    
    public boolean updatePassword(Integer id, String rawPassword) {
        try {
            logger.info("Updating password for user with id: {}", id);
            String encodedPassword = passwordEncoder.encode(rawPassword);
            int rowsUpdated = userRepository.updatePassword(id, encodedPassword);
            boolean success = rowsUpdated > 0;
            if (success) {
                logger.info("Password updated successfully for user with id: {}", id);
            } else {
                logger.warn("Failed to update password for user with id: {}", id);
            }
            return success;
        } catch (Exception e) {
            logger.error("Error updating password for user with id: {}", id, e);
            throw e;
        }
    }
    
}
