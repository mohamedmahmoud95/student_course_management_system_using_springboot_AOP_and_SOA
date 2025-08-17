package com.scms.repository;

import com.scms.entity.Grade;
import com.scms.entity.Student;
import com.scms.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    
    List<Grade> findByStudent(Student student);
    
    List<Grade> findByCourse(Course course);
    
    Optional<Grade> findByStudentAndCourse(Student student, Course course);
    
    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.student = :student")
    Double getAverageGradeByStudent(@Param("student") Student student);
    
    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.course = :course")
    Double getAverageGradeByCourse(@Param("course") Course course);
}
