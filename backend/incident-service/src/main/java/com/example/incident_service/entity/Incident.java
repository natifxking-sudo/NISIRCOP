package com.example.incident_service.entity;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;
import java.time.LocalDateTime;

@Entity
@Table(name = "incidents")
public class Incident {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 255)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "incident_type", length = 100)
    private String incidentType;
    
    @Column(nullable = false, length = 20)
    private String priority;
    
    @Column(columnDefinition = "geometry(Point, 4326)", nullable = false)
    private Point location;
    
    @Column(name = "reported_by")
    private Long reportedBy;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "occurred_at")
    private LocalDateTime occurredAt;
    
    @Column(length = 50)
    private String status = "REPORTED";
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (occurredAt == null) {
            occurredAt = LocalDateTime.now();
        }
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
    
    public Point getLocation() { return location; }
    public void setLocation(Point location) { this.location = location; }
    
    public Long getReportedBy() { return reportedBy; }
    public void setReportedBy(Long reportedBy) { this.reportedBy = reportedBy; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getOccurredAt() { return occurredAt; }
    public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
