package com.example.user_service.dto;

import jakarta.validation.constraints.Size;

public class UpdateUserRequest {
    
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;
    
    private Integer stationId;
    
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    // Getters and Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public Integer getStationId() { return stationId; }
    public void setStationId(Integer stationId) { this.stationId = stationId; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
