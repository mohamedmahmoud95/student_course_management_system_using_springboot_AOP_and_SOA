package com.scms.repository;

import com.scms.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    List<Course> findByTitleContainingIgnoreCase(String title);
    
    @Query("SELECT c FROM Course c WHERE c.capacity > (SELECT COUNT(e) FROM Enrollment e WHERE e.course = c AND e.status = 'ACTIVE')")
    List<Course> findAvailableCourses();
    
    @Query("SELECT c FROM Course c WHERE c.capacity <= (SELECT COUNT(e) FROM Enrollment e WHERE e.course = c AND e.status = 'ACTIVE')")
    List<Course> findFullCourses();
}
