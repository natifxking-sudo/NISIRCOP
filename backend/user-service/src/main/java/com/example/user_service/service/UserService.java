package com.example.user_service.service;

import com.example.user_service.dto.CreateUserRequest;
import com.example.user_service.dto.UpdateUserRequest;
import com.example.user_service.dto.UserDTO;
import com.example.user_service.entity.User;
import com.example.user_service.entity.UserRole;
import com.example.user_service.exception.UserAlreadyExistsException;
import com.example.user_service.exception.UserNotFoundException;
import com.example.user_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        log.debug("Fetching all users");
        return userRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        log.debug("Fetching user by id: {}", id);
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
        return convertToDTO(user);
    }
    
    @Transactional(readOnly = true)
    public UserDTO getUserByUsername(String username) {
        log.debug("Fetching user by username: {}", username);
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException(username));
        return convertToDTO(user);
    }
    
    @Transactional(readOnly = true)
    public List<UserDTO> getUsersByRole(UserRole role) {
        log.debug("Fetching users by role: {}", role);
        return userRepository.findByRoleString(role.name()).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<UserDTO> getUsersByStation(Integer stationId) {
        log.debug("Fetching users by station: {}", stationId);
        return userRepository.findByStationId(stationId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public UserDTO createUser(CreateUserRequest request) {
        log.info("Creating new user: {}", request.getUsername());
        
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException(request.getUsername());
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole().name());
        user.setFullName(request.getFullName());
        user.setStationId(request.getStationId());
        
        User saved = userRepository.saveWithEnumCast(user);
        log.info("User created successfully: id={}, username={}", saved.getId(), saved.getUsername());
        
        return convertToDTO(saved);
    }
    
    public UserDTO updateUser(Long id, UpdateUserRequest request) {
        log.info("Updating user: {}", id);
        
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
        
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getStationId() != null) {
            user.setStationId(request.getStationId());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        User updated = userRepository.save(user);
        log.info("User updated successfully: id={}", updated.getId());
        
        return convertToDTO(updated);
    }
    
    public void deleteUser(Long id) {
        log.info("Deleting user: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        
        userRepository.deleteById(id);
        log.info("User deleted successfully: id={}", id);
    }
    
    private UserDTO convertToDTO(User user) {
        return new UserDTO(
            user.getId(),
            user.getUsername(),
            user.getRole(),
            user.getFullName(),
            user.getStationId(),
            user.getCreatedAt()
        );
    }
}
