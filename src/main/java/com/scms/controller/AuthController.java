package com.scms.controller;

import com.scms.entity.Student;
import com.scms.entity.Administrator;
import com.scms.service.StudentService;
import com.scms.service.AdministratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private AdministratorService administratorService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }
    
    @GetMapping("/signup")
    public String signupPage() {
        return "auth/signup";
    }
    
    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "auth/forgot-password";
    }
    
    @PostMapping("/api/auth/login")
    @ResponseBody
    @Operation(summary = "User login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest, HttpSession session) {
        try {
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");
            String userType = loginRequest.get("userType");
            
            Map<String, Object> response = new HashMap<>();
            
            if ("student".equals(userType)) {
                Student student = studentService.findByEmail(email);
                if (student != null && password.equals(student.getPassword())) {
                    session.setAttribute("userType", "student");
                    session.setAttribute("userId", student.getId());
                    session.setAttribute("userName", student.getName());
                    
                    response.put("success", true);
                    response.put("message", "Login successful");
                    response.put("redirect", "/student/dashboard?studentId=" + student.getId());
                    return ResponseEntity.ok(response);
                }
            } else if ("admin".equals(userType)) {
                Administrator admin = administratorService.findByEmail(email);
                if (admin != null && password.equals(admin.getPassword())) {
                    session.setAttribute("userType", "admin");
                    session.setAttribute("userId", admin.getId());
                    session.setAttribute("userName", admin.getName());
                    
                    response.put("success", true);
                    response.put("message", "Login successful");
                    response.put("redirect", "/admin/dashboard");
                    return ResponseEntity.ok(response);
                }
            }
            
            response.put("success", false);
            response.put("message", "Invalid email or password");
            return ResponseEntity.badRequest().body(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/api/auth/signup")
    @ResponseBody
    @Operation(summary = "Student signup")
    public ResponseEntity<Map<String, Object>> signup(@Valid @RequestBody Student student) {
        try {
            Map<String, Object> response = new HashMap<>();
            
            if (!student.getEmail().endsWith("@eng.asu.edu.eg")) {
                response.put("success", false);
                response.put("message", "Email must be a valid university email (@eng.asu.edu.eg)");
                return ResponseEntity.badRequest().body(response);
            }
            
            Student existingStudent = studentService.findByEmail(student.getEmail());
            if (existingStudent != null) {
                response.put("success", false);
                response.put("message", "Email already registered");
                return ResponseEntity.badRequest().body(response);
            }
            
            Student savedStudent = studentService.createStudent(student);
            
            response.put("success", true);
            response.put("message", "Registration successful");
            response.put("redirect", "/login");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/api/auth/forgot-password")
    @ResponseBody
    @Operation(summary = "Forgot password")
    public ResponseEntity<Map<String, Object>> forgotPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String userType = request.get("userType");
            
            Map<String, Object> response = new HashMap<>();
            
            if ("student".equals(userType)) {
                Student student = studentService.findByEmail(email);
                if (student != null) {
                    // In a real application, send password reset email
                    response.put("success", true);
                    response.put("message", "Password reset instructions sent to your email");
                    return ResponseEntity.ok(response);
                }
            } else if ("admin".equals(userType)) {
                Administrator admin = administratorService.findByEmail(email);
                if (admin != null) {
                    // In a real application, send password reset email
                    response.put("success", true);
                    response.put("message", "Password reset instructions sent to your email");
                    return ResponseEntity.ok(response);
                }
            }
            
            response.put("success", false);
            response.put("message", "Email not found");
            return ResponseEntity.badRequest().body(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Password reset failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/api/auth/logout")
    @ResponseBody
    @Operation(summary = "User logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        session.invalidate();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Logout successful");
        response.put("redirect", "/");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/api/auth/check")
    @ResponseBody
    @Operation(summary = "Check authentication status")
    public ResponseEntity<Map<String, Object>> checkAuth(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        String userType = (String) session.getAttribute("userType");
        Long userId = (Long) session.getAttribute("userId");
        String userName = (String) session.getAttribute("userName");
        
        if (userType != null && userId != null) {
            response.put("authenticated", true);
            response.put("userType", userType);
            response.put("userId", userId);
            response.put("userName", userName);
        } else {
            response.put("authenticated", false);
        }
        
        return ResponseEntity.ok(response);
    }
}
