package com.sots.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.sots.project.dto.KnowledgeSpaceDTO;
import com.sots.project.dto.RelationDTO;
import com.sots.project.service.InvalidDataException;
import com.sots.project.service.KnowledgeSpaceService;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/knowledgeSpace")
public class KnowledgeSpaceController {
	
	@Autowired
	private KnowledgeSpaceService knowledgeSpaceService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	//@PreAuthorize("hasRole('ROLE_TEACHER')")
	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody KnowledgeSpaceDTO ksDTO) {
		try {
			return new ResponseEntity<>(knowledgeSpaceService.save(ksDTO), HttpStatus.OK);
		} catch (InvalidDataException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PreAuthorize("hasRole('ROLE_TEACHER')")
	@GetMapping("/sendReqRealKS/{domainId}")
	public ResponseEntity<?> getRealKS(@PathVariable Long domainId, @RequestHeader(value="Authorization") String token) {
		try {
			JSONObject o = knowledgeSpaceService.getMatrixForRealKS(domainId);
			HttpHeaders httpHeaders = new HttpHeaders();
		    httpHeaders.set("Authorization", token);
		    System.out.println(o.toJSONString());
			ResponseEntity<RelationDTO[]> message = restTemplate.postForEntity("http://localhost:5000/getRealKS",  o.toJSONString(),RelationDTO[].class);
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (InvalidDataException e) { 
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
	        return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_GATEWAY);
		}
//		return new ResponseEntity<>(knowledgeSpaceService.getRealKS(), HttpStatus.OK);
	}

}
