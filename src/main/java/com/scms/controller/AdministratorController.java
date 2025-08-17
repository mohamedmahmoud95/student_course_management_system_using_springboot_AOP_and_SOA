package com.scms.controller;

import com.scms.entity.Administrator;
import com.scms.entity.Student;
import com.scms.entity.Course;
import com.scms.entity.Enrollment;
import com.scms.entity.Grade;
import com.scms.service.AdministratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Administrator Management", description = "APIs for administrator operations")
public class AdministratorController {
    
    @Autowired
    private AdministratorService administratorService;
    
    @PostMapping("/register")
    @Operation(summary = "Register a new administrator")
    public ResponseEntity<Administrator> registerAdministrator(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String email = request.get("email");
        String password = request.get("password");
        
        Administrator administrator = administratorService.createAdministrator(name, email, password);
        return ResponseEntity.ok(administrator);
    }
    
    @PostMapping("/login")
    @Operation(summary = "Authenticate an administrator")
    public ResponseEntity<?> loginAdministrator(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        
        return administratorService.authenticateAdministrator(email, password)
                .map(admin -> ResponseEntity.ok(Map.of("message", "Login successful", "administrator", admin)))
                .orElse(ResponseEntity.badRequest().body(Map.of("message", "Invalid credentials")));
    }
    
    @GetMapping("/administrators")
    @Operation(summary = "Get all administrators")
    public ResponseEntity<List<Administrator>> getAllAdministrators() {
        List<Administrator> administrators = administratorService.getAllAdministrators();
        return ResponseEntity.ok(administrators);
    }
    
    @GetMapping("/administrators/{id}")
    @Operation(summary = "Get administrator by ID")
    public ResponseEntity<Administrator> getAdministratorById(@PathVariable Long id) {
        return administratorService.getAdministratorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/administrators/{id}")
    @Operation(summary = "Update administrator information")
    public ResponseEntity<Administrator> updateAdministrator(@PathVariable Long id, @RequestBody Administrator administrator) {
        administrator.setId(id);
        Administrator updatedAdministrator = administratorService.updateAdministrator(administrator);
        return ResponseEntity.ok(updatedAdministrator);
    }
    
    @DeleteMapping("/administrators/{id}")
    @Operation(summary = "Delete an administrator")
    public ResponseEntity<?> deleteAdministrator(@PathVariable Long id) {
        administratorService.deleteAdministrator(id);
        return ResponseEntity.ok(Map.of("message", "Administrator deleted successfully"));
    }
    
    @GetMapping("/students")
    @Operation(summary = "Get all students")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = administratorService.getAllStudents();
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/courses")
    @Operation(summary = "Get all courses")
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = administratorService.getAllCourses();
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/enrollments")
    @Operation(summary = "Get all enrollments")
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        List<Enrollment> enrollments = administratorService.getAllEnrollments();
        return ResponseEntity.ok(enrollments);
    }
    
    @GetMapping("/grades")
    @Operation(summary = "Get all grades")
    public ResponseEntity<List<Grade>> getAllGrades() {
        List<Grade> grades = administratorService.getAllGrades();
        return ResponseEntity.ok(grades);
    }
    
    @GetMapping("/reports/enrollment")
    @Operation(summary = "Generate enrollment report")
    public ResponseEntity<Map<String, Object>> generateEnrollmentReport() {
        Map<String, Object> report = administratorService.generateEnrollmentReport();
        return ResponseEntity.ok(report);
    }
    
    @GetMapping("/reports/grade")
    @Operation(summary = "Generate grade report")
    public ResponseEntity<Map<String, Object>> generateGradeReport() {
        Map<String, Object> report = administratorService.generateGradeReport();
        return ResponseEntity.ok(report);
    }
    
    @GetMapping("/reports/student/{studentId}")
    @Operation(summary = "Generate student report")
    public ResponseEntity<Map<String, Object>> generateStudentReport(@PathVariable Long studentId) {
        Map<String, Object> report = administratorService.generateStudentReport(studentId);
        return ResponseEntity.ok(report);
    }
}
