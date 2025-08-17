package com.scms.service;

import com.scms.entity.Administrator;
import com.scms.entity.Student;
import com.scms.entity.Course;
import com.scms.entity.Enrollment;
import com.scms.entity.Grade;
import com.scms.repository.AdministratorRepository;
import com.scms.repository.StudentRepository;
import com.scms.repository.CourseRepository;
import com.scms.repository.EnrollmentRepository;
import com.scms.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdministratorService {
    
    @Autowired
    private AdministratorRepository administratorRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    
    @Autowired
    private GradeRepository gradeRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public Administrator createAdministrator(String name, String email, String password) {
        if (administratorRepository.existsByEmail(email)) {
            throw new RuntimeException("Administrator with email " + email + " already exists");
        }
        
        Administrator administrator = new Administrator(name, email, passwordEncoder.encode(password));
        return administratorRepository.save(administrator);
    }
    
    public Optional<Administrator> authenticateAdministrator(String email, String password) {
        Optional<Administrator> adminOpt = administratorRepository.findByEmail(email);
        if (adminOpt.isPresent()) {
            Administrator administrator = adminOpt.get();
            if (passwordEncoder.matches(password, administrator.getPassword())) {
                return Optional.of(administrator);
            }
        }
        return Optional.empty();
    }
    
    public List<Administrator> getAllAdministrators() {
        return administratorRepository.findAll();
    }
    
    public Optional<Administrator> getAdministratorById(Long id) {
        return administratorRepository.findById(id);
    }
    
    public Administrator updateAdministrator(Administrator administrator) {
        return administratorRepository.save(administrator);
    }
    
    public void deleteAdministrator(Long id) {
        administratorRepository.deleteById(id);
    }
    
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }
    
    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }
    
    public Map<String, Object> generateEnrollmentReport() {
        List<Course> courses = courseRepository.findAll();
        Map<String, Object> report = Map.of(
            "totalCourses", courses.size(),
            "totalStudents", studentRepository.count(),
            "totalEnrollments", enrollmentRepository.count(),
            "courseEnrollmentStats", courses.stream()
                .collect(Collectors.toMap(
                    Course::getTitle,
                    course -> Map.of(
                        "capacity", course.getCapacity(),
                        "enrolled", enrollmentRepository.countActiveEnrollmentsByCourse(course),
                        "available", course.getCapacity() - enrollmentRepository.countActiveEnrollmentsByCourse(course)
                    )
                ))
        );
        return report;
    }
    
    public Map<String, Object> generateGradeReport() {
        List<Course> courses = courseRepository.findAll();
        Map<String, Object> report = Map.of(
            "totalGrades", gradeRepository.count(),
            "courseGradeStats", courses.stream()
                .collect(Collectors.toMap(
                    Course::getTitle,
                    course -> {
                        Double avgGrade = gradeRepository.getAverageGradeByCourse(course);
                        return Map.of(
                            "averageGrade", avgGrade != null ? avgGrade : 0.0,
                            "totalGrades", gradeRepository.findByCourse(course).size()
                        );
                    }
                ))
        );
        return report;
    }
    
    public Map<String, Object> generateStudentReport(Long studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) {
            throw new RuntimeException("Student not found");
        }
        
        Student student = studentOpt.get();
        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
        List<Grade> grades = gradeRepository.findByStudent(student);
        
        Double averageGrade = gradeRepository.getAverageGradeByStudent(student);
        
        return Map.of(
            "studentId", student.getId(),
            "studentName", student.getName(),
            "studentEmail", student.getEmail(),
            "totalEnrollments", enrollments.size(),
            "activeEnrollments", enrollments.stream()
                .filter(e -> e.getStatus() == Enrollment.EnrollmentStatus.ACTIVE)
                .count(),
            "totalGrades", grades.size(),
            "averageGrade", averageGrade != null ? averageGrade : 0.0,
            "gpa", averageGrade != null ? BigDecimal.valueOf(averageGrade) : BigDecimal.ZERO
        );
    }
}
