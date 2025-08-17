package com.scms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scms.entity.Grade;
import com.scms.entity.Student;
import com.scms.entity.Course;
import com.scms.service.GradeService;
import com.scms.config.TestSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GradeController.class)
@Import(TestSecurityConfig.class)
class GradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GradeService gradeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Grade testGrade;
    private Student testStudent;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setName("John Doe");
        testStudent.setEmail("john@test.com");

        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setTitle("Introduction to Computer Science");

        testGrade = new Grade();
        testGrade.setId(1L);
        testGrade.setStudent(testStudent);
        testGrade.setCourse(testCourse);
        testGrade.setScore(new BigDecimal("85.5"));
        testGrade.setRecordedDate(LocalDateTime.now());
        testGrade.setComments("Good performance");
    }

    @Test
    void recordGrade_Success() throws Exception {
        when(gradeService.recordGrade(any(Long.class), any(Long.class), any(BigDecimal.class), any(String.class)))
                .thenReturn(testGrade);

        mockMvc.perform(post("/api/grades/record")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"studentId\":1,\"courseId\":1,\"score\":85.5,\"comments\":\"Good performance\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.score").value(85.5));
    }

    @Test
    void updateGrade_Success() throws Exception {
        when(gradeService.updateGrade(any(Long.class), any(BigDecimal.class), any(String.class)))
                .thenReturn(testGrade);

        mockMvc.perform(put("/api/grades/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"score\":90.0,\"comments\":\"Excellent performance\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deleteGrade_Success() throws Exception {
        mockMvc.perform(delete("/api/grades/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Grade deleted successfully"));
    }

    @Test
    void getStudentGrades_Success() throws Exception {
        when(gradeService.getStudentGrades(1L)).thenReturn(Arrays.asList(testGrade));

        mockMvc.perform(get("/api/grades/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].score").value(85.5));
    }

    @Test
    void getCourseGrades_Success() throws Exception {
        when(gradeService.getCourseGrades(1L)).thenReturn(Arrays.asList(testGrade));

        mockMvc.perform(get("/api/grades/course/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].score").value(85.5));
    }

    @Test
    void getStudentCourseGrade_Success() throws Exception {
        when(gradeService.getGrade(1L, 1L)).thenReturn(Optional.of(testGrade));

        mockMvc.perform(get("/api/grades/student/1/course/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.score").value(85.5));
    }

    @Test
    void getStudentCourseGrade_NotFound() throws Exception {
        when(gradeService.getGrade(1L, 1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/grades/student/1/course/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStudentGPA_Success() throws Exception {
        when(gradeService.getStudentGPA(1L)).thenReturn(new BigDecimal("85.5"));

        mockMvc.perform(get("/api/grades/student/1/gpa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gpa").value(85.5));
    }
}
