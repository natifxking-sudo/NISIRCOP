package com.example.user_service.repository;

import com.example.user_service.model.User;
import com.example.user_service.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByRole(UserRole role);
    List<User> findByStationId(Long stationId);
    boolean existsByUsername(String username);
}
