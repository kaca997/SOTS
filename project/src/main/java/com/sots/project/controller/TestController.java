package com.sots.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sots.project.dto.TestDetailsDTO;
import com.sots.project.dto.TestDTO;
import com.sots.project.dto.UpdateTestDTO;
import com.sots.project.model.Test;
import com.sots.project.service.InvalidDataException;
import com.sots.project.service.TestService;

@RestController
@RequestMapping("/test")
public class TestController {
	
	@Autowired
	private TestService testService;
	
	@PreAuthorize("hasRole('ROLE_TEACHER')")
	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody TestDTO testDTO) {
		try {
			return new ResponseEntity<>(testService.save(testDTO), HttpStatus.OK);
		} catch (InvalidDataException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	
	@PreAuthorize("hasRole('ROLE_TEACHER')")
	@PutMapping("/update")
	public ResponseEntity<?> update(@RequestBody UpdateTestDTO test) {
		System.out.println(test);
		try {
			return new ResponseEntity<>(testService.updateTest(test), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PreAuthorize("hasRole('ROLE_TEACHER')")
	@PutMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		try {
			testService.deleteTest(id);
			return new ResponseEntity<>("Test successfully deleted!", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<List<Test>> getAll() {
		return new ResponseEntity<>(testService.getAll(), HttpStatus.OK);
	}	
	
	@GetMapping("/getCourseTestsToDo/{courseId}")
	public ResponseEntity<?> getAllTestForStudentByCourse(@PathVariable Long courseId) {
		System.out.println(courseId);
		try {
			return new ResponseEntity<>(testService.getCourseTestsToDo(courseId), HttpStatus.OK);
		}
		catch (InvalidDataException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		catch(Exception e){
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/getCourseTestsForTeacher/{courseId}")
	public ResponseEntity<?> getCourseTestsForTeacher(@PathVariable Long courseId) {
		System.out.println(courseId);
		try {
			return new ResponseEntity<>(testService.getTeacherCourseTest(courseId), HttpStatus.OK);
		}
		catch (InvalidDataException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		catch(Exception e){
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/getTest/{testId}")
	public ResponseEntity<?> getTest(@PathVariable Long testId) {
		System.out.println(testId);
		try {
			TestDetailsDTO dto = testService.getTest(testId);
			return new ResponseEntity<>(dto, HttpStatus.OK);
		}
		catch (InvalidDataException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/getTestTeacher/{testId}")
	public ResponseEntity<?> getTestTeacher(@PathVariable Long testId) {
		System.out.println(testId);
		try {
			TestDetailsDTO dto = testService.getTestTeacher(testId);
			return new ResponseEntity<>(dto, HttpStatus.OK);
		}
		catch (InvalidDataException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/getCoursesByTeacher")
	public ResponseEntity<?> getCoursesToAddTest(){
		try {
			return new ResponseEntity<>(testService.getCoursesByTeacher(), HttpStatus.OK);
		} catch (InvalidDataException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
