package com.scms.service;

import com.scms.entity.Enrollment;
import com.scms.entity.Student;
import com.scms.entity.Course;
import com.scms.entity.Administrator;
import com.scms.entity.Notification;
import com.scms.entity.AdminNotification;
import com.scms.repository.EnrollmentRepository;
import com.scms.repository.StudentRepository;
import com.scms.repository.CourseRepository;
import com.scms.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EnrollmentService {
    
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private AdminNotificationService adminNotificationService;
    
    public Enrollment enrollStudent(Long studentId, Long courseId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        
        if (studentOpt.isEmpty() || courseOpt.isEmpty()) {
            throw new RuntimeException("Student or course not found");
        }
        
        Student student = studentOpt.get();
        Course course = courseOpt.get();
        
        if (!course.hasAvailableSeats()) {
            throw new RuntimeException("Course is full");
        }
        
        Optional<Enrollment> existingEnrollment = enrollmentRepository.findByStudentAndCourse(student, course);
        if (existingEnrollment.isPresent()) {
            throw new RuntimeException("Student is already enrolled in this course");
        }
        
        Enrollment enrollment = new Enrollment(student, course);
        enrollment = enrollmentRepository.save(enrollment);
        
        // Send notification to student
        Notification studentNotification = new Notification(
            "Enrollment request submitted for " + course.getTitle() + ". Waiting for admin approval.",
            student,
            Notification.NotificationType.ENROLLMENT
        );
        notificationRepository.save(studentNotification);
        
        // Send notification to all admins about pending enrollment request
        adminNotificationService.notifyPendingEnrollmentRequest(enrollment);
        
        return enrollment;
    }
    
    public void withdrawStudent(Long studentId, Long courseId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        
        if (studentOpt.isEmpty() || courseOpt.isEmpty()) {
            throw new RuntimeException("Student or course not found");
        }
        
        Student student = studentOpt.get();
        Course course = courseOpt.get();
        
        Optional<Enrollment> enrollmentOpt = enrollmentRepository.findByStudentAndCourse(student, course);
        if (enrollmentOpt.isEmpty()) {
            throw new RuntimeException("Student is not enrolled in this course");
        }
        
        Enrollment enrollment = enrollmentOpt.get();
        
        // Check if enrollment is active or pending
        if (enrollment.getStatus() != Enrollment.EnrollmentStatus.ACTIVE && 
            enrollment.getStatus() != Enrollment.EnrollmentStatus.PENDING) {
            throw new RuntimeException("Can only withdraw from active or pending enrollments");
        }
        
        // Set status to WITHDRAWN
        enrollment.setStatus(Enrollment.EnrollmentStatus.WITHDRAWN);
        enrollmentRepository.save(enrollment);
        
        // Send notification to student
        Notification studentNotification = new Notification(
            "Successfully withdrawn from " + course.getTitle(),
            student,
            Notification.NotificationType.WITHDRAWAL
        );
        notificationRepository.save(studentNotification);
        
        // Send notification to all admins about withdrawal
        adminNotificationService.notifyWithdrawalRequest(enrollment);
    }
    
    public List<Enrollment> getStudentEnrollments(Long studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isPresent()) {
            return enrollmentRepository.findByStudent(studentOpt.get());
        }
        return List.of();
    }
    
    public List<Enrollment> getActiveStudentEnrollments(Long studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isPresent()) {
            return enrollmentRepository.findActiveEnrollmentsByStudent(studentOpt.get());
        }
        return List.of();
    }
    
    public List<Enrollment> getCourseEnrollments(Long courseId) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isPresent()) {
            return enrollmentRepository.findByCourse(courseOpt.get());
        }
        return List.of();
    }
    
    public List<Enrollment> getActiveCourseEnrollments(Long courseId) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isPresent()) {
            return enrollmentRepository.findActiveEnrollmentsByCourse(courseOpt.get());
        }
        return List.of();
    }
    
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }
    
    public Enrollment getEnrollmentById(Long enrollmentId) {
        return enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
    }
    
    public boolean isStudentEnrolled(Long studentId, Long courseId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        
        if (studentOpt.isPresent() && courseOpt.isPresent()) {
            Optional<Enrollment> enrollment = enrollmentRepository.findByStudentAndCourse(studentOpt.get(), courseOpt.get());
            return enrollment.isPresent() && enrollment.get().getStatus() == Enrollment.EnrollmentStatus.ACTIVE;
        }
        return false;
    }
    
    public void updateEnrollmentStatus(Long enrollmentId, String status, Administrator admin) {
        Optional<Enrollment> enrollmentOpt = enrollmentRepository.findById(enrollmentId);
        if (enrollmentOpt.isEmpty()) {
            throw new RuntimeException("Enrollment not found");
        }
        
        Enrollment enrollment = enrollmentOpt.get();
        try {
            Enrollment.EnrollmentStatus enrollmentStatus = Enrollment.EnrollmentStatus.valueOf(status.toUpperCase());
            enrollment.setStatus(enrollmentStatus);
            enrollmentRepository.save(enrollment);
            
            // Send notification to student about status change
            String message;
            if (enrollmentStatus == Enrollment.EnrollmentStatus.ACTIVE) {
                message = "Your enrollment in " + enrollment.getCourse().getTitle() + " has been approved!";
            } else if (enrollmentStatus == Enrollment.EnrollmentStatus.WITHDRAWN) {
                message = "Your enrollment in " + enrollment.getCourse().getTitle() + " has been rejected.";
            } else {
                message = "Your enrollment status in " + enrollment.getCourse().getTitle() + " has been updated to " + status;
            }
            
            Notification notification = new Notification(
                message,
                enrollment.getStudent(),
                Notification.NotificationType.ENROLLMENT
            );
            notificationRepository.save(notification);
            
            // Send notification to admin about their action
            if (enrollmentStatus == Enrollment.EnrollmentStatus.ACTIVE) {
                adminNotificationService.notifyEnrollmentApproved(enrollment, admin);
            } else if (enrollmentStatus == Enrollment.EnrollmentStatus.WITHDRAWN) {
                adminNotificationService.notifyEnrollmentRejected(enrollment, admin);
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid enrollment status: " + status);
        }
    }
    
    public void approveEnrollment(Long enrollmentId, Administrator admin) {
        updateEnrollmentStatus(enrollmentId, "ACTIVE", admin);
    }
    
    public void rejectEnrollment(Long enrollmentId, Administrator admin) {
        updateEnrollmentStatus(enrollmentId, "WITHDRAWN", admin);
    }
    
    public List<Enrollment> getPendingEnrollments() {
        return enrollmentRepository.findByStatus(Enrollment.EnrollmentStatus.PENDING);
    }
}
