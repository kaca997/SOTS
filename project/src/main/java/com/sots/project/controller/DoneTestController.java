package com.sots.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sots.project.dto.TestDetailsDTO;
import com.sots.project.service.DoneTestService;
import com.sots.project.service.InvalidDataException;

@RestController
@RequestMapping("/test")
public class DoneTestController {
	
	@Autowired
	private DoneTestService doneTestService;
	
	@PreAuthorize("hasRole('ROLE_STUDENT')")
	@GetMapping("/getDoneTests/{courseId}")
	public ResponseEntity<?> getDoneTests(@PathVariable Long courseId) {
		System.out.println(courseId);
		try {
			System.out.println("*********************"+ doneTestService.getDoneTests(courseId).size());
			return new ResponseEntity<>(doneTestService.getDoneTests(courseId), HttpStatus.OK);
		}
		catch (InvalidDataException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		catch(Exception e){
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PreAuthorize("hasRole('ROLE_STUDENT')")
	@PostMapping("/submitTest")
	public ResponseEntity<?> submitTest(@RequestBody TestDetailsDTO dto) {
		try {
			TestDetailsDTO d = doneTestService.submitTest(dto);
			System.out.println(d);
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
	
	@PreAuthorize("hasRole('ROLE_STUDENT')")
	@GetMapping("/getDoneTest/{doneTestId}")
	public ResponseEntity<?> getTest(@PathVariable Long doneTestId) {
		System.out.println(doneTestId);
		try {
			TestDetailsDTO dto = doneTestService.getDoneTest(doneTestId);
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
}
