package com.scms.config;

import com.scms.entity.Administrator;
import com.scms.entity.Student;
import com.scms.entity.Course;
import com.scms.entity.Enrollment;
import com.scms.entity.Grade;
import com.scms.entity.Notification;
import com.scms.repository.AdministratorRepository;
import com.scms.repository.StudentRepository;
import com.scms.repository.CourseRepository;
import com.scms.repository.EnrollmentRepository;
import com.scms.repository.GradeRepository;
import com.scms.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {
    
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
    private NotificationRepository notificationRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        initializeAdministrators();
        initializeStudents();
        initializeCourses();
        initializeEnrollments();
        initializeGrades();
        initializeNotifications();
    }
    
    private void initializeAdministrators() {
        if (administratorRepository.count() == 0) {
            Administrator admin1 = new Administrator("أحمد محمود", "ahmed.mahmoud@eng.asu.edu.eg", passwordEncoder.encode("admin123"));
            Administrator admin2 = new Administrator("فاطمة علي", "fatima.ali@eng.asu.edu.eg", passwordEncoder.encode("admin123"));
            
            administratorRepository.save(admin1);
            administratorRepository.save(admin2);
        }
    }
    
    private void initializeStudents() {
        if (studentRepository.count() == 0) {
            Student student1 = new Student("محمد رسلان", "mohamed.raslan@eng.asu.edu.eg", passwordEncoder.encode("password123"));
            Student student2 = new Student("عمر أحمد", "omar.ahmed@eng.asu.edu.eg", passwordEncoder.encode("password123"));
            Student student3 = new Student("علي محمد", "ali.mohamed@eng.asu.edu.eg", passwordEncoder.encode("password123"));
            Student student4 = new Student("سارة محمود", "sara.mahmoud@eng.asu.edu.eg", passwordEncoder.encode("password123"));
            Student student5 = new Student("مريم علي", "maryam.ali@eng.asu.edu.eg", passwordEncoder.encode("password123"));
            
            studentRepository.save(student1);
            studentRepository.save(student2);
            studentRepository.save(student3);
            studentRepository.save(student4);
            studentRepository.save(student5);
        }
    }
    
    private void initializeCourses() {
        if (courseRepository.count() == 0) {
            Course course1 = new Course("Introduction to Computer Science", 
                "Basic concepts of programming and computer science", 30, "None");
            Course course2 = new Course("Data Structures and Algorithms", 
                "Advanced programming concepts and problem solving", 25, "Introduction to Computer Science");
            Course course3 = new Course("Database Management Systems", 
                "Design and implementation of database systems", 20, "Introduction to Computer Science");
            Course course4 = new Course("Web Development", 
                "Building modern web applications", 35, "None");
            Course course5 = new Course("Software Engineering", 
                "Software development methodologies and practices", 30, "Data Structures and Algorithms");
            
            courseRepository.save(course1);
            courseRepository.save(course2);
            courseRepository.save(course3);
            courseRepository.save(course4);
            courseRepository.save(course5);
        }
    }
    
    private void initializeEnrollments() {
        if (enrollmentRepository.count() == 0) {
            Student student1 = studentRepository.findById(1L).orElse(null);
            Student student2 = studentRepository.findById(2L).orElse(null);
            Student student3 = studentRepository.findById(3L).orElse(null);
            
            Course course1 = courseRepository.findById(1L).orElse(null);
            Course course2 = courseRepository.findById(2L).orElse(null);
            Course course3 = courseRepository.findById(3L).orElse(null);
            Course course4 = courseRepository.findById(4L).orElse(null);
            
            if (student1 != null && course1 != null) {
                Enrollment enrollment1 = new Enrollment(student1, course1);
                enrollmentRepository.save(enrollment1);
            }
            
            if (student1 != null && course2 != null) {
                Enrollment enrollment2 = new Enrollment(student1, course2);
                enrollmentRepository.save(enrollment2);
            }
            
            if (student2 != null && course1 != null) {
                Enrollment enrollment3 = new Enrollment(student2, course1);
                enrollmentRepository.save(enrollment3);
            }
            
            if (student2 != null && course3 != null) {
                Enrollment enrollment4 = new Enrollment(student2, course3);
                enrollmentRepository.save(enrollment4);
            }
            
            if (student3 != null && course4 != null) {
                Enrollment enrollment5 = new Enrollment(student3, course4);
                enrollmentRepository.save(enrollment5);
            }
        }
    }
    
    private void initializeGrades() {
        if (gradeRepository.count() == 0) {
            Student student1 = studentRepository.findById(1L).orElse(null);
            Student student2 = studentRepository.findById(2L).orElse(null);
            
            Course course1 = courseRepository.findById(1L).orElse(null);
            Course course2 = courseRepository.findById(2L).orElse(null);
            
            if (student1 != null && course1 != null) {
                Grade grade1 = new Grade(student1, course1, new BigDecimal("95.5"));
                grade1.setComments("Excellent performance");
                gradeRepository.save(grade1);
            }
            
            if (student1 != null && course2 != null) {
                Grade grade2 = new Grade(student1, course2, new BigDecimal("88.0"));
                grade2.setComments("Very good work");
                gradeRepository.save(grade2);
            }
            
            if (student2 != null && course1 != null) {
                Grade grade3 = new Grade(student2, course1, new BigDecimal("92.5"));
                grade3.setComments("Outstanding performance");
                gradeRepository.save(grade3);
            }
        }
    }
    
    private void initializeNotifications() {
        if (notificationRepository.count() == 0) {
            Student student1 = studentRepository.findById(1L).orElse(null);
            Student student2 = studentRepository.findById(2L).orElse(null);
            
            if (student1 != null) {
                Notification notification1 = new Notification("Successfully enrolled in Introduction to Computer Science", 
                    student1, Notification.NotificationType.ENROLLMENT);
                notificationRepository.save(notification1);
                
                Notification notification2 = new Notification("New grade recorded for Introduction to Computer Science", 
                    student1, Notification.NotificationType.GRADE_UPDATE);
                notificationRepository.save(notification2);
            }
            
            if (student2 != null) {
                Notification notification3 = new Notification("Successfully enrolled in Introduction to Computer Science", 
                    student2, Notification.NotificationType.ENROLLMENT);
                notificationRepository.save(notification3);
            }
        }
    }
}
