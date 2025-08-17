package com.scms.controller;

import com.scms.entity.Course;
import com.scms.entity.Enrollment;
import com.scms.service.CourseService;
import com.scms.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
@Tag(name = "Course Management", description = "APIs for course operations")
public class CourseController {
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private EnrollmentService enrollmentService;
    
    @PostMapping
    @Operation(summary = "Create a new course")
    public ResponseEntity<Course> createCourse(@RequestBody Map<String, Object> request) {
        String title = (String) request.get("title");
        String description = (String) request.get("description");
        Integer capacity = (Integer) request.get("capacity");
        String prerequisites = (String) request.get("prerequisites");
        
        Course course = courseService.createCourse(title, description, capacity, prerequisites);
        return ResponseEntity.ok(course);
    }
    
    @GetMapping
    @Operation(summary = "Get all courses")
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/available")
    @Operation(summary = "Get available courses")
    public ResponseEntity<List<Course>> getAvailableCourses() {
        List<Course> courses = courseService.getAvailableCourses();
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/full")
    @Operation(summary = "Get full courses")
    public ResponseEntity<List<Course>> getFullCourses() {
        List<Course> courses = courseService.getFullCourses();
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search courses by title")
    public ResponseEntity<List<Course>> searchCourses(@RequestParam String title) {
        List<Course> courses = courseService.searchCoursesByTitle(title);
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update course information")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        course.setId(id);
        Course updatedCourse = courseService.updateCourse(course);
        return ResponseEntity.ok(updatedCourse);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a course")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(Map.of("message", "Course deleted successfully"));
    }
    
    @GetMapping("/{id}/available")
    @Operation(summary = "Check if course has available seats")
    public ResponseEntity<Map<String, Object>> isCourseAvailable(@PathVariable Long id) {
        boolean available = courseService.isCourseAvailable(id);
        int availableSeats = courseService.getAvailableSeats(id);
        return ResponseEntity.ok(Map.of(
            "courseId", id,
            "available", available,
            "availableSeats", availableSeats
        ));
    }
    
    @GetMapping("/{id}/enrollments")
    @Operation(summary = "Get course enrollments")
    public ResponseEntity<List<Enrollment>> getCourseEnrollments(@PathVariable Long id) {
        List<Enrollment> enrollments = enrollmentService.getCourseEnrollments(id);
        return ResponseEntity.ok(enrollments);
    }
}
