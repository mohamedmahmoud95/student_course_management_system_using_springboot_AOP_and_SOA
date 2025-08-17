package com.scms.controller;

import com.scms.entity.Grade;
import com.scms.service.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/grades")
@Tag(name = "Grade Management", description = "APIs for grade operations")
public class GradeController {
    
    @Autowired
    private GradeService gradeService;
    
    @PostMapping("/record")
    @Operation(summary = "Record a grade for a student in a course")
    public ResponseEntity<Grade> recordGrade(@RequestBody Map<String, Object> request) {
        Long studentId = Long.valueOf(request.get("studentId").toString());
        Long courseId = Long.valueOf(request.get("courseId").toString());
        BigDecimal score = new BigDecimal(request.get("score").toString());
        String comments = (String) request.get("comments");
        
        Grade grade = gradeService.recordGrade(studentId, courseId, score, comments);
        return ResponseEntity.ok(grade);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update a grade")
    public ResponseEntity<Grade> updateGrade(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        BigDecimal score = new BigDecimal(request.get("score").toString());
        String comments = (String) request.get("comments");
        
        Grade grade = gradeService.updateGrade(id, score, comments);
        return ResponseEntity.ok(grade);
    }
    
    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get all grades for a student")
    public ResponseEntity<List<Grade>> getStudentGrades(@PathVariable Long studentId) {
        List<Grade> grades = gradeService.getStudentGrades(studentId);
        return ResponseEntity.ok(grades);
    }
    
    @GetMapping("/course/{courseId}")
    @Operation(summary = "Get all grades for a course")
    public ResponseEntity<List<Grade>> getCourseGrades(@PathVariable Long courseId) {
        List<Grade> grades = gradeService.getCourseGrades(courseId);
        return ResponseEntity.ok(grades);
    }
    
    @GetMapping("/student/{studentId}/course/{courseId}")
    @Operation(summary = "Get grade for a specific student and course")
    public ResponseEntity<Grade> getGrade(@PathVariable Long studentId, @PathVariable Long courseId) {
        return gradeService.getGrade(studentId, courseId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/student/{studentId}/gpa")
    @Operation(summary = "Get student GPA")
    public ResponseEntity<Map<String, Object>> getStudentGPA(@PathVariable Long studentId) {
        BigDecimal gpa = gradeService.getStudentGPA(studentId);
        return ResponseEntity.ok(Map.of("studentId", studentId, "gpa", gpa));
    }
    
    @GetMapping("/course/{courseId}/average")
    @Operation(summary = "Get course average grade")
    public ResponseEntity<Map<String, Object>> getCourseAverage(@PathVariable Long courseId) {
        BigDecimal average = gradeService.getCourseAverage(courseId);
        return ResponseEntity.ok(Map.of("courseId", courseId, "average", average));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a grade")
    public ResponseEntity<?> deleteGrade(@PathVariable Long id) {
        gradeService.deleteGrade(id);
        return ResponseEntity.ok(Map.of("message", "Grade deleted successfully"));
    }
}
