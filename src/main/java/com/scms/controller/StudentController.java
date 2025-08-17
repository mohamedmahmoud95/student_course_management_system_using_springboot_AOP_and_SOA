package com.scms.controller;

import com.scms.entity.Student;
import com.scms.entity.Enrollment;
import com.scms.entity.Grade;
import com.scms.entity.Notification;
import com.scms.service.StudentService;
import com.scms.service.EnrollmentService;
import com.scms.service.GradeService;
import com.scms.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Student Management", description = "APIs for student operations")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private EnrollmentService enrollmentService;
    
    @Autowired
    private GradeService gradeService;
    
    @Autowired
    private NotificationService notificationService;
    
    @PostMapping("/register")
    @Operation(summary = "Register a new student")
    public ResponseEntity<Student> registerStudent(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String email = request.get("email");
        String password = request.get("password");
        
        Student student = studentService.registerStudent(name, email, password);
        return ResponseEntity.ok(student);
    }
    
    @PostMapping("/login")
    @Operation(summary = "Authenticate a student")
    public ResponseEntity<?> loginStudent(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        
        return studentService.authenticateStudent(email, password)
                .map(student -> ResponseEntity.ok(Map.of("message", "Login successful", "student", student)))
                .orElse(ResponseEntity.badRequest().body(Map.of("message", "Invalid credentials")));
    }
    
    @GetMapping
    @Operation(summary = "Get all students")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update student information")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        student.setId(id);
        Student updatedStudent = studentService.updateStudent(student);
        return ResponseEntity.ok(updatedStudent);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a student")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(Map.of("message", "Student deleted successfully"));
    }
    
    @GetMapping("/{id}/enrollments")
    @Operation(summary = "Get student enrollments")
    public ResponseEntity<List<Enrollment>> getStudentEnrollments(@PathVariable Long id) {
        List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(id);
        return ResponseEntity.ok(enrollments);
    }
    
    @GetMapping("/{id}/grades")
    @Operation(summary = "Get student grades")
    public ResponseEntity<List<Grade>> getStudentGrades(@PathVariable Long id) {
        List<Grade> grades = gradeService.getStudentGrades(id);
        return ResponseEntity.ok(grades);
    }
    
    @GetMapping("/{id}/gpa")
    @Operation(summary = "Get student GPA")
    public ResponseEntity<Map<String, Object>> getStudentGPA(@PathVariable Long id) {
        BigDecimal gpa = studentService.calculateStudentGPA(id);
        return ResponseEntity.ok(Map.of("studentId", id, "gpa", gpa));
    }
    
    @GetMapping("/{id}/notifications")
    @Operation(summary = "Get student notifications")
    public ResponseEntity<List<Notification>> getStudentNotifications(@PathVariable Long id) {
        List<Notification> notifications = notificationService.getStudentNotifications(id);
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/{id}/notifications/unread")
    @Operation(summary = "Get unread notifications count")
    public ResponseEntity<Map<String, Object>> getUnreadNotificationsCount(@PathVariable Long id) {
        long count = notificationService.getUnreadCount(id);
        return ResponseEntity.ok(Map.of("studentId", id, "unreadCount", count));
    }
    
    @PostMapping("/{id}/notifications/{notificationId}/read")
    @Operation(summary = "Mark notification as read")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable Long id, @PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(Map.of("message", "Notification marked as read"));
    }
    
    @PostMapping("/{id}/notifications/read-all")
    @Operation(summary = "Mark all notifications as read")
    public ResponseEntity<?> markAllNotificationsAsRead(@PathVariable Long id) {
        notificationService.markAllAsRead(id);
        return ResponseEntity.ok(Map.of("message", "All notifications marked as read"));
    }
}
