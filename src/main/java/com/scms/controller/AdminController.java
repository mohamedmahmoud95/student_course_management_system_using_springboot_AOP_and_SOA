package com.scms.controller;

import com.scms.entity.Student;
import com.scms.entity.Course;
import com.scms.entity.Enrollment;
import com.scms.entity.Grade;
import com.scms.service.StudentService;
import com.scms.service.CourseService;
import com.scms.service.EnrollmentService;
import com.scms.service.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Management", description = "APIs for administrative operations")
public class AdminController {
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private EnrollmentService enrollmentService;
    
    @Autowired
    private GradeService gradeService;
    
    // Student Management
    @PostMapping("/students")
    @Operation(summary = "Add a new student")
    public ResponseEntity<Map<String, Object>> addStudent(@RequestBody Map<String, String> request) {
        try {
            String name = request.get("name");
            String email = request.get("email");
            String password = request.get("password");
            
            Student student = new Student(name, email, password);
            Student savedStudent = studentService.createStudent(student);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Student added successfully");
            response.put("student", savedStudent);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PutMapping("/students/{id}")
    @Operation(summary = "Update a student")
    public ResponseEntity<Map<String, Object>> updateStudent(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String name = request.get("name");
            String email = request.get("email");
            
            Optional<Student> existingStudent = studentService.getStudentById(id);
            if (existingStudent.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Student not found");
                return ResponseEntity.notFound().build();
            }
            
            Student student = existingStudent.get();
            student.setName(name);
            student.setEmail(email);
            
            Student updatedStudent = studentService.updateStudent(student);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Student updated successfully");
            response.put("student", updatedStudent);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @DeleteMapping("/students/{id}")
    @Operation(summary = "Delete a student")
    public ResponseEntity<Map<String, Object>> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Student deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // Course Management
    @PostMapping("/courses")
    @Operation(summary = "Add a new course")
    public ResponseEntity<Map<String, Object>> addCourse(@RequestBody Map<String, Object> request) {
        try {
            String title = (String) request.get("title");
            String description = (String) request.get("description");
            Integer capacity = (Integer) request.get("capacity");
            String prerequisites = (String) request.get("prerequisites");
            
            Course course = courseService.createCourse(title, description, capacity, prerequisites);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Course added successfully");
            response.put("course", course);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PutMapping("/courses/{id}")
    @Operation(summary = "Update a course")
    public ResponseEntity<Map<String, Object>> updateCourse(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            String title = (String) request.get("title");
            String description = (String) request.get("description");
            Integer capacity = (Integer) request.get("capacity");
            String prerequisites = (String) request.get("prerequisites");
            
            Optional<Course> existingCourse = courseService.getCourseById(id);
            if (existingCourse.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Course not found");
                return ResponseEntity.notFound().build();
            }
            
            Course course = existingCourse.get();
            course.setTitle(title);
            course.setDescription(description);
            course.setCapacity(capacity);
            course.setPrerequisites(prerequisites);
            
            Course updatedCourse = courseService.updateCourse(course);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Course updated successfully");
            response.put("course", updatedCourse);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @DeleteMapping("/courses/{id}")
    @Operation(summary = "Delete a course")
    public ResponseEntity<Map<String, Object>> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Course deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // Grade Management
    @PostMapping("/grades")
    @Operation(summary = "Add a new grade")
    public ResponseEntity<Map<String, Object>> addGrade(@RequestBody Map<String, Object> request) {
        try {
            Long studentId = Long.valueOf(request.get("studentId").toString());
            Long courseId = Long.valueOf(request.get("courseId").toString());
            BigDecimal score = new BigDecimal(request.get("score").toString());
            String comments = (String) request.get("comments");
            
            Grade grade = gradeService.recordGrade(studentId, courseId, score, comments);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Grade added successfully");
            response.put("grade", grade);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PutMapping("/grades/{id}")
    @Operation(summary = "Update a grade")
    public ResponseEntity<Map<String, Object>> updateGrade(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            BigDecimal score = new BigDecimal(request.get("score").toString());
            String comments = (String) request.get("comments");
            
            Grade grade = gradeService.updateGrade(id, score, comments);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Grade updated successfully");
            response.put("grade", grade);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @DeleteMapping("/grades/{id}")
    @Operation(summary = "Delete a grade")
    public ResponseEntity<Map<String, Object>> deleteGrade(@PathVariable Long id) {
        try {
            gradeService.deleteGrade(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Grade deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // Enrollment Management
    @PutMapping("/enrollments/{id}/status")
    @Operation(summary = "Update enrollment status")
    public ResponseEntity<Map<String, Object>> updateEnrollmentStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String status = request.get("status");
            enrollmentService.updateEnrollmentStatus(id, status);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Enrollment status updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // Dashboard Statistics
    @GetMapping("/dashboard/stats")
    @Operation(summary = "Get dashboard statistics")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        try {
            List<Student> students = studentService.getAllStudents();
            List<Course> courses = courseService.getAllCourses();
            List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
            List<Grade> grades = gradeService.getAllGrades();
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalStudents", students.size());
            stats.put("totalCourses", courses.size());
            stats.put("totalEnrollments", enrollments.size());
            stats.put("totalGrades", grades.size());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("stats", stats);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
