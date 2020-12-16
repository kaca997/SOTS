package com.sots.project.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sots.project.dto.KnowledgeSpaceDTO;
import com.sots.project.dto.RelationDTO;
import com.sots.project.model.Course;
import com.sots.project.model.Domain;
import com.sots.project.model.KnowledgeSpace;
import com.sots.project.model.KnowledgeSpaceType;
import com.sots.project.model.Problem;
import com.sots.project.model.Relation;
import com.sots.project.repository.CourseRepository;
import com.sots.project.repository.DomainRepository;
import com.sots.project.repository.KnowledgeSpaceRepository;

@Service
public class KnowledgeSpaceService {

	@Autowired
	private CourseRepository courseRepository; 
	
	@Autowired
	private KnowledgeSpaceRepository knowledgeSpaceRepository; 
	
	@Autowired 
	private DomainRepository domainRepository;
	
	public KnowledgeSpace save(KnowledgeSpaceDTO domainDTO) throws InvalidDataException{
		
		Course course = courseRepository.findById(domainDTO.getCourseId()).get();
		KnowledgeSpace ksd = new KnowledgeSpace(domainDTO.getRelations());
		KnowledgeSpace saved = knowledgeSpaceRepository.save(ksd);
		//course.setKnowledgeSpace(saved);
		
		course = courseRepository.save(course);
		
		return saved;
	}

	public Object getRealKS() {
		// TODO Auto-generated method stub
		return null;
	}

	public KnowledgeSpace getRealKSByDomain(Long domainId) throws InvalidDataException {
		
		Domain d = domainRepository.findById(domainId).get();
		if (d == null) {
			throw new InvalidDataException("This domain doesn't exist.");
		}
		
		for (KnowledgeSpace ks : d.getKnowledgeSpaces()) {
			if (ks.getKnowledgeSpaceType() == KnowledgeSpaceType.REAL) {
				return ks;
			}
		}
		return null;
	}

	public KnowledgeSpace createRealKS(RelationDTO[] body, Long domainId) throws InvalidDataException{
		Domain d = domainRepository.findById(domainId).get();
		if (d == null) {
			throw new InvalidDataException("This domain doesn't exist.");
		}
		List<Problem> problems = d.getListOfProblems();
		
		if (problems.isEmpty()) {
			throw new InvalidDataException("Problems aren't defined.");
		}
		
		Set<Long> problemNumbers = new HashSet<Long>();
		
		for (RelationDTO rel : body) {
			problemNumbers.add(rel.getOrderNum1());
			problemNumbers.add(rel.getOrderNum2());
		}
		
		List<Long> listOfProblems = new ArrayList<Long>(problemNumbers);
		Collections.sort(listOfProblems);
		
		if (problems.size() != listOfProblems.size()) {
			throw new InvalidDataException("Something went wrong");
		}
		
		
		List<Relation> relations = new ArrayList<>();
		
		for (int i = 0; i < body.length; i++) {
			relations.add(new Relation());
		}
		
		for (int i = 0; i < listOfProblems.size(); i++) {
			for (int j = 0; j < body.length; j++) {
				if (body[j].getOrderNum1() == i) {
					relations.get(j).setSurmiseFrom(problems.get(i));
				} else if (body[j].getOrderNum2() == i) {
					relations.get(j).setSurmiseTo(problems.get(i));
				}
			}
		}
		
		KnowledgeSpace knowledgeSpace = new KnowledgeSpace(KnowledgeSpaceType.REAL, relations, d);
		d.getKnowledgeSpaces().add(knowledgeSpace);
		
		knowledgeSpaceRepository.save(knowledgeSpace);
		domainRepository.save(d);
		
		return knowledgeSpace;
	}

}
