package com.scms.repository;

import com.scms.entity.AdminNotification;
import com.scms.entity.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminNotificationRepository extends JpaRepository<AdminNotification, Long> {
    
    List<AdminNotification> findByAdminOrderBySentDateDesc(Administrator admin);
    
    List<AdminNotification> findByAdminAndReadFalseOrderBySentDateDesc(Administrator admin);
    
    @Query("SELECT COUNT(n) FROM AdminNotification n WHERE n.admin = ?1 AND n.read = false")
    long countUnreadByAdmin(Administrator admin);
    
    List<AdminNotification> findByType(AdminNotification.AdminNotificationType type);
    
    List<AdminNotification> findByAdminAndTypeOrderBySentDateDesc(Administrator admin, AdminNotification.AdminNotificationType type);
    
    List<AdminNotification> findAllByOrderBySentDateDesc();
}
