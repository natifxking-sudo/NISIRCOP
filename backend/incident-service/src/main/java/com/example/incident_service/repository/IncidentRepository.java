package com.example.incident_service.repository;

import com.example.incident_service.entity.Incident;
import com.example.incident_service.entity.IncidentPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    
    List<Incident> findByReportedBy(Long reportedBy);
    
    List<Incident> findByIncidentType(String incidentType);
    
    List<Incident> findByPriority(String priority);
    
    @Query("SELECT i FROM Incident i WHERE i.occurredAt BETWEEN :startDate AND :endDate")
    List<Incident> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                    @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT i FROM Incident i WHERE i.incidentType = :type AND i.priority = :priority")
    List<Incident> findByTypeAndPriority(@Param("type") String type, 
                                         @Param("priority") IncidentPriority priority);
    
    @Query(value = """
        SELECT i.* FROM incidents i
        WHERE ST_DWithin(
            i.location::geography,
            ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography,
            :distanceMeters
        )
        ORDER BY ST_Distance(
            i.location::geography,
            ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography
        )
        """, nativeQuery = true)
    List<Incident> findNearLocation(@Param("lat") double latitude,
                                    @Param("lng") double longitude,
                                    @Param("distanceMeters") double distance);
}
