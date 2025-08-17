package com.scms.service;

import com.scms.entity.Enrollment;
import com.scms.entity.Student;
import com.scms.entity.Course;
import com.scms.repository.EnrollmentRepository;
import com.scms.repository.StudentRepository;
import com.scms.repository.CourseRepository;
import com.scms.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

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
    void getStudentEnrollments_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(enrollmentRepository.findByStudent(testStudent)).thenReturn(Arrays.asList(testEnrollment));

        List<Enrollment> result = enrollmentService.getStudentEnrollments(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testEnrollment, result.get(0));
        verify(enrollmentRepository).findByStudent(testStudent);
    }

    @Test
    void getCourseEnrollments_Success() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(enrollmentRepository.findByCourse(testCourse)).thenReturn(Arrays.asList(testEnrollment));

        List<Enrollment> result = enrollmentService.getCourseEnrollments(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testEnrollment, result.get(0));
        verify(enrollmentRepository).findByCourse(testCourse);
    }

    @Test
    void getAllEnrollments_Success() {
        when(enrollmentRepository.findAll()).thenReturn(Arrays.asList(testEnrollment));

        List<Enrollment> result = enrollmentService.getAllEnrollments();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testEnrollment, result.get(0));
        verify(enrollmentRepository).findAll();
    }

    @Test
    void isStudentEnrolled_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(enrollmentRepository.findByStudentAndCourse(testStudent, testCourse)).thenReturn(Optional.of(testEnrollment));

        boolean result = enrollmentService.isStudentEnrolled(1L, 1L);

        assertTrue(result);
        verify(enrollmentRepository).findByStudentAndCourse(testStudent, testCourse);
    }

    @Test
    void isStudentEnrolled_NotEnrolled() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(enrollmentRepository.findByStudentAndCourse(testStudent, testCourse)).thenReturn(Optional.empty());

        boolean result = enrollmentService.isStudentEnrolled(1L, 1L);

        assertFalse(result);
        verify(enrollmentRepository).findByStudentAndCourse(testStudent, testCourse);
    }
}
