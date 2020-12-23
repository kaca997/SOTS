package com.sots.project.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sots.project.dto.KnowledgeSpaceDTO;
import com.sots.project.dto.RelationDTO;
import com.sots.project.model.Answer;
import com.sots.project.model.ChosenAnswer;
import com.sots.project.model.Course;
import com.sots.project.model.Domain;
import com.sots.project.model.DoneTest;
import com.sots.project.model.KnowledgeSpace;
import com.sots.project.model.KnowledgeSpaceType;
import com.sots.project.model.Problem;
import com.sots.project.model.Question;
import com.sots.project.model.Relation;
import com.sots.project.model.Section;
import com.sots.project.model.Test;
import com.sots.project.repository.CourseRepository;
import com.sots.project.repository.DomainRepository;
import com.sots.project.repository.DoneTestRepository;
import com.sots.project.repository.KnowledgeSpaceRepository;
import com.sots.project.repository.TestRepository;

import net.minidev.json.JSONObject;

@Service
public class KnowledgeSpaceService {

	@Autowired
	private CourseRepository courseRepository; 
	
	@Autowired
	private KnowledgeSpaceRepository knowledgeSpaceRepository; 
	
	@Autowired 
	private DomainRepository domainRepository;
	
	@Autowired 
	private TestRepository testRepository;
	
	@Autowired
	private DoneTestRepository doneTestRepository;
	
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
	
	public KnowledgeSpace getRealKSByTest(Long testId) throws InvalidDataException {
		Test t = testRepository.findById(testId).get();
		if (t == null) {
			throw new InvalidDataException("This test doesn't exist. ");
		}
		
		return getRealKSByDomain(t.getCourse().getDomain().getId());
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
			return null;
//			throw new InvalidDataException("Something went wrong");
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
		
		
		KnowledgeSpace knowledgeSpace = getRealKSByDomain(domainId);
		if (knowledgeSpace != null) {
			knowledgeSpace.setRelations(relations);
			knowledgeSpace.setDomain(d);
			knowledgeSpaceRepository.save(knowledgeSpace);
			domainRepository.save(d);
			return knowledgeSpace;
		}
		KnowledgeSpace knowledgeSpace2 = new KnowledgeSpace(KnowledgeSpaceType.REAL, relations, d);
		d.getKnowledgeSpaces().add(knowledgeSpace2);
		
		knowledgeSpaceRepository.save(knowledgeSpace2);
		domainRepository.save(d);
		
		return knowledgeSpace;
	}
	
	public JSONObject getMatrixForRealKS(Long domainId) throws InvalidDataException {
		
		List<Test> tests = testRepository.findByDomain(domainId);
		if (tests.isEmpty()) {
			throw new InvalidDataException("There are no tests for this domain.");
		}
		
		Domain d = domainRepository.findById(domainId).get();
		List<Problem> listOfProblems = d.getListOfProblems();
		if (listOfProblems.isEmpty()) {
			throw new InvalidDataException("Problems are not defined.");
		}
		
		Test t = null;
		for (Test test : tests) {
			t = test; //uzimamo samo prvi test
			break;
		}
		
		List<DoneTest> doneTests = doneTestRepository.findAllByTest(t);
		if (doneTests.isEmpty()) {
			throw new InvalidDataException("There are no done tests.");
		}
		
		Map<String, List<Integer>> matrix = new HashMap<>();
		for (Problem p : listOfProblems) {
			Question q = null;
			for (Section s : t.getSections()) {
				List<Question> questions = s.getQuestions()
						  .stream()
						  .filter(c -> c.getProblem().getId() == p.getId())
						  .collect(Collectors.toList());
				if (!questions.isEmpty()) {
					q = questions.get(0);
					break;
				} 
			}
			final Question qu = q;
			List<ChosenAnswer> chosenAnswers = new ArrayList<>();
			List<Integer> ans = new ArrayList<>();
			for (DoneTest doneTest : doneTests) {
				List<ChosenAnswer> answers = doneTest.getChosenAnswers().stream()
					      .filter(el -> qu.getAnswers().contains(el.getAnswer()))
					      .collect(Collectors.toList());
//				chosenAnswers.addAll(answers);
				Integer answInt = 0;
				for (Answer a : q.getAnswers()) {
					boolean exist = answers.stream().anyMatch(answer -> answer.getAnswer().getId() == a.getId());
					if (exist) {
						if (a.isCorrect() == false) {
							answInt = 0;
							break;
						} else {
							answInt = 1;
						}
					} else {
						if (a.isCorrect() == false) {
							answInt = 1;
						} else {
							answInt = 0;
							break;
						}
					}
				
			}
				ans.add(answInt);
			
		}
			matrix.put(p.getName(), ans);
		}
		
		return new JSONObject(matrix);
		
	}

	public JSONObject getMatrixForRealKSTestId(Long testId) throws InvalidDataException {
		Test t = testRepository.findById(testId).get();
		if (t == null) {
			throw new InvalidDataException("This test doesn't exist. ");
		}
		
		return getMatrixForRealKS(t.getCourse().getDomain().getId());
	}

	public void createRealKSTest(RelationDTO[] body, Long testId) throws InvalidDataException {
		Test t = testRepository.findById(testId).get();
		if (t == null) {
			throw new InvalidDataException("This test doesn't exist. ");
		}
		
		createRealKS(body, t.getCourse().getDomain().getId());
		
	}

}
