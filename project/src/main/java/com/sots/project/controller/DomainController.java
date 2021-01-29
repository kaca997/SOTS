package com.sots.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

import com.sots.project.dto.NewDomainDTO;
import com.sots.project.dto.RelationDTO;
import com.sots.project.model.KnowledgeSpace;
import com.sots.project.service.DomainService;
import com.sots.project.service.InvalidDataException;
import com.sots.project.service.KnowledgeSpaceService;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/domain")
public class DomainController {

	@Autowired
	private DomainService domainService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private KnowledgeSpaceService knowledgeSpaceService;
	
	
	@PreAuthorize("hasRole('ROLE_TEACHER')")
	@PostMapping("/create")
	public ResponseEntity<String> createDomain(@RequestBody NewDomainDTO domainDTO){
		System.out.println(domainDTO);
		try {
			return new ResponseEntity<>(domainService.save(domainDTO), HttpStatus.CREATED);
		} catch (InvalidDataException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PreAuthorize("hasRole('ROLE_TEACHER')")
	@GetMapping("/get/{id}")
	public ResponseEntity<?> getDomain(@PathVariable Long id, @RequestHeader(value="Authorization") String token){
		try {
			
			KnowledgeSpace ks = knowledgeSpaceService.getRealKSByDomain(id);
			try {
				if (ks == null) {
					JSONObject o = knowledgeSpaceService.getMatrixForRealKS(id);
					HttpHeaders httpHeaders = new HttpHeaders();
				    httpHeaders.set("Authorization", token);
				    System.out.println(o.toJSONString());
					ResponseEntity<RelationDTO[]> message = restTemplate.postForEntity("http://localhost:5000/getRealKS",  o.toJSONString(),RelationDTO[].class);
//					ResponseEntity<RelationDTO[]> message = restTemplate.getForEntity("http://localhost:5000/getRealKS",  RelationDTO[].class);

					knowledgeSpaceService.createRealKS(message.getBody(), id);
				}
			} catch (InvalidDataException e) {
				e.printStackTrace();
				knowledgeSpaceService.createNewRealKS(id);
//				return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
			}
			
			return new ResponseEntity<>(domainService.getDomain(id), HttpStatus.OK);
		} catch (InvalidDataException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
