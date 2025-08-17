package com.scms.service;

import com.scms.entity.AdminNotification;
import com.scms.entity.Administrator;
import com.scms.entity.Enrollment;
import com.scms.entity.Student;
import com.scms.repository.AdminNotificationRepository;
import com.scms.repository.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminNotificationService {
    
    @Autowired
    private AdminNotificationRepository adminNotificationRepository;
    
    @Autowired
    private AdministratorRepository administratorRepository;
    
    public List<AdminNotification> getAllAdminNotifications() {
        return adminNotificationRepository.findAllByOrderBySentDateDesc();
    }
    
    public List<AdminNotification> getAdminNotifications(Administrator admin) {
        return adminNotificationRepository.findByAdminOrderBySentDateDesc(admin);
    }
    
    public List<AdminNotification> getUnreadAdminNotifications(Administrator admin) {
        return adminNotificationRepository.findByAdminAndReadFalseOrderBySentDateDesc(admin);
    }
    
    public long getUnreadCount(Administrator admin) {
        return adminNotificationRepository.countUnreadByAdmin(admin);
    }
    
    public AdminNotification createNotification(String message, Administrator admin, AdminNotification.AdminNotificationType type) {
        AdminNotification notification = new AdminNotification(message, admin, type);
        return adminNotificationRepository.save(notification);
    }
    
    public AdminNotification createNotification(String message, Administrator admin, AdminNotification.AdminNotificationType type, Long relatedEntityId, String relatedEntityType) {
        AdminNotification notification = new AdminNotification(message, admin, type, relatedEntityId, relatedEntityType);
        return adminNotificationRepository.save(notification);
    }
    
    public void markAsRead(Long notificationId) {
        adminNotificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            adminNotificationRepository.save(notification);
        });
    }
    
    public void markAllAsRead(Administrator admin) {
        List<AdminNotification> unreadNotifications = getUnreadAdminNotifications(admin);
        for (AdminNotification notification : unreadNotifications) {
            notification.setRead(true);
        }
        adminNotificationRepository.saveAll(unreadNotifications);
    }
    
    // Specific notification creation methods
    public void notifyPendingEnrollmentRequest(Enrollment enrollment) {
        // Notify all admins about pending enrollment
        List<Administrator> admins = administratorRepository.findAll();
        for (Administrator admin : admins) {
            String message = String.format("New enrollment request from %s for course: %s", 
                enrollment.getStudent().getName(), 
                enrollment.getCourse().getTitle());
            createNotification(message, admin, AdminNotification.AdminNotificationType.PENDING_ENROLLMENT_REQUEST, 
                enrollment.getId(), "ENROLLMENT");
        }
    }
    
    public void notifyEnrollmentApproved(Enrollment enrollment, Administrator admin) {
        String message = String.format("Enrollment approved for %s in course: %s", 
            enrollment.getStudent().getName(), 
            enrollment.getCourse().getTitle());
        createNotification(message, admin, AdminNotification.AdminNotificationType.ENROLLMENT_APPROVED, 
            enrollment.getId(), "ENROLLMENT");
    }
    
    public void notifyEnrollmentRejected(Enrollment enrollment, Administrator admin) {
        String message = String.format("Enrollment rejected for %s in course: %s", 
            enrollment.getStudent().getName(), 
            enrollment.getCourse().getTitle());
        createNotification(message, admin, AdminNotification.AdminNotificationType.ENROLLMENT_REJECTED, 
            enrollment.getId(), "ENROLLMENT");
    }
    
    public void notifyWithdrawalRequest(Enrollment enrollment) {
        // Notify all admins about withdrawal
        List<Administrator> admins = administratorRepository.findAll();
        for (Administrator admin : admins) {
            String message = String.format("Student %s has withdrawn from course: %s", 
                enrollment.getStudent().getName(), 
                enrollment.getCourse().getTitle());
            createNotification(message, admin, AdminNotification.AdminNotificationType.WITHDRAWAL_APPROVED, 
                enrollment.getId(), "ENROLLMENT");
        }
    }
    
    public void notifyGradeAdded(Student student, String courseTitle, Administrator admin) {
        String message = String.format("Grade added for %s in course: %s", 
            student.getName(), courseTitle);
        createNotification(message, admin, AdminNotification.AdminNotificationType.GRADE_ADDED, 
            student.getId(), "STUDENT");
    }
    
    public void notifyGradeUpdated(Student student, String courseTitle, Administrator admin) {
        String message = String.format("Grade updated for %s in course: %s", 
            student.getName(), courseTitle);
        createNotification(message, admin, AdminNotification.AdminNotificationType.GRADE_UPDATED, 
            student.getId(), "STUDENT");
    }
    
    public void notifyCourseCreated(String courseTitle, Administrator admin) {
        String message = String.format("New course created: %s", courseTitle);
        createNotification(message, admin, AdminNotification.AdminNotificationType.COURSE_CREATED);
    }
    
    public void notifyCourseUpdated(String courseTitle, Administrator admin) {
        String message = String.format("Course updated: %s", courseTitle);
        createNotification(message, admin, AdminNotification.AdminNotificationType.COURSE_UPDATED);
    }
}
