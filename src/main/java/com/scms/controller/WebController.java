package com.scms.controller;

import com.scms.entity.Student;
import com.scms.entity.Course;
import com.scms.entity.Enrollment;
import com.scms.entity.Grade;
import com.scms.entity.Notification;
import com.scms.service.StudentService;
import com.scms.service.CourseService;
import com.scms.service.EnrollmentService;
import com.scms.service.GradeService;
import com.scms.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

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
    
    @GetMapping("/student/dashboard")
    public String studentDashboard(@RequestParam Long studentId, Model model) {
        Student student = studentService.getStudentById(studentId).orElse(null);
        if (student == null) {
            return "redirect:/login";
        }
        
        List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(studentId);
        List<Grade> grades = gradeService.getStudentGrades(studentId);
        BigDecimal gpa = studentService.calculateStudentGPA(studentId);
        // long unreadNotifications = notificationService.getUnreadCount(studentId); // Temporarily disabled
        
        model.addAttribute("student", student);
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("grades", grades);
        model.addAttribute("gpa", gpa);
        model.addAttribute("unreadNotifications", 0L); // Set to 0 temporarily
        
        return "student/dashboard";
    }
    
    @GetMapping("/student/courses")
    public String studentCourses(@RequestParam Long studentId, Model model) {
        Student student = studentService.getStudentById(studentId).orElse(null);
        if (student == null) {
            return "redirect:/login";
        }
        
        List<Course> availableCourses = courseService.getAvailableCourses();
        List<Enrollment> studentEnrollments = enrollmentService.getStudentEnrollments(studentId);
        
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
        
        // List<com.scms.entity.Notification> notifications = notificationService.getStudentNotifications(studentId); // Temporarily disabled
        
        model.addAttribute("student", student);
        model.addAttribute("notifications", List.of()); // Empty list temporarily
        
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
        try {
            List<Student> students = studentService.getAllStudents();
            
            // Calculate actual GPA and enrollment counts
            Map<Long, BigDecimal> studentGPAs = new HashMap<>();
            Map<Long, Integer> studentEnrollmentCounts = new HashMap<>();
            
            for (Student student : students) {
                try {
                    BigDecimal gpa = studentService.calculateStudentGPA(student.getId());
                    studentGPAs.put(student.getId(), gpa);
                    
                    List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(student.getId());
                    studentEnrollmentCounts.put(student.getId(), enrollments.size());
                } catch (Exception e) {
                    studentGPAs.put(student.getId(), BigDecimal.ZERO);
                    studentEnrollmentCounts.put(student.getId(), 0);
                }
            }
            
            // Calculate statistics for the cards using the same approach as admin dashboard
            int totalStudents = students.size();
            List<Enrollment> allEnrollments = enrollmentService.getAllEnrollments();
            int totalEnrollments = allEnrollments.size();
            
            // Calculate average GPA
            BigDecimal totalGPA = studentGPAs.values().stream()
                .filter(gpa -> gpa.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            int studentsWithGrades = (int) studentGPAs.values().stream()
                .filter(gpa -> gpa.compareTo(BigDecimal.ZERO) > 0)
                .count();
            BigDecimal averageGPA = studentsWithGrades > 0 ? 
                totalGPA.divide(BigDecimal.valueOf(studentsWithGrades), 2, BigDecimal.ROUND_HALF_UP) : 
                BigDecimal.ZERO;
            
            // Count active students (students with enrollments)
            int activeStudents = (int) studentEnrollmentCounts.values().stream()
                .filter(count -> count > 0)
                .count();
            
            model.addAttribute("students", students);
            model.addAttribute("studentGPAs", studentGPAs);
            model.addAttribute("studentEnrollmentCounts", studentEnrollmentCounts);

            
            // Calculate statistics using simple approach
            model.addAttribute("totalStudents", students.size());
            model.addAttribute("totalEnrollments", allEnrollments.size());
            model.addAttribute("averageGPA", averageGPA);
            model.addAttribute("activeStudents", activeStudents);
            return "admin/students";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error loading students: " + e.getMessage());
            return "error";
        }
    }
    
    @GetMapping("/admin/students/{studentId}")
    public String studentDetails(@PathVariable Long studentId, Model model) {
        try {
            Student student = studentService.getStudentById(studentId).orElse(null);
            if (student == null) {
                model.addAttribute("error", "Student not found");
                return "error";
            }
            
            BigDecimal gpa = studentService.calculateStudentGPA(studentId);
            List<Enrollment> enrollments = studentService.getStudentEnrollments(studentId);
            List<Grade> grades = gradeService.getStudentGrades(studentId);
            List<Notification> notifications = notificationService.getStudentNotifications(studentId);
            
            model.addAttribute("student", student);
            model.addAttribute("studentGPA", gpa);
            model.addAttribute("studentEnrollments", enrollments);
            model.addAttribute("studentGrades", grades);
            model.addAttribute("studentNotifications", notifications);
            
            return "admin/student-details";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error loading student details: " + e.getMessage());
            return "error";
        }
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
    
    @GetMapping("/admin/notifications")
    public String adminNotifications(Model model) {
        return "admin/notifications";
    }
    
    @GetMapping("/admin/login-bypass")
    public String adminLoginBypass(HttpSession session) {
        session.setAttribute("userType", "admin");
        session.setAttribute("userId", 1L);
        session.setAttribute("userName", "Admin");
        return "redirect:/admin/dashboard";
    }
    
    @GetMapping("/test")
    public String test() {
        return "Hello World!";
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
