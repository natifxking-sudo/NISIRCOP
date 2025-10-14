package com.example.incident_service.dto;

import java.time.LocalDateTime;

public class IncidentDTO {
    private Long id;
    private String title;
    private String description;
    private String incidentType;
    private String priority;
    private LocationDTO location;
    private Long reportedBy;
    private LocalDateTime createdAt;
    private LocalDateTime occurredAt;
    private String status;
    
    public IncidentDTO() {}
    
    public IncidentDTO(Long id, String title, String description, String incidentType, 
                       String priority, LocationDTO location, Long reportedBy, 
                       LocalDateTime createdAt, LocalDateTime occurredAt, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.incidentType = incidentType;
        this.priority = priority;
        this.location = location;
        this.reportedBy = reportedBy;
        this.createdAt = createdAt;
        this.occurredAt = occurredAt;
        this.status = status;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getIncidentType() { return incidentType; }
    public void setIncidentType(String incidentType) { this.incidentType = incidentType; }
    
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    
    public LocationDTO getLocation() { return location; }
    public void setLocation(LocationDTO location) { this.location = location; }
    
    public Long getReportedBy() { return reportedBy; }
    public void setReportedBy(Long reportedBy) { this.reportedBy = reportedBy; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getOccurredAt() { return occurredAt; }
    public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
