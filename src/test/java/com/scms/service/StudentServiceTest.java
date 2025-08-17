package com.scms.service;

import com.scms.entity.Student;
import com.scms.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private StudentService studentService;

    private Student testStudent;

    @BeforeEach
    void setUp() {
        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setName("John Doe");
        testStudent.setEmail("john@test.com");
        testStudent.setPassword("encodedPassword");
    }

    @Test
    void registerStudent_Success() {
        when(studentRepository.existsByEmail("john@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        Student result = studentService.registerStudent("John Doe", "john@test.com", "password123");

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john@test.com", result.getEmail());
        verify(studentRepository).existsByEmail("john@test.com");
        verify(passwordEncoder).encode("password123");
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void registerStudent_EmailAlreadyExists() {
        when(studentRepository.existsByEmail("john@test.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> {
            studentService.registerStudent("John Doe", "john@test.com", "password123");
        });

        verify(studentRepository).existsByEmail("john@test.com");
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void authenticateStudent_Success() {
        when(studentRepository.findByEmail("john@test.com")).thenReturn(Optional.of(testStudent));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

        Optional<Student> result = studentService.authenticateStudent("john@test.com", "password123");

        assertTrue(result.isPresent());
        assertEquals(testStudent, result.get());
        verify(studentRepository).findByEmail("john@test.com");
        verify(passwordEncoder).matches("password123", "encodedPassword");
    }

    @Test
    void authenticateStudent_InvalidCredentials() {
        when(studentRepository.findByEmail("john@test.com")).thenReturn(Optional.of(testStudent));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        Optional<Student> result = studentService.authenticateStudent("john@test.com", "wrongPassword");

        assertFalse(result.isPresent());
        verify(studentRepository).findByEmail("john@test.com");
        verify(passwordEncoder).matches("wrongPassword", "encodedPassword");
    }

    @Test
    void getStudentById_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        Optional<Student> result = studentService.getStudentById(1L);

        assertTrue(result.isPresent());
        assertEquals(testStudent, result.get());
        verify(studentRepository).findById(1L);
    }

    @Test
    void getStudentById_NotFound() {
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Student> result = studentService.getStudentById(999L);

        assertFalse(result.isPresent());
        verify(studentRepository).findById(999L);
    }
}
