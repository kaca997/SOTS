package com.sots.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sots.project.dto.KnowledgeSpaceDomainDTO;
import com.sots.project.model.Course;
import com.sots.project.model.KnowledgeSpaceDomain;
import com.sots.project.repository.CourseRepository;
import com.sots.project.repository.KnowledgeSpaceRepository;

@Service
public class KnowledgeSpaceService {

	@Autowired
	private CourseRepository courseRepository; 
	
	@Autowired
	private KnowledgeSpaceRepository knowledgeSpaceRepository; 
	
	public KnowledgeSpaceDomain save(KnowledgeSpaceDomainDTO domainDTO) throws InvalidDataException{
		
		Course course = courseRepository.findById(domainDTO.getCourseId()).get();
		KnowledgeSpaceDomain ksd = new KnowledgeSpaceDomain(domainDTO.getProblems(), domainDTO.getRelations());
		KnowledgeSpaceDomain saved = knowledgeSpaceRepository.save(ksd);
		course.setKnowledgeSpaceDomain(saved);
		
		course = courseRepository.save(course);
		
		return saved;
	}

}
