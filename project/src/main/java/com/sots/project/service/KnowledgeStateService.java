package com.sots.project.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sots.project.dto.AllPossibleStatesDTO;
import com.sots.project.dto.StateNodeDTO;
import com.sots.project.dto.StateRelationDTO;
import com.sots.project.model.AllPossibleKnowledgeStates;
import com.sots.project.model.Domain;
import com.sots.project.model.KnowledgeSpace;
import com.sots.project.model.KnowledgeSpaceType;
import com.sots.project.model.Problem;
import com.sots.project.model.Relation;
import com.sots.project.model.StateNode;
import com.sots.project.model.Test;
import com.sots.project.repository.AllPossibleKnowledgeStatesRepository;
import com.sots.project.repository.CourseRepository;
import com.sots.project.repository.DomainRepository;
import com.sots.project.repository.KnowledgeSpaceRepository;
import com.sots.project.repository.ProblemRepository;
import com.sots.project.repository.StateNodeRepository;
import com.sots.project.repository.TestRepository;

@Service
public class KnowledgeStateService {

	@Autowired
	private StateNodeRepository stateNodeRepository; 
	
	@Autowired
	private AllPossibleKnowledgeStatesRepository knowledgeStatesRepository;
	
	@Autowired
	private DomainRepository domainRepository;
	
	@Autowired
	private KnowledgeSpaceRepository ksRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private ProblemRepository problemRepository;
	
	@Autowired
	private TestRepository testRepository;

	public AllPossibleStatesDTO generateKnowledgeStatesGraphTest(Long testID) throws InvalidDataException {
		Test t = testRepository.findById(testID).get();
		if (t == null) {
			throw new InvalidDataException("This domain doesn't exist.");
		}
//		Domain d = domainRepository.findById(domainID).get();
		Domain d = t.getCourse().getDomain();
		return this.generateKnowledgeStatesGraph(d.getId());
	}
	public AllPossibleKnowledgeStates generateAllPossibleStates(Long testID) throws InvalidDataException {
		Test t = testRepository.findById(testID).get();
		if (t == null) {
			throw new InvalidDataException("This domain doesn't exist.");
		}
//		Domain d = domainRepository.findById(domainID).get();
		Domain d = t.getCourse().getDomain();

		
		KnowledgeSpace kSpace = null; 
		
		for (KnowledgeSpace ks : d.getKnowledgeSpaces()) {
			if (ks.getKnowledgeSpaceType() == KnowledgeSpaceType.REAL) {
				kSpace = ks;
			}
		}
		
		if(kSpace == null) {
			kSpace = d.getKnowledgeSpaces().get(0);
		}
		
		List<Relation> relations = kSpace.getRelations();
		List<Problem> problems = d.getListOfProblems();
		
		AllPossibleKnowledgeStates allStates = new AllPossibleKnowledgeStates();
		allStates.setDomain(d);
		allStates.getStates().add(new StateNode());
		
		StateNode sn = new StateNode();
		sn.getProblems().addAll(problems);
		
		if(!allStates.getStates().contains(sn)) {
			allStates.getStates().add(sn);
		}
		
		for(Relation r : relations) {
			Set<Problem> problems1 = findAncestorsForProblem(r.getSurmiseFrom(), relations);
			StateNode st1 = new StateNode();
			st1.getProblems().addAll(problems1);
			st1.getProblems().add(r.getSurmiseFrom());

			if(!allStates.getStates().contains(st1)) {
				allStates.getStates().add(st1);

//				System.out.println("Parent relation"+ dto1);
				StateRelationDTO dto1 = new StateRelationDTO();
				if(problems1.size() == 0) {
					dto1.setSurmiseFrom("{}");
					dto1.setSurmiseTo("{ "+r.getSurmiseFrom().getName()+" }");
				}
			}
			StateNode st = new StateNode();
			Set<Problem> problemss = findAncestorsForProblem(r.getSurmiseTo(), relations);
			st.getProblems().addAll(problemss);
			st.getProblems().add(r.getSurmiseTo());

			if(!allStates.getStates().contains(st) && (problemss.size() != problems.size()-1) ) {
				allStates.getStates().add(st);
			}
		}
		System.out.println("All: "+ allStates);
		return allStates;
	}
	
	public AllPossibleStatesDTO generateKnowledgeStatesGraph(Long domainID) throws InvalidDataException {
		Domain d = domainRepository.findById(domainID).get();
		if (d == null) {
			throw new InvalidDataException("This domain doesn't exist.");
		}
		
		KnowledgeSpace kSpace = null; 
		
		for (KnowledgeSpace ks : d.getKnowledgeSpaces()) {
			if (ks.getKnowledgeSpaceType() == KnowledgeSpaceType.REAL) {
				kSpace = ks;
			}
		}
		
		if(kSpace == null) {
			kSpace = d.getKnowledgeSpaces().get(0);
		}
		
		List<Relation> relations = kSpace.getRelations();
		List<Problem> problems = d.getListOfProblems();
		
		AllPossibleKnowledgeStates allStates = new AllPossibleKnowledgeStates();
		AllPossibleStatesDTO allDTO = new AllPossibleStatesDTO();
		allStates.setDomain(d);
		allStates.getStates().add(new StateNode());
		allDTO.getNodes().add(new StateNodeDTO("{}", 0));
		
		StateNode sn = new StateNode();
		sn.getProblems().addAll(problems);
		
		String nodeDTOo = "";
		if(!allStates.getStates().contains(sn)) {
			allStates.getStates().add(sn);
			String nodeDTO = "{ ";
			for(Problem p : problems) {
				nodeDTO += p.getName() + ", ";
			}
			nodeDTOo = nodeDTO.substring(0, nodeDTO.length()-2);
			nodeDTOo += " }";
			allDTO.getNodes().add(new StateNodeDTO(nodeDTOo, problems.size()+1));
		}
		
		for(Relation r : relations) {
			StateRelationDTO relationDTO = new StateRelationDTO();
			Set<Problem> problems1 = findAncestorsForProblem(r.getSurmiseFrom(), relations);
			StateNode st1 = new StateNode();
			st1.getProblems().addAll(problems1);
			st1.getProblems().add(r.getSurmiseFrom());
			String nodeDTO = "{ ";
			for(Problem p : problems1) {
				nodeDTO += p.getName() + ", ";
			}
			nodeDTO += r.getSurmiseFrom().getName()+ " }";
			relationDTO.setSurmiseFrom(nodeDTO);
			if(!allStates.getStates().contains(st1)) {
				allStates.getStates().add(st1);
				allDTO.getNodes().add(new StateNodeDTO(nodeDTO, problems1.size()+1));

//				System.out.println("Parent relation"+ dto1);
				StateRelationDTO dto1 = new StateRelationDTO();
				if(problems1.size() == 0) {
					dto1.setSurmiseFrom("{}");
					dto1.setSurmiseTo("{ "+r.getSurmiseFrom().getName()+" }");
					allDTO.getRelations().add(dto1);
				}
			}
			StateNode st = new StateNode();
			Set<Problem> problemss = findAncestorsForProblem(r.getSurmiseTo(), relations);
			Set<Problem> children1 = findDescendant(r.getSurmiseTo(), relations);
			st.getProblems().addAll(problemss);
			st.getProblems().add(r.getSurmiseTo());
			String nodeDTO2 = "{ ";
			for(Problem p : problemss) {
				nodeDTO2 += p.getName() + ", ";
			}
			nodeDTO2 += r.getSurmiseTo().getName()+ " }";

			if(problemss.size() == problems.size()-1) {
				relationDTO.setSurmiseTo(nodeDTOo);
			}
			else {
				relationDTO.setSurmiseTo(nodeDTO2);
			}
			if(!allStates.getStates().contains(st) && (problemss.size() != problems.size()-1) ) {
				allStates.getStates().add(st);
				allDTO.getNodes().add(new StateNodeDTO(nodeDTO2, problemss.size()+1));
				StateRelationDTO dto = new StateRelationDTO();
				if(children1.size() == 0) {
					dto.setSurmiseTo(nodeDTOo);
					dto.setSurmiseFrom(nodeDTO2);
					allDTO.getRelations().add(dto);
				}
			}
			allDTO.getRelations().add(relationDTO);
		}
//		System.out.println("All: "+ allStates);
		System.out.println("All: "+ allDTO);
		return allDTO;
	}
	
	
	private Set<Problem> findAncestorsForProblem(Problem problem, List<Relation> relations) {
		System.out.println("Problem par:" + problem);
		Set<Problem> parentsFinal = new HashSet<Problem>();
		Set<Problem> problems = new HashSet<Problem>();
		problems.add(problem);
		boolean end = false;
		while(!end) {
			Set<Problem> found = findParents(problems, relations);
			if(found.size() == 0) {
				end = true;
			}
			else {
				parentsFinal.addAll(found);
				problems = found;
			}
		}
		return parentsFinal;
	}
	
	private Set<Problem> findParents(Set<Problem> problems, List<Relation> relations) {
		Set<Problem> parents = new HashSet<Problem>();
		for(Problem p : problems) {
			for(Relation r : relations) {
				if(r.getSurmiseTo().equals(p)) {
					parents.add(r.getSurmiseFrom());
				}
			}
		}
		return parents;
	}
	
	private Set<Problem> findDescendant(Problem problem, List<Relation> relations){
		System.out.println("Problem children:" + problem);
		Set<Problem> childrenFinal = new HashSet<Problem>();
		Set<Problem> problems = new HashSet<Problem>();
		problems.add(problem);
		boolean end = false;
		while(!end) {
			Set<Problem> found = findChildren(problems, relations);
			if(found.size() == 0) {
				end = true;
			}
			else {
				childrenFinal.addAll(found);
				problems = found;
			}
		}
		return childrenFinal;
	}
	
	private Set<Problem> findChildren(Set<Problem> problems, List<Relation> relations) {
		Set<Problem> children = new HashSet<Problem>();
		for(Problem p : problems) {
			for(Relation r : relations) {
				if(r.getSurmiseFrom().equals(p)) {
					children.add(r.getSurmiseTo());
				}
			}
		}
		return children;
	}
}
