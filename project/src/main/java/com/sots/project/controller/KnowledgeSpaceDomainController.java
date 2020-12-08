package com.sots.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sots.project.dto.KnowledgeSpaceDomainDTO;
import com.sots.project.service.InvalidDataException;
import com.sots.project.service.KnowledgeSpaceService;

@RestController
@RequestMapping("/knowledgeSpace")
public class KnowledgeSpaceDomainController {
	
	@Autowired
	private KnowledgeSpaceService knowledgeSpaceDomainService;
	
	//@PreAuthorize("hasRole('ROLE_TEACHER')")
	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody KnowledgeSpaceDomainDTO domainDTO) {
		try {
			return new ResponseEntity<>(knowledgeSpaceDomainService.save(domainDTO), HttpStatus.OK);
		} catch (InvalidDataException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
