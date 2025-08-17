package com.scms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String message;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    @JsonIgnore
    private Student recipient;
    
    @Column(nullable = false)
    private LocalDateTime sentDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;
    
    @Column(nullable = false)
    private boolean read = false;
    
    public enum NotificationType {
        ENROLLMENT, WITHDRAWAL, GRADE_UPDATE, COURSE_UPDATE, SYSTEM
    }
    
    public Notification() {
        this.sentDate = LocalDateTime.now();
    }
    
    public Notification(String message, Student recipient, NotificationType type) {
        this();
        this.message = message;
        this.recipient = recipient;
        this.type = type;
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
    
    public Student getRecipient() {
        return recipient;
    }
    
    public void setRecipient(Student recipient) {
        this.recipient = recipient;
    }
    
    public LocalDateTime getSentDate() {
        return sentDate;
    }
    
    public void setSentDate(LocalDateTime sentDate) {
        this.sentDate = sentDate;
    }
    
    public NotificationType getType() {
        return type;
    }
    
    public void setType(NotificationType type) {
        this.type = type;
    }
    
    public boolean isRead() {
        return read;
    }
    
    public void setRead(boolean read) {
        this.read = read;
    }
}
