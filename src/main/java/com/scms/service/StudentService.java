package com.scms.service;

import com.scms.entity.Student;
import com.scms.entity.Enrollment;
import com.scms.entity.Grade;
import com.scms.entity.Notification;
import com.scms.repository.StudentRepository;
import com.scms.repository.EnrollmentRepository;
import com.scms.repository.GradeRepository;
import com.scms.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    
    @Autowired
    private GradeRepository gradeRepository;
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public Student registerStudent(String name, String email, String password) {
        if (studentRepository.existsByEmail(email)) {
            throw new RuntimeException("Student with email " + email + " already exists");
        }
        
        Student student = new Student(name, email, passwordEncoder.encode(password));
        return studentRepository.save(student);
    }
    
    public Optional<Student> authenticateStudent(String email, String password) {
        Optional<Student> studentOpt = studentRepository.findByEmail(email);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (passwordEncoder.matches(password, student.getPassword())) {
                return Optional.of(student);
            }
        }
        return Optional.empty();
    }
    
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }
    
    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }
    
    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }
    
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
    
    public List<Enrollment> getStudentEnrollments(Long studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isPresent()) {
            return enrollmentRepository.findByStudent(studentOpt.get());
        }
        return List.of();
    }
    
    public List<Grade> getStudentGrades(Long studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isPresent()) {
            return gradeRepository.findByStudent(studentOpt.get());
        }
        return List.of();
    }
    
    public BigDecimal calculateStudentGPA(Long studentId) {
        List<Grade> grades = getStudentGrades(studentId);
        if (grades.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal totalScore = grades.stream()
                .map(Grade::getScore)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return totalScore.divide(BigDecimal.valueOf(grades.size()), 2, BigDecimal.ROUND_HALF_UP);
    }
    
    public List<Notification> getStudentNotifications(Long studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isPresent()) {
            return notificationRepository.findByRecipientOrderBySentDateDesc(studentOpt.get());
        }
        return List.of();
    }
    
    public long getUnreadNotificationCount(Long studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isPresent()) {
            return notificationRepository.countUnreadNotificationsByRecipient(studentOpt.get());
        }
        return 0;
    }
}
