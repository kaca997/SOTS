package com.sots.project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sots.project.model.Answer;
import com.sots.project.model.Question;
import com.sots.project.model.Teacher;
import com.sots.project.model.Test;
import com.sots.project.repository.TestRepository;

@Service
public class TestService {
	
	@Autowired
	private TestRepository testRepository;

	public Test save(Test test) throws InvalidDataException{
		
		if (test.getQuestions().size() == 0) {
			throw new InvalidDataException("There are no questions in the test. Please add them.");
		}
		
		for (Question q: test.getQuestions()) {
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
		
		Teacher teacher = (Teacher) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		test.setTeacher(teacher);
		Test t = testRepository.save(test);
		
		return t;
	}

	public List<Test> getAll() {
		
		List<Test> allTests = new ArrayList<>();
		allTests = testRepository.findAll();
		return allTests;
	}

}
