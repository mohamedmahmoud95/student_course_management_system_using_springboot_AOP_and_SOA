package com.scms.repository;

import com.scms.entity.Notification;
import com.scms.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByRecipient(Student recipient);
    
    List<Notification> findByRecipientOrderBySentDateDesc(Student recipient);
    
    @Query("SELECT n FROM Notification n WHERE n.recipient = :recipient AND n.read = false")
    List<Notification> findUnreadNotificationsByRecipient(@Param("recipient") Student recipient);
    
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.recipient = :recipient AND n.read = false")
    long countUnreadNotificationsByRecipient(@Param("recipient") Student recipient);
    
    List<Notification> findAllByOrderBySentDateDesc();
    
    List<Notification> findByReadFalse();
}
