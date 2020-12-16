package com.sots.project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sots.project.dto.DomainDTO;
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
	
	public String save(NewDomainDTO domainDTO) throws InvalidDataException {
		System.out.println(domainDTO);
		Domain domain = new Domain();
		domain.setName(domainDTO.getDomainName());
		Course c;
		try {
			c = courseRepository.findById(domainDTO.getCourseId()).get();
		}catch(Exception e){
			
			throw new InvalidDataException("Course not found!");
		}
		
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
			Problem from;
			Problem to;
			System.out.println(relation);
			try {
				from = problemRepository.findByNameAndDomain(relation.getSurmiseFrom(), domain.getId());
				to = problemRepository.findByNameAndDomain(relation.getSurmiseTo(), domain.getId());
			}catch(Exception e){
				e.printStackTrace();
				return null;
//				throw new InvalidDataException("Problem not found!");
			}
			relations.add(new Relation(from, to));
		}
		ks.setRelations(relations);
		ks = ksRepository.save(ks);
		return null;
	}
	public DomainDTO getDomain(Long id) throws InvalidDataException {
		Domain d;
		try {
			d = domainRepository.findById(id).get();
		}catch(Exception e){
			throw new InvalidDataException("Domain not found!");
		}
		DomainDTO dto = new DomainDTO();
		dto.setDomainName(d.getName());
		
		Course c = courseRepository.findCourseByDomain(d);
		dto.setCourseName(c.getName());
		for(Problem p : d.getListOfProblems()) {
			dto.getProblems().add(p.getName());
		}
	
		for(KnowledgeSpace ks : d.getKnowledgeSpaces()) {
			if(ks.getKnowledgeSpaceType() == KnowledgeSpaceType.EXPECTED) {
				for(Relation r : ks.getRelations()) {
					dto.getExpectedKnowledgeSpace()
					.add(new NewRelationDTO(r.getSurmiseFrom().getName(), r.getSurmiseTo().getName()));
				}
			}else {
				for(Relation r : ks.getRelations()) {
					dto.getRealKnowledgeSpace()
					.add(new NewRelationDTO(r.getSurmiseFrom().getName(), r.getSurmiseTo().getName()));
				}
			}
		}
		return dto;
	}
}