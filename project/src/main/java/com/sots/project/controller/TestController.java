package com.sots.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<?> create(@RequestBody Test test) {
		try {
			return new ResponseEntity<>(testService.save(test), HttpStatus.OK);
		} catch (InvalidDataException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<List<Test>> getAll() {
		return new ResponseEntity<>(testService.getAll(), HttpStatus.OK);
	}

}
