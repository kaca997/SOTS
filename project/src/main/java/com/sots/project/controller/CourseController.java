package com.sots.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sots.project.model.Course;
import com.sots.project.service.CourseService;

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
}
