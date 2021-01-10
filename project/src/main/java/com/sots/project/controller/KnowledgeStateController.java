package com.sots.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sots.project.service.InvalidDataException;
import com.sots.project.service.KnowledgeStateService;

@RestController
@RequestMapping("/knowledgeState")
public class KnowledgeStateController {

	@Autowired
	private KnowledgeStateService knowledgeStateService;
	
//	@PreAuthorize("hasRole('ROLE_STUDENT')")
	@GetMapping("/getStatesGraph/{domainID}")
	public ResponseEntity<?> getStateGraph(@PathVariable Long domainID) {
		try {
			return new ResponseEntity<>(knowledgeStateService.generateKnowledgeStatesGraph(domainID), HttpStatus.OK);
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
	
	@GetMapping("/getStatesGraphTest/{testID}")
	public ResponseEntity<?> getStateGraphTest(@PathVariable Long testID) {
		try {
			return new ResponseEntity<>(knowledgeStateService.generateKnowledgeStatesGraphTest(testID), HttpStatus.OK);
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
	
	@GetMapping("/generateStates/{testID}")
	public ResponseEntity<?> getDoneTests(@PathVariable Long testID) {
		try {
			return new ResponseEntity<>(knowledgeStateService.generateAllPossibleStates(testID), HttpStatus.OK);
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
