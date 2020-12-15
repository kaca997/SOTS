package com.sots.project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sots.project.dto.NewDomainDTO;
import com.sots.project.dto.NewRelationDTO;
import com.sots.project.model.Course;
import com.sots.project.model.Domain;
import com.sots.project.model.KnowledgeSpace;
import com.sots.project.model.KnowledgeSpaceType;
import com.sots.project.model.Problem;
import com.sots.project.model.Relation;
import com.sots.project.repository.CourseRepository;
import com.sots.project.repository.DomainRepository;
import com.sots.project.repository.KnowledgeSpaceRepository;
import com.sots.project.repository.ProblemRepository;

@Service
public class DomainService {

	@Autowired
	private DomainRepository domainRepository;
	
	@Autowired
	private KnowledgeSpaceRepository ksRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private ProblemRepository problemRepository;
	
	public String save(NewDomainDTO domainDTO) {
		Domain domain = new Domain();
		domain.setName(domainDTO.getDomainName());
		
		Course c = courseRepository.findById(domainDTO.getCourseId()).get();
		
		
		for(String problemName : domainDTO.getProblemList()) {
			domain.getListOfProblems().add(new Problem(problemName));
		}
		domain = domainRepository.save(domain);
		c.setDomain(domain);
		courseRepository.save(c);
		
		KnowledgeSpace ks = new KnowledgeSpace();
		ks.setDomain(domain);
		ks.setKnowledgeSpaceType(KnowledgeSpaceType.EXPECTED);
		
		List<Relation> relations = new ArrayList<>();
		
		for(NewRelationDTO relation : domainDTO.getRelations()) {
			System.out.println(relation);
			Problem from = problemRepository.findByNameAndDomain(relation.getSurmiseFrom(), domain.getId());
			Problem to = problemRepository.findByNameAndDomain(relation.getSurmiseTo(), domain.getId());
			relations.add(new Relation(from, to));
		}
		ks.setRelations(relations);
		ks = ksRepository.save(ks);
		return null;
	}
}
