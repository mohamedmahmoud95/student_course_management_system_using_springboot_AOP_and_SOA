package com.scms.service;

import com.scms.entity.Enrollment;
import com.scms.entity.Student;
import com.scms.entity.Course;
import com.scms.entity.Notification;
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
        
        Notification notification = new Notification(
            "Successfully enrolled in " + course.getTitle(),
            student,
            Notification.NotificationType.ENROLLMENT
        );
        notificationRepository.save(notification);
        
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
        enrollment.setStatus(Enrollment.EnrollmentStatus.WITHDRAWN);
        enrollmentRepository.save(enrollment);
        
        Notification notification = new Notification(
            "Successfully withdrawn from " + course.getTitle(),
            student,
            Notification.NotificationType.WITHDRAWAL
        );
        notificationRepository.save(notification);
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
    
    public boolean isStudentEnrolled(Long studentId, Long courseId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        
        if (studentOpt.isPresent() && courseOpt.isPresent()) {
            Optional<Enrollment> enrollment = enrollmentRepository.findByStudentAndCourse(studentOpt.get(), courseOpt.get());
            return enrollment.isPresent() && enrollment.get().getStatus() == Enrollment.EnrollmentStatus.ACTIVE;
        }
        return false;
    }
}
