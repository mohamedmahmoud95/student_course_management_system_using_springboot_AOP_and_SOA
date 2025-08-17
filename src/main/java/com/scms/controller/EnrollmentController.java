package com.scms.controller;

import com.scms.entity.Enrollment;
import com.scms.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
@Tag(name = "Enrollment Management", description = "APIs for enrollment operations")
public class EnrollmentController {
    
    @Autowired
    private EnrollmentService enrollmentService;
    
    @PostMapping("/enroll")
    @Operation(summary = "Enroll a student in a course")
    public ResponseEntity<Map<String, Object>> enrollStudent(@RequestBody Map<String, Long> request) {
        Long studentId = request.get("studentId");
        Long courseId = request.get("courseId");
        
        try {
            Enrollment enrollment = enrollmentService.enrollStudent(studentId, courseId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "We've sent a notification to the admin to review and approve your enrollment");
            response.put("enrollment", enrollment);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/withdraw")
    @Operation(summary = "Withdraw a student from a course")
    public ResponseEntity<Map<String, Object>> withdrawStudent(@RequestBody Map<String, Long> request) {
        Long studentId = request.get("studentId");
        Long courseId = request.get("courseId");
        
        try {
            enrollmentService.withdrawStudent(studentId, courseId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Student withdrawn successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/{enrollmentId}")
    @Operation(summary = "Get enrollment by ID")
    public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable Long enrollmentId) {
        try {
            Enrollment enrollment = enrollmentService.getEnrollmentById(enrollmentId);
            return ResponseEntity.ok(enrollment);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/debug/{enrollmentId}")
    @Operation(summary = "Debug enrollment by ID")
    public ResponseEntity<Map<String, Object>> debugEnrollmentById(@PathVariable Long enrollmentId) {
        try {
            Enrollment enrollment = enrollmentService.getEnrollmentById(enrollmentId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("enrollment", enrollment);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get all enrollments for a student")
    public ResponseEntity<List<Enrollment>> getStudentEnrollments(@PathVariable Long studentId) {
        List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(studentId);
        return ResponseEntity.ok(enrollments);
    }
    
    @GetMapping("/student/{studentId}/active")
    @Operation(summary = "Get active enrollments for a student")
    public ResponseEntity<List<Enrollment>> getActiveStudentEnrollments(@PathVariable Long studentId) {
        List<Enrollment> enrollments = enrollmentService.getActiveStudentEnrollments(studentId);
        return ResponseEntity.ok(enrollments);
    }
    
    @GetMapping
    @Operation(summary = "Get all enrollments")
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(enrollments);
    }
    
    @GetMapping("/course/{courseId}")
    @Operation(summary = "Get all enrollments for a course")
    public ResponseEntity<List<Enrollment>> getCourseEnrollments(@PathVariable Long courseId) {
        List<Enrollment> enrollments = enrollmentService.getCourseEnrollments(courseId);
        return ResponseEntity.ok(enrollments);
    }
    
    @GetMapping("/course/{courseId}/active")
    @Operation(summary = "Get active enrollments for a course")
    public ResponseEntity<List<Enrollment>> getActiveCourseEnrollments(@PathVariable Long courseId) {
        List<Enrollment> enrollments = enrollmentService.getActiveCourseEnrollments(courseId);
        return ResponseEntity.ok(enrollments);
    }
    
    @GetMapping("/check")
    @Operation(summary = "Check if student is enrolled in a course")
    public ResponseEntity<Map<String, Object>> isStudentEnrolled(@RequestParam Long studentId, @RequestParam Long courseId) {
        boolean enrolled = enrollmentService.isStudentEnrolled(studentId, courseId);
        return ResponseEntity.ok(Map.of(
            "studentId", studentId,
            "courseId", courseId,
            "enrolled", enrolled
        ));
    }
}
