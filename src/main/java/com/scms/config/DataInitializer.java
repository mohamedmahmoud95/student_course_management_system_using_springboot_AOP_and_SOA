package com.scms.config;

import com.scms.entity.Administrator;
import com.scms.entity.Student;
import com.scms.entity.Course;
import com.scms.repository.AdministratorRepository;
import com.scms.repository.StudentRepository;
import com.scms.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private AdministratorRepository administratorRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        initializeAdministrators();
        initializeStudents();
        initializeCourses();
    }
    
    private void initializeAdministrators() {
        if (administratorRepository.count() == 0) {
            Administrator admin = new Administrator("Admin User", "admin@scms.com", passwordEncoder.encode("admin123"));
            administratorRepository.save(admin);
        }
    }
    
    private void initializeStudents() {
        if (studentRepository.count() == 0) {
            Student student1 = new Student("John Doe", "john@student.com", passwordEncoder.encode("student123"));
            Student student2 = new Student("Jane Smith", "jane@student.com", passwordEncoder.encode("student123"));
            Student student3 = new Student("Bob Johnson", "bob@student.com", passwordEncoder.encode("student123"));
            
            studentRepository.save(student1);
            studentRepository.save(student2);
            studentRepository.save(student3);
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
}
