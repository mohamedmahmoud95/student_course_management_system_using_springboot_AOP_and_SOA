package com.scms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "admin_notifications")
public class AdminNotification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String message;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    @JsonIgnore
    private Administrator admin;
    
    @Column(nullable = false)
    private LocalDateTime sentDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminNotificationType type;
    
    @Column(name = "is_read", nullable = false)
    private boolean read = false;
    
    // Reference to the related entity (enrollment, etc.)
    @Column
    private Long relatedEntityId;
    
    @Column
    private String relatedEntityType;
    
    public enum AdminNotificationType {
        PENDING_ENROLLMENT_REQUEST,    // Student wants to enroll
        PENDING_WITHDRAWAL_REQUEST,    // Student wants to withdraw
        ENROLLMENT_APPROVED,           // Admin approved enrollment
        ENROLLMENT_REJECTED,           // Admin rejected enrollment
        WITHDRAWAL_APPROVED,           // Admin approved withdrawal
        WITHDRAWAL_REJECTED,           // Admin rejected withdrawal
        GRADE_ADDED,                   // Admin added a grade
        GRADE_UPDATED,                 // Admin updated a grade
        COURSE_CREATED,                // Admin created a course
        COURSE_UPDATED,                // Admin updated a course
        SYSTEM_ALERT                   // System-level alerts for admin
    }
    
    public AdminNotification() {
        this.sentDate = LocalDateTime.now();
    }
    
    public AdminNotification(String message, Administrator admin, AdminNotificationType type) {
        this();
        this.message = message;
        this.admin = admin;
        this.type = type;
    }
    
    public AdminNotification(String message, Administrator admin, AdminNotificationType type, Long relatedEntityId, String relatedEntityType) {
        this(message, admin, type);
        this.relatedEntityId = relatedEntityId;
        this.relatedEntityType = relatedEntityType;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Administrator getAdmin() {
        return admin;
    }
    
    public void setAdmin(Administrator admin) {
        this.admin = admin;
    }
    
    public LocalDateTime getSentDate() {
        return sentDate;
    }
    
    public void setSentDate(LocalDateTime sentDate) {
        this.sentDate = sentDate;
    }
    
    public AdminNotificationType getType() {
        return type;
    }
    
    public void setType(AdminNotificationType type) {
        this.type = type;
    }
    
    public boolean isRead() {
        return read;
    }
    
    public void setRead(boolean read) {
        this.read = read;
    }
    
    public Long getRelatedEntityId() {
        return relatedEntityId;
    }
    
    public void setRelatedEntityId(Long relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
    }
    
    public String getRelatedEntityType() {
        return relatedEntityType;
    }
    
    public void setRelatedEntityType(String relatedEntityType) {
        this.relatedEntityType = relatedEntityType;
    }
}
