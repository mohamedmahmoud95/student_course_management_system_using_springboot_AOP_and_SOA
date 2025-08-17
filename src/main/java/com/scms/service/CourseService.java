package com.scms.service;

import com.scms.entity.Course;
import com.scms.entity.Enrollment;
import com.scms.repository.CourseRepository;
import com.scms.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    
    public Course createCourse(String title, String description, Integer capacity, String prerequisites) {
        Course course = new Course(title, description, capacity, prerequisites);
        return courseRepository.save(course);
    }
    
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }
    
    public List<Course> getAvailableCourses() {
        return courseRepository.findAvailableCourses();
    }
    
    public List<Course> getFullCourses() {
        return courseRepository.findFullCourses();
    }
    
    public List<Course> searchCoursesByTitle(String title) {
        return courseRepository.findByTitleContainingIgnoreCase(title);
    }
    
    public Course updateCourse(Course course) {
        return courseRepository.save(course);
    }
    
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
    
    public boolean isCourseAvailable(Long courseId) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            long currentEnrollments = enrollmentRepository.countActiveEnrollmentsByCourse(course);
            return currentEnrollments < course.getCapacity();
        }
        return false;
    }
    
    public int getAvailableSeats(Long courseId) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            long currentEnrollments = enrollmentRepository.countActiveEnrollmentsByCourse(course);
            return (int) (course.getCapacity() - currentEnrollments);
        }
        return 0;
    }
    
    public List<Enrollment> getCourseEnrollments(Long courseId) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isPresent()) {
            return enrollmentRepository.findByCourse(courseOpt.get());
        }
        return List.of();
    }
}
