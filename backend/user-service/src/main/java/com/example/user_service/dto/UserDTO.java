package com.example.user_service.dto;

import com.example.user_service.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private UserRole role;
    private String fullName;
    private Long stationId;
    private LocalDateTime createdAt;
}
