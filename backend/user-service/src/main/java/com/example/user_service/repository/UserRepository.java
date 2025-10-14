package com.example.user_service.repository;

import com.example.user_service.entity.User;
import com.example.user_service.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    
    Optional<User> findByUsername(String username);
    
    List<User> findByRole(String role);
    
    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findByRoleString(@Param("role") String role);
    
    List<User> findByStationId(Integer stationId);
    
    boolean existsByUsername(String username);
}
