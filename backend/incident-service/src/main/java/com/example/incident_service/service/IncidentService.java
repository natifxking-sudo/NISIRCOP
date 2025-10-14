package com.example.incident_service.service;

import com.example.incident_service.dto.CreateIncidentRequest;
import com.example.incident_service.dto.IncidentDTO;
import com.example.incident_service.dto.LocationDTO;
import com.example.incident_service.entity.Incident;
import com.example.incident_service.entity.IncidentPriority;
import com.example.incident_service.repository.IncidentRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class IncidentService {
    
    private static final Logger log = LoggerFactory.getLogger(IncidentService.class);
    
    @Autowired
    private IncidentRepository incidentRepository;
    
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    
    @Transactional(readOnly = true)
    public List<IncidentDTO> getAllIncidents() {
        log.debug("Fetching all incidents");
        try {
            List<Incident> incidents = incidentRepository.findAll();
            log.debug("Found {} incidents", incidents.size());
            
            List<IncidentDTO> result = new ArrayList<>();
            for (Incident incident : incidents) {
                try {
                    IncidentDTO dto = convertToDTO(incident);
                    result.add(dto);
                } catch (Exception e) {
                    log.error("Error converting incident {}: {}", incident.getId(), e.getMessage());
                    // Skip this incident and continue
                }
            }
            return result;
        } catch (Exception e) {
            log.error("Error fetching incidents", e);
            return new ArrayList<>(); // Return empty list instead of throwing
        }
    }
    
    @Transactional(readOnly = true)
    public IncidentDTO getIncidentById(Long id) {
        log.debug("Fetching incident by id: {}", id);
        Incident incident = incidentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Incident not found with id: " + id));
        return convertToDTO(incident);
    }
    
    @Transactional(readOnly = true)
    public List<IncidentDTO> getIncidentsByReporter(Long reporterId) {
        log.debug("Fetching incidents by reporter: {}", reporterId);
        return incidentRepository.findByReportedBy(reporterId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<IncidentDTO> getIncidentsByType(String type) {
        log.debug("Fetching incidents by type: {}", type);
        return incidentRepository.findByIncidentType(type).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<IncidentDTO> getIncidentsByPriority(IncidentPriority priority) {
        log.debug("Fetching incidents by priority: {}", priority);
        return incidentRepository.findByPriority(priority.name()).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<IncidentDTO> getIncidentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Fetching incidents between {} and {}", startDate, endDate);
        return incidentRepository.findByDateRange(startDate, endDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<IncidentDTO> getIncidentsNearLocation(double latitude, double longitude, double radiusMeters) {
        log.debug("Fetching incidents near ({}, {}) within {} meters", latitude, longitude, radiusMeters);
        try {
            return incidentRepository.findNearLocation(latitude, longitude, radiusMeters).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error in spatial query", e);
            return new ArrayList<>();
        }
    }
    
    public IncidentDTO createIncident(CreateIncidentRequest request) {
        log.info("Creating new incident: {}", request.getTitle());
        
        try {
            Incident incident = new Incident();
            incident.setTitle(request.getTitle());
            incident.setDescription(request.getDescription());
            incident.setIncidentType(request.getIncidentType());
            incident.setPriority(request.getPriority().name());
            incident.setLocation(createPoint(request.getLatitude(), request.getLongitude()));
            incident.setReportedBy(request.getReportedBy());
            incident.setOccurredAt(request.getOccurredAt());
            
            Incident saved = incidentRepository.save(incident);
            log.info("Incident created successfully: id={}", saved.getId());
            
            return convertToDTO(saved);
        } catch (Exception e) {
            log.error("Error creating incident", e);
            throw new RuntimeException("Failed to create incident: " + e.getMessage());
        }
    }
    
    public IncidentDTO updateIncidentStatus(Long id, String status) {
        log.info("Updating incident status: id={}, status={}", id, status);
        
        Incident incident = incidentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Incident not found with id: " + id));
        
        incident.setStatus(status);
        Incident updated = incidentRepository.save(incident);
        
        log.info("Incident status updated successfully: id={}", updated.getId());
        return convertToDTO(updated);
    }
    
    public void deleteIncident(Long id) {
        log.info("Deleting incident: {}", id);
        
        if (!incidentRepository.existsById(id)) {
            throw new RuntimeException("Incident not found with id: " + id);
        }
        
        incidentRepository.deleteById(id);
        log.info("Incident deleted successfully: id={}", id);
    }
    
    private Point createPoint(double latitude, double longitude) {
        try {
            Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
            point.setSRID(4326);
            return point;
        } catch (Exception e) {
            log.error("Error creating point: lat={}, lng={}", latitude, longitude, e);
            throw new RuntimeException("Invalid coordinates");
        }
    }
    
    private IncidentDTO convertToDTO(Incident incident) {
        try {
            LocationDTO location = null;
            if (incident.getLocation() != null) {
                location = new LocationDTO(
                    incident.getLocation().getY(),  // Latitude
                    incident.getLocation().getX()   // Longitude
                );
            }
            
            return new IncidentDTO(
                incident.getId(),
                incident.getTitle(),
                incident.getDescription(),
                incident.getIncidentType(),
                incident.getPriority(),
                location,
                incident.getReportedBy(),
                incident.getCreatedAt(),
                incident.getOccurredAt(),
                incident.getStatus()
            );
        } catch (Exception e) {
            log.error("Error converting incident to DTO: id={}, error={}", 
                     incident != null ? incident.getId() : "null", e.getMessage());
            // Return a minimal DTO with error info
            return new IncidentDTO(
                incident.getId(),
                incident.getTitle() != null ? incident.getTitle() : "Error loading",
                "Error: " + e.getMessage(),
                incident.getIncidentType(),
                incident.getPriority(),
                null,
                incident.getReportedBy(),
                incident.getCreatedAt(),
                incident.getOccurredAt(),
                "ERROR"
            );
        }
    }
}
