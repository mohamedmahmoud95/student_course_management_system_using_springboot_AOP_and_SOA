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
    
    public Student createStudent(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("Student with email " + student.getEmail() + " already exists");
        }
        
        // Encode password if not already encoded
        if (!student.getPassword().startsWith("$2a$")) {
            student.setPassword(passwordEncoder.encode(student.getPassword()));
        }
        
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

    public Student findByEmail(String email) {
        return studentRepository.findByEmail(email).orElse(null);
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
        
        BigDecimal totalGPA = BigDecimal.ZERO;
        int totalCredits = 0;
        
        for (Grade grade : grades) {
            BigDecimal score = grade.getScore();
            // Convert percentage to 4.0 scale
            BigDecimal gpa;
            if (score.compareTo(new BigDecimal("93")) >= 0) {
                gpa = new BigDecimal("4.0");
            } else if (score.compareTo(new BigDecimal("90")) >= 0) {
                gpa = new BigDecimal("3.7");
            } else if (score.compareTo(new BigDecimal("87")) >= 0) {
                gpa = new BigDecimal("3.3");
            } else if (score.compareTo(new BigDecimal("83")) >= 0) {
                gpa = new BigDecimal("3.0");
            } else if (score.compareTo(new BigDecimal("80")) >= 0) {
                gpa = new BigDecimal("2.7");
            } else if (score.compareTo(new BigDecimal("77")) >= 0) {
                gpa = new BigDecimal("2.3");
            } else if (score.compareTo(new BigDecimal("73")) >= 0) {
                gpa = new BigDecimal("2.0");
            } else if (score.compareTo(new BigDecimal("70")) >= 0) {
                gpa = new BigDecimal("1.7");
            } else if (score.compareTo(new BigDecimal("67")) >= 0) {
                gpa = new BigDecimal("1.3");
            } else if (score.compareTo(new BigDecimal("63")) >= 0) {
                gpa = new BigDecimal("1.0");
            } else if (score.compareTo(new BigDecimal("60")) >= 0) {
                gpa = new BigDecimal("0.7");
            } else {
                gpa = BigDecimal.ZERO;
            }
            
            totalGPA = totalGPA.add(gpa);
            totalCredits++;
        }
        
        return totalCredits > 0 ? totalGPA.divide(BigDecimal.valueOf(totalCredits), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
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
