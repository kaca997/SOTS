package com.sots.project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sots.project.dto.CourseDTO;
import com.sots.project.model.Course;
import com.sots.project.model.Student;
import com.sots.project.model.Teacher;
import com.sots.project.repository.CourseRepository;

@Service
public class CourseService {
	
	@Autowired
	private CourseRepository courseRepository;
	
	public List<Course> getAllForStudent() {
		
		Student s = (Student) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Course> courses  = courseRepository.findStudentCourses(s.getId());
		return courses;
	}
	
	public List<Course> getAllForTeacher() {
		
		Teacher t = (Teacher) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Course> courses  = courseRepository.findTeacherCourses(t.getId());
		for(Course c: courses) {
			c.getDomain();
		}
		System.out.println(courses);
		return courses;
	}
	
	public List<CourseDTO> getWithoutDomain() {
		
		List<Course> teacherCurses  = this.getAllForTeacher();
		List<CourseDTO> courses = new ArrayList<CourseDTO>();
		for(Course c: teacherCurses) {
			if(c.getDomain() == null) {
				courses.add(new CourseDTO(c.getId(), c.getName()));
			}
		}
		return courses;
		
	}


}
