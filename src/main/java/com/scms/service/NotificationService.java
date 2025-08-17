package com.scms.service;

import com.scms.entity.Notification;
import com.scms.entity.Student;
import com.scms.repository.NotificationRepository;
import com.scms.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    public Notification sendNotification(Long studentId, String message, Notification.NotificationType type) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) {
            throw new RuntimeException("Student not found");
        }
        
        Notification notification = new Notification(message, studentOpt.get(), type);
        return notificationRepository.save(notification);
    }
    
    public List<Notification> getStudentNotifications(Long studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isPresent()) {
            return notificationRepository.findByRecipientOrderBySentDateDesc(studentOpt.get());
        }
        return List.of();
    }
    
    public List<Notification> getUnreadNotifications(Long studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isPresent()) {
            return notificationRepository.findUnreadNotificationsByRecipient(studentOpt.get());
        }
        return List.of();
    }
    
    public long getUnreadCount(Long studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isPresent()) {
            return notificationRepository.countUnreadNotificationsByRecipient(studentOpt.get());
        }
        return 0;
    }
    
    public void markAsRead(Long notificationId) {
        Optional<Notification> notificationOpt = notificationRepository.findById(notificationId);
        if (notificationOpt.isPresent()) {
            Notification notification = notificationOpt.get();
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }
    
    public void markAllAsRead(Long studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isPresent()) {
            List<Notification> unreadNotifications = notificationRepository.findUnreadNotificationsByRecipient(studentOpt.get());
            for (Notification notification : unreadNotifications) {
                notification.setRead(true);
                notificationRepository.save(notification);
            }
        }
    }
    
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }
    
    public void deleteAllNotifications(Long studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isPresent()) {
            List<Notification> notifications = notificationRepository.findByRecipient(studentOpt.get());
            notificationRepository.deleteAll(notifications);
        }
    }
    
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAllByOrderBySentDateDesc();
    }
    
    public void markAllAsRead() {
        List<Notification> unreadNotifications = notificationRepository.findByReadFalse();
        for (Notification notification : unreadNotifications) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }
}
