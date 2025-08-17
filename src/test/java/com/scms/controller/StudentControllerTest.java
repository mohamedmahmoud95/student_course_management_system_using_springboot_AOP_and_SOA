package com.scms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scms.entity.Student;
import com.scms.service.StudentService;
import com.scms.service.EnrollmentService;
import com.scms.service.GradeService;
import com.scms.service.NotificationService;
import com.scms.config.TestSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@Import(TestSecurityConfig.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;
    
    @MockBean
    private EnrollmentService enrollmentService;
    
    @MockBean
    private GradeService gradeService;
    
    @MockBean
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    private Student testStudent;

    @BeforeEach
    void setUp() {
        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setName("John Doe");
        testStudent.setEmail("john@test.com");
        testStudent.setPassword("password123");
    }

    @Test
    void registerStudent_Success() throws Exception {
        when(studentService.registerStudent(any(String.class), any(String.class), any(String.class)))
                .thenReturn(testStudent);

        mockMvc.perform(post("/api/students/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"John Doe\",\"email\":\"john@test.com\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@test.com"));
    }

    @Test
    void loginStudent_Success() throws Exception {
        when(studentService.authenticateStudent("john@test.com", "password123"))
                .thenReturn(Optional.of(testStudent));

        mockMvc.perform(post("/api/students/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"john@test.com\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.student.id").value(1));
    }

    @Test
    void loginStudent_InvalidCredentials() throws Exception {
        when(studentService.authenticateStudent("john@test.com", "wrongPassword"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/students/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"john@test.com\",\"password\":\"wrongPassword\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }

    @Test
    void getAllStudents_Success() throws Exception {
        when(studentService.getAllStudents()).thenReturn(Arrays.asList(testStudent));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john@test.com"));
    }

    @Test
    void getStudentById_Success() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(Optional.of(testStudent));

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@test.com"));
    }

    @Test
    void getStudentById_NotFound() throws Exception {
        when(studentService.getStudentById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/students/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateStudent_Success() throws Exception {
        when(studentService.updateStudent(any(Student.class))).thenReturn(testStudent);

        mockMvc.perform(put("/api/students/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"John Updated\",\"email\":\"john@test.com\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void deleteStudent_Success() throws Exception {
        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Student deleted successfully"));
    }
}
