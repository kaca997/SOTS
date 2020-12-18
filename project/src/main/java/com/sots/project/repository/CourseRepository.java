package com.sots.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sots.project.model.Course;
import com.sots.project.model.Domain;

public interface CourseRepository extends JpaRepository<Course, Long>{
	
	@Query(value = "SELECT cr.* FROM course cr JOIN student_course sc ON sc.course_id = cr.id WHERE sc.student_id = ?1", nativeQuery = true)	
	List<Course> findStudentCourses(Long studentID);
	
	@Query(value = "SELECT cr.* FROM course cr JOIN teacher_course sc ON sc.course_id = cr.id WHERE sc.teacher_id = ?1", nativeQuery = true)	
	List<Course> findTeacherCourses(Long teacherID);
	
	Course findCourseByDomain(Domain d);

}
