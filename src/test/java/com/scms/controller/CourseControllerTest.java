package com.scms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scms.entity.Course;
import com.scms.service.CourseService;
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

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
@Import(TestSecurityConfig.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private EnrollmentService enrollmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Course testCourse;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setTitle("Introduction to Computer Science");
        testCourse.setDescription("Basic concepts of computer science");
        testCourse.setPrerequisites("None");
        testCourse.setCapacity(30);
    }

    @Test
    void createCourse_Success() throws Exception {
        when(courseService.createCourse(any(String.class), any(String.class), any(Integer.class), any(String.class)))
                .thenReturn(testCourse);

        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Introduction to Computer Science\",\"description\":\"Basic concepts of computer science\",\"prerequisites\":\"None\",\"capacity\":30}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Introduction to Computer Science"))
                .andExpect(jsonPath("$.capacity").value(30));
    }

    @Test
    void getAllCourses_Success() throws Exception {
        when(courseService.getAllCourses()).thenReturn(Arrays.asList(testCourse));

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Introduction to Computer Science"));
    }

    @Test
    void getCourseById_Success() throws Exception {
        when(courseService.getCourseById(1L)).thenReturn(Optional.of(testCourse));

        mockMvc.perform(get("/api/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Introduction to Computer Science"));
    }

    @Test
    void getCourseById_NotFound() throws Exception {
        when(courseService.getCourseById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/courses/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCourse_Success() throws Exception {
        when(courseService.updateCourse(any(Course.class))).thenReturn(testCourse);

        mockMvc.perform(put("/api/courses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Updated Course Title\",\"description\":\"Updated description\",\"prerequisites\":\"None\",\"capacity\":25}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deleteCourse_Success() throws Exception {
        mockMvc.perform(delete("/api/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Course deleted successfully"));
    }

    @Test
    void getAvailableCourses_Success() throws Exception {
        when(courseService.getAvailableCourses()).thenReturn(Arrays.asList(testCourse));

        mockMvc.perform(get("/api/courses/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Introduction to Computer Science"));
    }
}
