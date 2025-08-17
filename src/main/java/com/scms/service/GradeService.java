package com.scms.service;

import com.scms.entity.Grade;
import com.scms.entity.Student;
import com.scms.entity.Course;
import com.scms.entity.Notification;
import com.scms.repository.GradeRepository;
import com.scms.repository.StudentRepository;
import com.scms.repository.CourseRepository;
import com.scms.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GradeService {
    
    @Autowired
    private GradeRepository gradeRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    public Grade recordGrade(Long studentId, Long courseId, BigDecimal score, String comments) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        
        if (studentOpt.isEmpty() || courseOpt.isEmpty()) {
            throw new RuntimeException("Student or course not found");
        }
        
        Student student = studentOpt.get();
        Course course = courseOpt.get();
        
        if (score.compareTo(BigDecimal.ZERO) < 0 || score.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new RuntimeException("Grade must be between 0 and 100");
        }
        
        Optional<Grade> existingGrade = gradeRepository.findByStudentAndCourse(student, course);
        Grade grade;
        
        if (existingGrade.isPresent()) {
            grade = existingGrade.get();
            grade.setScore(score);
            grade.setComments(comments);
        } else {
            grade = new Grade(student, course, score);
            grade.setComments(comments);
        }
        
        grade = gradeRepository.save(grade);
        
        Notification notification = new Notification(
            "Grade updated for " + course.getTitle() + ": " + score + "% (" + grade.getLetterGrade() + ")",
            student,
            Notification.NotificationType.GRADE_UPDATE
        );
        notificationRepository.save(notification);
        
        return grade;
    }
    
    public Grade updateGrade(Long gradeId, BigDecimal score, String comments) {
        Optional<Grade> gradeOpt = gradeRepository.findById(gradeId);
        if (gradeOpt.isEmpty()) {
            throw new RuntimeException("Grade not found");
        }
        
        Grade grade = gradeOpt.get();
        grade.setScore(score);
        grade.setComments(comments);
        
        grade = gradeRepository.save(grade);
        
        Notification notification = new Notification(
            "Grade updated for " + grade.getCourse().getTitle() + ": " + score + "% (" + grade.getLetterGrade() + ")",
            grade.getStudent(),
            Notification.NotificationType.GRADE_UPDATE
        );
        notificationRepository.save(notification);
        
        return grade;
    }
    
    public List<Grade> getStudentGrades(Long studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isPresent()) {
            return gradeRepository.findByStudent(studentOpt.get());
        }
        return List.of();
    }
    
    public List<Grade> getCourseGrades(Long courseId) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isPresent()) {
            return gradeRepository.findByCourse(courseOpt.get());
        }
        return List.of();
    }
    
    public Optional<Grade> getGrade(Long studentId, Long courseId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        
        if (studentOpt.isPresent() && courseOpt.isPresent()) {
            return gradeRepository.findByStudentAndCourse(studentOpt.get(), courseOpt.get());
        }
        return Optional.empty();
    }
    
    public BigDecimal getStudentGPA(Long studentId) {
        Double averageGrade = gradeRepository.getAverageGradeByStudent(studentRepository.findById(studentId).orElse(null));
        return averageGrade != null ? BigDecimal.valueOf(averageGrade) : BigDecimal.ZERO;
    }
    
    public BigDecimal getCourseAverage(Long courseId) {
        Double averageGrade = gradeRepository.getAverageGradeByCourse(courseRepository.findById(courseId).orElse(null));
        return averageGrade != null ? BigDecimal.valueOf(averageGrade) : BigDecimal.ZERO;
    }
    
    public void deleteGrade(Long gradeId) {
        gradeRepository.deleteById(gradeId);
    }
}
