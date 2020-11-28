package com.sots.project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sots.project.dto.UpdateTestDTO;
import com.sots.project.model.Answer;
import com.sots.project.model.Question;
import com.sots.project.model.Section;
import com.sots.project.model.Teacher;
import com.sots.project.model.Test;
import com.sots.project.repository.TestRepository;

@Service
public class TestService {
	
	@Autowired
	private TestRepository testRepository;

	public Test save(Test test) throws InvalidDataException{
		
		for (Section section: test.getSections()) {
			
			if (section.getQuestions().size() == 0) {
				throw new InvalidDataException("There are no questions in the section. Please add them.");
			}
			
			for (Question q: section.getQuestions()) {
				if (q.getText().equals("")) {
					throw new InvalidDataException("There mustn't be questions that do not have text.");
				}
				if (q.getAnswers().size() == 0) {
					throw new InvalidDataException("There mustn't be questions that do not have any answers offered.");
				}
				boolean corrAnsw = false;
				for (Answer a : q.getAnswers()) {
					if (a.isCorrect()) {
						corrAnsw = true;
						break;
					}
				}
				if (!corrAnsw) {
					throw new InvalidDataException("There mustn't be questions that do not have correct answer.");
				}
			}
		}
		
		Teacher teacher = (Teacher) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		test.setTeacher(teacher);
		Test t = testRepository.save(test);
		
		return t;
	}

	
	public Test updateTest(UpdateTestDTO dto) throws InvalidDataException{
		
		if (dto.getQuestions().size() == 0) {
			throw new InvalidDataException("There are no questions in the test. Please add them.");
		}
		
		for (Question q: dto.getQuestions()) {
			if (q.getText().equals("")) {
				throw new InvalidDataException("There mustn't be questions that do not have text.");
			}
			if (q.getAnswers().size() == 0) {
				throw new InvalidDataException("There mustn't be questions that do not have any answers offered.");
			}
			boolean corrAnsw = false;
			for (Answer a : q.getAnswers()) {
				if (a.isCorrect()) {
					corrAnsw = true;
					break;
				}
			}
			if (!corrAnsw) {
				throw new InvalidDataException("There mustn't be questions that do not have correct answer.");
			}
		}

		Test t = testRepository.findById(dto.getId()).get();
		
		if(t == null) {
			throw new InvalidDataException("This test does not exist!");
		}
		
		//t.setQuestions(dto.getQuestions());
		Test updated = testRepository.save(t);
		return updated;
	}

	public void deleteTest(Long id){
		testRepository.deleteById(id);
	}
	
	public List<Test> getAll() {
		
		List<Test> allTests = new ArrayList<>();
		allTests = testRepository.findAll();
		return allTests;
	}

}
