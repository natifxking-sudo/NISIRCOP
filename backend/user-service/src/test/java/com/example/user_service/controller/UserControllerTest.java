package com.example.user_service.controller;

import com.example.user_service.dto.CreateUserRequest;
import com.example.user_service.dto.UserDTO;
import com.example.user_service.model.UserRole;
import com.example.user_service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private UserDTO testUserDTO;
    
    @BeforeEach
    void setUp() {
        testUserDTO = new UserDTO();
        testUserDTO.setId(1L);
        testUserDTO.setUsername("testuser");
        testUserDTO.setRole(UserRole.OFFICER);
        testUserDTO.setFullName("Test User");
        testUserDTO.setStationId(1L);
        testUserDTO.setCreatedAt(LocalDateTime.now());
    }
    
    @Test
    void getAllUsers_ShouldReturnUserList() throws Exception {
        // Arrange
        List<UserDTO> users = Arrays.asList(testUserDTO);
        when(userService.findAll()).thenReturn(users);
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testuser"))
                .andExpect(jsonPath("$[0].role").value("OFFICER"));
    }
    
    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        // Arrange
        when(userService.findById(1L)).thenReturn(testUserDTO);
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.id").value(1));
    }
    
    @Test
    void getUserByUsername_ShouldReturnUser() throws Exception {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(testUserDTO);
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/users/username/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }
    
    @Test
    void createUser_WithValidRequest_ShouldReturnCreated() throws Exception {
        // Arrange
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("newuser");
        request.setPassword("password123");
        request.setRole(UserRole.OFFICER);
        request.setFullName("New User");
        request.setStationId(1L);
        
        when(userService.create(any(CreateUserRequest.class))).thenReturn(testUserDTO);
        
        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"));
    }
    
    @Test
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        // Arrange
        when(userService.update(eq(1L), any(UserDTO.class))).thenReturn(testUserDTO);
        
        // Act & Assert
        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }
    
    @Test
    void deleteUser_ShouldReturnNoContent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void health_ShouldReturnHealthyStatus() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/users/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("User Service is healthy"));
    }
}
