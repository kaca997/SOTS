package com.sots.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sots.project.dto.KnowledgeSpaceDTO;
import com.sots.project.model.Course;
import com.sots.project.model.KnowledgeSpace;
import com.sots.project.repository.CourseRepository;
import com.sots.project.repository.KnowledgeSpaceRepository;

@Service
public class KnowledgeSpaceService {

	@Autowired
	private CourseRepository courseRepository; 
	
	@Autowired
	private KnowledgeSpaceRepository knowledgeSpaceRepository; 
	
	public KnowledgeSpace save(KnowledgeSpaceDTO domainDTO) throws InvalidDataException{
		
		Course course = courseRepository.findById(domainDTO.getCourseId()).get();
		KnowledgeSpace ksd = new KnowledgeSpace(domainDTO.getProblems(), domainDTO.getRelations());
		KnowledgeSpace saved = knowledgeSpaceRepository.save(ksd);
		//course.setKnowledgeSpace(saved);
		
		course = courseRepository.save(course);
		
		return saved;
	}

}
