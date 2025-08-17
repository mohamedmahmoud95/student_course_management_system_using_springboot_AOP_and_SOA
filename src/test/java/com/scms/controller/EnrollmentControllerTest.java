package com.scms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scms.entity.Enrollment;
import com.scms.entity.Student;
import com.scms.entity.Course;
import com.scms.service.EnrollmentService;
import com.scms.config.TestSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnrollmentController.class)
@Import(TestSecurityConfig.class)
class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnrollmentService enrollmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Enrollment testEnrollment;
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
        testCourse.setCapacity(30);

        testEnrollment = new Enrollment();
        testEnrollment.setId(1L);
        testEnrollment.setStudent(testStudent);
        testEnrollment.setCourse(testCourse);
        testEnrollment.setEnrollmentDate(LocalDateTime.now());
        testEnrollment.setStatus(Enrollment.EnrollmentStatus.ACTIVE);
    }

    @Test
    void enrollStudent_Success() throws Exception {
        when(enrollmentService.enrollStudent(1L, 1L)).thenReturn(testEnrollment);

        mockMvc.perform(post("/api/enrollments/enroll")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"studentId\":1,\"courseId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.enrollment.id").value(1))
                .andExpect(jsonPath("$.enrollment.status").value("ACTIVE"));
    }

    @Test
    void withdrawStudent_Success() throws Exception {
        mockMvc.perform(post("/api/enrollments/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"studentId\":1,\"courseId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Student withdrawn successfully"));
    }

    @Test
    void getStudentEnrollments_Success() throws Exception {
        when(enrollmentService.getStudentEnrollments(1L)).thenReturn(Arrays.asList(testEnrollment));

        mockMvc.perform(get("/api/enrollments/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));
    }

    @Test
    void getCourseEnrollments_Success() throws Exception {
        when(enrollmentService.getCourseEnrollments(1L)).thenReturn(Arrays.asList(testEnrollment));

        mockMvc.perform(get("/api/enrollments/course/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));
    }

    @Test
    void isStudentEnrolled_Success() throws Exception {
        when(enrollmentService.isStudentEnrolled(1L, 1L)).thenReturn(true);

        mockMvc.perform(get("/api/enrollments/check")
                .param("studentId", "1")
                .param("courseId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enrolled").value(true));
    }
}
