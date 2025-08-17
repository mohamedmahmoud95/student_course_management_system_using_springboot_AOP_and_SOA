package com.scms.repository;

import com.scms.entity.Enrollment;
import com.scms.entity.Student;
import com.scms.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    
    List<Enrollment> findByStudent(Student student);
    
    List<Enrollment> findByCourse(Course course);
    
    @Query("SELECT e FROM Enrollment e WHERE e.student = :student AND e.status = 'ACTIVE'")
    List<Enrollment> findActiveEnrollmentsByStudent(@Param("student") Student student);
    
    @Query("SELECT e FROM Enrollment e WHERE e.course = :course AND e.status = 'ACTIVE'")
    List<Enrollment> findActiveEnrollmentsByCourse(@Param("course") Course course);
    
    Optional<Enrollment> findByStudentAndCourse(Student student, Course course);
    
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course = :course AND e.status = 'ACTIVE'")
    long countActiveEnrollmentsByCourse(@Param("course") Course course);

    List<Enrollment> findByStatus(Enrollment.EnrollmentStatus status);
}
