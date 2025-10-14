package com.example.incident_service.controller;

import com.example.incident_service.dto.CreateIncidentRequest;
import com.example.incident_service.dto.IncidentDTO;
import com.example.incident_service.entity.IncidentPriority;
import com.example.incident_service.service.IncidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/incidents")
@RequiredArgsConstructor
@Tag(name = "Incident Management", description = "APIs for managing crime incidents")
public class IncidentController {
    
    private final IncidentService incidentService;
    
    @GetMapping
    @Operation(summary = "Get all incidents")
    public ResponseEntity<List<IncidentDTO>> getAllIncidents() {
        List<IncidentDTO> incidents = incidentService.getAllIncidents();
        return ResponseEntity.ok(incidents);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get incident by ID")
    public ResponseEntity<IncidentDTO> getIncidentById(@PathVariable Long id) {
        IncidentDTO incident = incidentService.getIncidentById(id);
        return ResponseEntity.ok(incident);
    }
    
    @GetMapping("/reporter/{reporterId}")
    @Operation(summary = "Get incidents by reporter")
    public ResponseEntity<List<IncidentDTO>> getIncidentsByReporter(@PathVariable Long reporterId) {
        List<IncidentDTO> incidents = incidentService.getIncidentsByReporter(reporterId);
        return ResponseEntity.ok(incidents);
    }
    
    @GetMapping("/type/{type}")
    @Operation(summary = "Get incidents by type")
    public ResponseEntity<List<IncidentDTO>> getIncidentsByType(@PathVariable String type) {
        List<IncidentDTO> incidents = incidentService.getIncidentsByType(type);
        return ResponseEntity.ok(incidents);
    }
    
    @GetMapping("/priority/{priority}")
    @Operation(summary = "Get incidents by priority")
    public ResponseEntity<List<IncidentDTO>> getIncidentsByPriority(@PathVariable IncidentPriority priority) {
        List<IncidentDTO> incidents = incidentService.getIncidentsByPriority(priority);
        return ResponseEntity.ok(incidents);
    }
    
    @GetMapping("/daterange")
    @Operation(summary = "Get incidents by date range")
    public ResponseEntity<List<IncidentDTO>> getIncidentsByDateRange(
            @Parameter(description = "Start date (ISO format)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date (ISO format)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<IncidentDTO> incidents = incidentService.getIncidentsByDateRange(startDate, endDate);
        return ResponseEntity.ok(incidents);
    }
    
    @GetMapping("/near")
    @Operation(summary = "Get incidents near location", description = "Find incidents within specified radius of a location")
    public ResponseEntity<List<IncidentDTO>> getIncidentsNearLocation(
            @Parameter(description = "Latitude") @RequestParam double latitude,
            @Parameter(description = "Longitude") @RequestParam double longitude,
            @Parameter(description = "Radius in meters") @RequestParam(defaultValue = "1000") double radiusMeters) {
        List<IncidentDTO> incidents = incidentService.getIncidentsNearLocation(latitude, longitude, radiusMeters);
        return ResponseEntity.ok(incidents);
    }
    
    @PostMapping
    @Operation(summary = "Create new incident")
    public ResponseEntity<IncidentDTO> createIncident(@Valid @RequestBody CreateIncidentRequest request) {
        IncidentDTO created = incidentService.createIncident(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PatchMapping("/{id}/status")
    @Operation(summary = "Update incident status")
    public ResponseEntity<IncidentDTO> updateIncidentStatus(
            @PathVariable Long id,
            @Parameter(description = "New status") @RequestParam String status) {
        IncidentDTO updated = incidentService.updateIncidentStatus(id, status);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete incident")
    public ResponseEntity<Void> deleteIncident(@PathVariable Long id) {
        incidentService.deleteIncident(id);
        return ResponseEntity.noContent().build();
    }
}
