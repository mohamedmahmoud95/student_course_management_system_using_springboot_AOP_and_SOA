package com.scms.controller;

import com.scms.entity.Enrollment;
import com.scms.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Enrollment> enrollStudent(@RequestBody Map<String, Long> request) {
        Long studentId = request.get("studentId");
        Long courseId = request.get("courseId");
        
        Enrollment enrollment = enrollmentService.enrollStudent(studentId, courseId);
        return ResponseEntity.ok(enrollment);
    }
    
    @PostMapping("/withdraw")
    @Operation(summary = "Withdraw a student from a course")
    public ResponseEntity<?> withdrawStudent(@RequestBody Map<String, Long> request) {
        Long studentId = request.get("studentId");
        Long courseId = request.get("courseId");
        
        enrollmentService.withdrawStudent(studentId, courseId);
        return ResponseEntity.ok(Map.of("message", "Student withdrawn successfully"));
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
