package com.sots.project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sots.project.dto.AnswerDTO;
import com.sots.project.dto.QuestionDTO;
import com.sots.project.dto.SectionDTO;
import com.sots.project.dto.TestDetailsDTO;
import com.sots.project.dto.TestPreviewDTO;
import com.sots.project.dto.UpdateTestDTO;
import com.sots.project.model.Answer;
import com.sots.project.model.Course;
import com.sots.project.model.DoneTest;
import com.sots.project.dto.CourseDTO;
import com.sots.project.dto.TestDTO;
import com.sots.project.dto.UpdateTestDTO;
import com.sots.project.model.Answer;
import com.sots.project.model.Course;
import com.sots.project.model.Question;
import com.sots.project.model.Section;
import com.sots.project.model.Student;
import com.sots.project.model.Teacher;
import com.sots.project.model.Test;
import com.sots.project.repository.AnswerRepository;
import com.sots.project.repository.CourseRepository;
import com.sots.project.repository.DoneTestRepository;
import com.sots.project.repository.CourseRepository;

import com.sots.project.repository.TestRepository;
import com.sots.project.repository.UserRepository;

@Service
public class TestService {
	
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
	
	
	public Test save(TestDTO testDTO) throws InvalidDataException{
	
		for (Section section: testDTO.getSections()) {
			
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
		Course c = courseRepository.findById(testDTO.getCourseId()).get();
		
		if(c == null) {
			throw new InvalidDataException("This course does not exist!");
		}
		Test t = new Test(testDTO.getTestTitle(), true, testDTO.getSections(), teacher, c);
		t = testRepository.save(t);
		
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
	
	public List<TestPreviewDTO> getCourseTestsToDo(Long courseId) throws InvalidDataException {
		Course c = courseRepository.findById(courseId).get();
		if(c==null) {
			throw new InvalidDataException("This course does not exist!");
		}
		System.out.println(c.getTests().size() + " ---------: "+ c.getTests());
		Long studentId = ((Student) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
		Student s = (Student) userRepository.findById(studentId).get();
		if(!c.getStudents().contains(s)) {
			throw new InvalidDataException("You are not assigned to this course!");
		}
		List<DoneTest> done = doneTestRepository.findAllByStudent(s);
		System.out.println(done.size() + "------------DONE: "+ done);
		List<Test> toDo = new ArrayList<Test>();
		for(Test t : c.getTests()) {
			boolean doneT = false;
			for(DoneTest d : done) {
				if(d.getTest().equals(t)) {
					doneT = true;
				}
			}
			if(!doneT) {
				toDo.add(t);
			}
		}
		List<TestPreviewDTO> lista = new ArrayList<TestPreviewDTO>();
		for(Test t : toDo) {
			lista.add(new TestPreviewDTO(t));
		}
		return lista;
	}
	
	public List<TestPreviewDTO> getTeacherCourseTest(Long courseId) throws InvalidDataException {
		Course c = courseRepository.findById(courseId).get();
		if(c==null) {
			throw new InvalidDataException("This course does not exist!");
		}
		System.out.println(c.getTests().size() + " ---------: "+ c.getTests());
		Long teacherId = ((Teacher) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
		Teacher teacher = (Teacher) userRepository.findById(teacherId).get();
		if(!c.getTeachers().contains(teacher)) {
			throw new InvalidDataException("You are not assigned to this course!");
		}
		List<Test> tTest = new ArrayList<Test>();
		for(Test test : c.getTests()) {
			if(test.getTeacher().equals(teacher)) {
				tTest.add(test);
			}
		}
		List<TestPreviewDTO> lista = new ArrayList<TestPreviewDTO>();
		for(Test t : tTest) {
			lista.add(new TestPreviewDTO(t));
		}
		return lista;
	}
	
	public TestDetailsDTO getTest(Long testId) throws InvalidDataException {
		Test t = testRepository.findById(testId).get();
		if(t==null) {
			throw new InvalidDataException("Test not found!");
		}
		
//		System.out.println("Studenti: " + t.getCourse().getStudents());
		Student s = (Student) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		
//		System.out.println("Student: " + s);
		
		Student st = (Student) userRepository.findById(s.getId()).get();
		if(!(t.getCourse().getStudents().contains(st))) {
			throw new InvalidDataException("You can't do this test!");
		}
		
		List<DoneTest> done = doneTestRepository.findAllByStudent(s);
//		System.out.println("DONE: "+ done);
		for(DoneTest d : done) {
			if(d.getTest().equals(t)) {
				throw new InvalidDataException("You have already done this test!");
			}
		}
		TestDetailsDTO dto = new TestDetailsDTO(t);
		return dto;
	}
	
	public TestDetailsDTO getTestTeacher(Long testId) throws InvalidDataException {
		Test t = testRepository.findById(testId).get();
		if(t==null) {
			throw new InvalidDataException("Test not found!");
		}
		
//		System.out.println("Studenti: " + t.getCourse().getStudents());
		Teacher s = (Teacher) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		
//		System.out.println("Student: " + s);
		
		Teacher st = (Teacher) userRepository.findById(s.getId()).get();
		if(!(t.getCourse().getTeachers().contains(st))) {
			throw new InvalidDataException("You are not assigned to this test");
		}
		
		TestDetailsDTO dto = new TestDetailsDTO(t);
		return dto;
	}

	public List<CourseDTO> getCoursesByTeacher() throws InvalidDataException{
		Teacher teacher = (Teacher) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (teacher == null) {
			throw new InvalidDataException("You are not logged in.");
		}
		List<CourseDTO> courses = new ArrayList<>();
		
		for (Course c : teacher.getCourses()) {
			courses.add(new CourseDTO(c.getId(), c.getName()));
		}
		return courses;
		
	}
}
