package com.backend.mapper;

import com.backend.dto.UserDTO;
import com.backend.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void toDTO_WithValidUser_ShouldMapAllFields() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setRole("student");

        // Act
        UserDTO result = userMapper.toDTO(user);

        // Assert
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getRole(), result.getRole());
    }

    @Test
    void toDTO_WithNullUser_ShouldReturnNull() {
        // Act
        UserDTO result = userMapper.toDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_WithValidDTO_ShouldMapAllFields() {
        // Arrange
        UserDTO dto = new UserDTO();
        dto.setId(1);
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setRole("STUDENT");

        // Act
        User result = userMapper.toEntity(dto);

        // Assert
        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getFirstName(), result.getFirstName());
        assertEquals(dto.getLastName(), result.getLastName());
        assertEquals(dto.getEmail(), result.getEmail());
        assertEquals(dto.getRole(), result.getRole());
    }

    @Test
    void toEntity_WithNullDTO_ShouldReturnNull() {
        // Act
        User result = userMapper.toEntity(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toDTO_WithPartialUser_ShouldMapAvailableFields() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setEmail("john.doe@example.com");
        // firstName, lastName, and role are null

        // Act
        UserDTO result = userMapper.toDTO(user);

        // Assert
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getEmail(), result.getEmail());
        assertNull(result.getFirstName());
        assertNull(result.getLastName());
        assertNull(result.getRole());
    }

    @Test
    void toEntity_WithPartialDTO_ShouldMapAvailableFields() {
        // Arrange
        UserDTO dto = new UserDTO();
        dto.setId(1);
        dto.setEmail("john.doe@example.com");
        // firstName, lastName, and role are null

        // Act
        User result = userMapper.toEntity(dto);

        // Assert
        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getEmail(), result.getEmail());
        assertNull(result.getFirstName());
        assertNull(result.getLastName());
        assertNull(result.getRole());
    }
}