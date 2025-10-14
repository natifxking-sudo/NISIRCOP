package com.example.user_service.repository;

import com.example.user_service.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    @Transactional
    public User saveWithEnumCast(User user) {
        if (user.getId() == null) {
            // Insert
            String sql = "INSERT INTO users (username, password, role, full_name, station_id, created_at) " +
                        "VALUES (:username, :password, CAST(:role AS user_role), :fullName, :stationId, :createdAt) " +
                        "RETURNING id";
            
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter("username", user.getUsername());
            query.setParameter("password", user.getPassword());
            query.setParameter("role", user.getRole());
            query.setParameter("fullName", user.getFullName());
            query.setParameter("stationId", user.getStationId());
            query.setParameter("createdAt", LocalDateTime.now());
            
            Long id = ((Number) query.getSingleResult()).longValue();
            user.setId(id);
            user.setCreatedAt(LocalDateTime.now());
            
            return user;
        } else {
            // Update
            String sql = "UPDATE users SET full_name = :fullName, station_id = :stationId, " +
                        "password = :password WHERE id = :id";
            
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter("id", user.getId());
            query.setParameter("fullName", user.getFullName());
            query.setParameter("stationId", user.getStationId());
            query.setParameter("password", user.getPassword());
            query.executeUpdate();
            
            return user;
        }
    }
}
