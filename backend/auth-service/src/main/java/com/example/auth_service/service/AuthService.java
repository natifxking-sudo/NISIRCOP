package com.example.auth_service.service;

import com.example.auth_service.dto.LoginRequest;
import com.example.auth_service.dto.LoginResponse;
import com.example.auth_service.entity.User;
import com.example.auth_service.repository.UserRepository;
import com.example.auth_service.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    
    public LoginResponse login(LoginRequest request) {
        log.info("Login attempt for username: {}", request.getUsername());
        
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.warn("User not found: {}", request.getUsername());
                    return new RuntimeException("Invalid username or password");
                });
        
        log.debug("User found: {}, checking password...", user.getUsername());
        log.debug("Stored hash starts with: {}", user.getPassword().substring(0, 20));
        log.debug("Password length: {}", request.getPassword().length());
        
        boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        log.debug("Password matches: {}", matches);
        
        if (!matches) {
            log.warn("Failed login attempt for username: {} - password mismatch", request.getUsername());
            throw new RuntimeException("Invalid username or password");
        }
        
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        
        log.info("Login successful for username: {}, userId: {}", request.getUsername(), user.getId());
        
        return new LoginResponse(
                token,
                jwtUtil.getExpirationTime(),
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
    
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
    
    public String getUsernameFromToken(String token) {
        return jwtUtil.getUsernameFromToken(token);
    }
}
