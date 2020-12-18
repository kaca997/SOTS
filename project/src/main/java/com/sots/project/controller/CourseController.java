package com.sots.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sots.project.service.CourseService;
import com.sots.project.service.InvalidDataException;

@RestController
@RequestMapping("/course")
public class CourseController {

	@Autowired
	private CourseService courseService;
	
	@GetMapping("/getAllForStudent")
	public ResponseEntity<?> getAllForStudent() {
		try {
			return new ResponseEntity<>(courseService.getAllForStudent(), HttpStatus.OK);
		}
		catch(Exception e){
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/getAllForTeacher")
	public ResponseEntity<?> getAllForTeacher() {
		try {
			return new ResponseEntity<>(courseService.getAllForTeacher(), HttpStatus.OK);
		}
		catch(Exception e){
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/getCoursesWithoutDomain")
	public ResponseEntity<?> getWithoutDomain() {
		try {
			return new ResponseEntity<>(courseService.getWithoutDomain(), HttpStatus.OK);
		}
		catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/getProblemsByCourse/{courseId}")
	public ResponseEntity<?> getProblemsByCourse(@PathVariable Long courseId) {
		try {
			return new ResponseEntity<>(courseService.getProblemsByCourse(courseId), HttpStatus.OK);
		}
		catch (InvalidDataException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
}
