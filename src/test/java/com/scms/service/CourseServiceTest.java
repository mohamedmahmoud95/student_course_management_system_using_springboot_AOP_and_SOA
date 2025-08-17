package com.scms.service;

import com.scms.entity.Course;
import com.scms.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

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
    void createCourse_Success() {
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        Course result = courseService.createCourse("Introduction to Computer Science", "Basic concepts", 30, "None");

        assertNotNull(result);
        assertEquals("Introduction to Computer Science", result.getTitle());
        assertEquals(30, result.getCapacity());
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void getAllCourses_Success() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(testCourse));

        List<Course> result = courseService.getAllCourses();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCourse, result.get(0));
        verify(courseRepository).findAll();
    }

    @Test
    void getCourseById_Success() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));

        Optional<Course> result = courseService.getCourseById(1L);

        assertTrue(result.isPresent());
        assertEquals(testCourse, result.get());
        verify(courseRepository).findById(1L);
    }

    @Test
    void getCourseById_NotFound() {
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Course> result = courseService.getCourseById(999L);

        assertFalse(result.isPresent());
        verify(courseRepository).findById(999L);
    }

    @Test
    void updateCourse_Success() {
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        Course updatedCourse = new Course();
        updatedCourse.setId(1L);
        updatedCourse.setTitle("Updated Course Title");
        updatedCourse.setCapacity(25);

        Course result = courseService.updateCourse(updatedCourse);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void deleteCourse_Success() {
        courseService.deleteCourse(1L);

        verify(courseRepository).deleteById(1L);
    }

    @Test
    void getAvailableCourses_Success() {
        when(courseRepository.findAvailableCourses()).thenReturn(Arrays.asList(testCourse));

        List<Course> result = courseService.getAvailableCourses();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCourse, result.get(0));
        verify(courseRepository).findAvailableCourses();
    }
}
