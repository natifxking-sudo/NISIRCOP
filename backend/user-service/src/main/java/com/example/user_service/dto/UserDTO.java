package com.example.user_service.dto;

import java.time.LocalDateTime;

public class UserDTO {
    private Long id;
    private String username;
    private String role;
    private String fullName;
    private Integer stationId;
    private LocalDateTime createdAt;
    
    public UserDTO() {}
    
    public UserDTO(Long id, String username, String role, String fullName, Integer stationId, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.fullName = fullName;
        this.stationId = stationId;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public Integer getStationId() { return stationId; }
    public void setStationId(Integer stationId) { this.stationId = stationId; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
