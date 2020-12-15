package com.sots.project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sots.project.dto.AnswerDTO;
import com.sots.project.dto.TestDetailsDTO;
import com.sots.project.dto.QuestionDTO;
import com.sots.project.dto.SectionDTO;
import com.sots.project.dto.TestPreviewDTO;
import com.sots.project.model.Answer;
import com.sots.project.model.ChosenAnswer;
import com.sots.project.model.Course;
import com.sots.project.model.DoneTest;
import com.sots.project.model.Student;
import com.sots.project.model.Test;
import com.sots.project.repository.AnswerRepository;
import com.sots.project.repository.CourseRepository;
import com.sots.project.repository.DoneTestRepository;
import com.sots.project.repository.TestRepository;
import com.sots.project.repository.UserRepository;

@Service
public class DoneTestService {
	@Autowired
	private TestRepository testRepository;
	
	@Autowired
	private DoneTestRepository doneTestRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AnswerRepository answerRepository;
	
	public List<TestPreviewDTO> getDoneTests(Long courseId) throws InvalidDataException {
		Course c = courseRepository.findById(courseId).get();
		if(c==null) {
			throw new InvalidDataException("This course does not exist!");
		}
		
		Long studentId = ((Student) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
		Student s = (Student) userRepository.findById(studentId).get();
		List<DoneTest> doneAll = doneTestRepository.findAllByStudent(s);
		System.out.println("DONE: "+ doneAll);
		List<DoneTest> doneCourse = new ArrayList<DoneTest>();
		for(DoneTest d : doneAll) {
			if(d.getTest().getCourse().equals(c)) {
				doneCourse.add(d);
				System.out.println("dodajem t");
			}
		}
		List<TestPreviewDTO> lista = new ArrayList<TestPreviewDTO>();
		for(DoneTest dt: doneCourse) {
			lista.add(new TestPreviewDTO(dt));
		}
		return lista;
	}
	
	public TestDetailsDTO submitTest(TestDetailsDTO dto) throws InvalidDataException {
		DoneTest done = new DoneTest();
		Test t;
		try {
			t = testRepository.findById(dto.getTestId()).get();
		}catch(Exception e){
			throw new InvalidDataException("Test not found!");
		}
		done.setTest(t);
		Long studentId = ((Student) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
		Student s = (Student) userRepository.findById(studentId).get();
		done.setStudent(s);
		for(SectionDTO section: dto.getSections()) {
			for(QuestionDTO question : section.getQuestions()) {
				for(AnswerDTO answer : question.getAnswers()) {
					if(answer.isChosen()) {
						Answer a = answerRepository.getOne(answer.getId());
						if(a == null) {
							throw new InvalidDataException("Answer not found!");
						}
						done.getChosenAnswers().add(new ChosenAnswer(a));
					}
				}
			}
		}
		DoneTest dt = doneTestRepository.save(done);
		TestDetailsDTO saved = new TestDetailsDTO(dt.getTest());
		saved.setDoneTestId(dt.getId());
		List<AnswerDTO> list = new ArrayList<>();
		
		saved.getSections().stream().forEach((x) -> 
		x.getQuestions().stream().forEach((y) ->
		y.getAnswers().stream().forEach(list::add)));
		
		for(AnswerDTO answerDTO : list) {
			for(ChosenAnswer a : dt.getChosenAnswers()) {
				if(a.getAnswer().getId()==answerDTO.getId()) {
					answerDTO.setChosen(true);
					break;
				}
			}
		}
		return saved;
	}
	
	
	public TestDetailsDTO getDoneTest(Long doneTestId) throws InvalidDataException {
		DoneTest dt;
		try {
			dt = doneTestRepository.findById(doneTestId).get();
		}catch(Exception e){
			throw new InvalidDataException("Test not found!");
		}
		
//		System.out.println("Studenti: " + t.getCourse().getStudents());
		Student s = (Student) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		
//		System.out.println("Student: " + s);
		
		Student st = (Student) userRepository.findById(s.getId()).get();
		if(!(dt.getStudent().equals(st))) {
			throw new InvalidDataException("You are not assigned to this test!");
		}
		TestDetailsDTO dto = new TestDetailsDTO(dt.getTest());
		dto.setDoneTestId(doneTestId);
		List<AnswerDTO> list = new ArrayList<>();
		
		dto.getSections().stream().forEach((x) -> 
		x.getQuestions().stream().forEach((y) ->
		y.getAnswers().stream().forEach(list::add)));
		
		for(AnswerDTO answerDTO : list) {
			for(ChosenAnswer a : dt.getChosenAnswers()) {
				if(a.getAnswer().getId()==answerDTO.getId()) {
					answerDTO.setChosen(true);
					break;
				}
			}
		}
		return dto;
	}
}
