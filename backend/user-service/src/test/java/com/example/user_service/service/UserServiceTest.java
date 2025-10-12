package com.example.user_service.service;

import com.example.user_service.dto.CreateUserRequest;
import com.example.user_service.dto.UserDTO;
import com.example.user_service.exception.ResourceNotFoundException;
import com.example.user_service.exception.UserAlreadyExistsException;
import com.example.user_service.model.User;
import com.example.user_service.model.UserRole;
import com.example.user_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    private User testUser;
    
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("hashedpassword");
        testUser.setRole(UserRole.OFFICER);
        testUser.setFullName("Test User");
        testUser.setStationId(1L);
        testUser.setCreatedAt(LocalDateTime.now());
    }
    
    @Test
    void findAll_ShouldReturnAllUsers() {
        // Arrange
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findAll()).thenReturn(users);
        
        // Act
        List<UserDTO> result = userService.findAll();
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUsername());
        verify(userRepository, times(1)).findAll();
    }
    
    @Test
    void findById_WithValidId_ShouldReturnUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        
        // Act
        UserDTO result = userService.findById(1L);
        
        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }
    
    @Test
    void findById_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.findById(999L));
        verify(userRepository, times(1)).findById(999L);
    }
    
    @Test
    void findByUsername_WithValidUsername_ShouldReturnUser() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        
        // Act
        UserDTO result = userService.findByUsername("testuser");
        
        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }
    
    @Test
    void findByUsername_WithInvalidUsername_ShouldThrowException() {
        // Arrange
        when(userRepository.findByUsername("invalid")).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.findByUsername("invalid"));
    }
    
    @Test
    void create_WithValidRequest_ShouldCreateUser() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("newuser");
        request.setPassword("password123");
        request.setRole(UserRole.OFFICER);
        request.setFullName("New User");
        request.setStationId(1L);
        
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        // Act
        UserDTO result = userService.create(request);
        
        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).existsByUsername("newuser");
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    void create_WithExistingUsername_ShouldThrowException() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("existinguser");
        request.setPassword("password123");
        request.setRole(UserRole.OFFICER);
        
        when(userRepository.existsByUsername("existinguser")).thenReturn(true);
        
        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> userService.create(request));
        verify(userRepository, times(1)).existsByUsername("existinguser");
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    void update_WithValidId_ShouldUpdateUser() {
        // Arrange
        UserDTO updateDTO = new UserDTO();
        updateDTO.setFullName("Updated Name");
        updateDTO.setRole(UserRole.POLICE_STATION);
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        // Act
        UserDTO result = userService.update(1L, updateDTO);
        
        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    void delete_WithValidId_ShouldDeleteUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).delete(any(User.class));
        
        // Act
        userService.delete(1L);
        
        // Assert
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(testUser);
    }
    
    @Test
    void delete_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.delete(999L));
        verify(userRepository, times(1)).findById(999L);
        verify(userRepository, never()).delete(any(User.class));
    }
    
    @Test
    void findByRole_ShouldReturnUsersWithRole() {
        // Arrange
        List<User> officers = Arrays.asList(testUser);
        when(userRepository.findByRole(UserRole.OFFICER)).thenReturn(officers);
        
        // Act
        List<UserDTO> result = userService.findByRole(UserRole.OFFICER);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(UserRole.OFFICER, result.get(0).getRole());
        verify(userRepository, times(1)).findByRole(UserRole.OFFICER);
    }
}
