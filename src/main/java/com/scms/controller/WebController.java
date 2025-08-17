package com.scms.controller;

import com.scms.entity.Student;
import com.scms.entity.Course;
import com.scms.entity.Enrollment;
import com.scms.entity.Grade;
import com.scms.service.StudentService;
import com.scms.service.CourseService;
import com.scms.service.EnrollmentService;
import com.scms.service.GradeService;
import com.scms.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
public class WebController {
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private EnrollmentService enrollmentService;
    
    @Autowired
    private GradeService gradeService;
    
    @Autowired
    private NotificationService notificationService;
    
    @GetMapping("/")
    public String home() {
        return "index";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/register")
    public String register() {
        return "register";
    }
    
    @GetMapping("/student/dashboard")
    public String studentDashboard(@RequestParam Long studentId, Model model) {
        Student student = studentService.getStudentById(studentId).orElse(null);
        if (student == null) {
            return "redirect:/login";
        }
        
        List<Enrollment> enrollments = enrollmentService.getActiveStudentEnrollments(studentId);
        List<Grade> grades = gradeService.getStudentGrades(studentId);
        BigDecimal gpa = studentService.calculateStudentGPA(studentId);
        long unreadNotifications = notificationService.getUnreadCount(studentId);
        
        model.addAttribute("student", student);
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("grades", grades);
        model.addAttribute("gpa", gpa);
        model.addAttribute("unreadNotifications", unreadNotifications);
        
        return "student/dashboard";
    }
    
    @GetMapping("/student/courses")
    public String studentCourses(@RequestParam Long studentId, Model model) {
        Student student = studentService.getStudentById(studentId).orElse(null);
        if (student == null) {
            return "redirect:/login";
        }
        
        List<Course> availableCourses = courseService.getAvailableCourses();
        List<Enrollment> studentEnrollments = enrollmentService.getActiveStudentEnrollments(studentId);
        
        model.addAttribute("student", student);
        model.addAttribute("availableCourses", availableCourses);
        model.addAttribute("studentEnrollments", studentEnrollments);
        
        return "student/courses";
    }
    
    @GetMapping("/student/grades")
    public String studentGrades(@RequestParam Long studentId, Model model) {
        Student student = studentService.getStudentById(studentId).orElse(null);
        if (student == null) {
            return "redirect:/login";
        }
        
        List<Grade> grades = gradeService.getStudentGrades(studentId);
        BigDecimal gpa = studentService.calculateStudentGPA(studentId);
        
        model.addAttribute("student", student);
        model.addAttribute("grades", grades);
        model.addAttribute("gpa", gpa);
        
        return "student/grades";
    }
    
    @GetMapping("/student/notifications")
    public String studentNotifications(@RequestParam Long studentId, Model model) {
        Student student = studentService.getStudentById(studentId).orElse(null);
        if (student == null) {
            return "redirect:/login";
        }
        
        List<com.scms.entity.Notification> notifications = notificationService.getStudentNotifications(studentId);
        
        model.addAttribute("student", student);
        model.addAttribute("notifications", notifications);
        
        return "student/notifications";
    }
    
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        List<Student> students = studentService.getAllStudents();
        List<Course> courses = courseService.getAllCourses();
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        List<Grade> grades = gradeService.getAllGrades();
        
        model.addAttribute("totalStudents", students.size());
        model.addAttribute("totalCourses", courses.size());
        model.addAttribute("totalEnrollments", enrollments.size());
        model.addAttribute("totalGrades", grades.size());
        
        return "admin/dashboard";
    }
    
    @GetMapping("/admin/students")
    public String adminStudents(Model model) {
        List<Student> students = studentService.getAllStudents();
        
        // Calculate GPA for each student
        Map<Long, BigDecimal> studentGPAs = new HashMap<>();
        for (Student student : students) {
            BigDecimal gpa = studentService.calculateStudentGPA(student.getId());
            studentGPAs.put(student.getId(), gpa);
        }
        
        model.addAttribute("students", students);
        model.addAttribute("studentGPAs", studentGPAs);
        return "admin/students";
    }
    
    @GetMapping("/admin/courses")
    public String adminCourses(Model model) {
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "admin/courses";
    }
    
    @GetMapping("/admin/enrollments")
    public String adminEnrollments(Model model) {
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        model.addAttribute("enrollments", enrollments);
        return "admin/enrollments";
    }
    
    @GetMapping("/admin/grades")
    public String adminGrades(Model model) {
        List<Grade> grades = gradeService.getAllGrades();
        model.addAttribute("grades", grades);
        return "admin/grades";
    }
    
    @PostMapping("/enroll")
    public String enrollStudent(@RequestParam Long studentId, @RequestParam Long courseId, Model model) {
        try {
            enrollmentService.enrollStudent(studentId, courseId);
            model.addAttribute("message", "Successfully enrolled in course");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/student/courses?studentId=" + studentId;
    }
    
    @PostMapping("/withdraw")
    public String withdrawStudent(@RequestParam Long studentId, @RequestParam Long courseId, Model model) {
        try {
            enrollmentService.withdrawStudent(studentId, courseId);
            model.addAttribute("message", "Successfully withdrawn from course");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/student/courses?studentId=" + studentId;
    }
}
