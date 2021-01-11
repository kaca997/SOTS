package com.sots.project.service;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.jamesmurty.utils.XMLBuilder;
import com.sots.project.dto.AnswerDTO;
import com.sots.project.dto.CourseDTO;
import com.sots.project.dto.KnowledgeStatesProbabilityDTO;
import com.sots.project.dto.QuestionDTO;
import com.sots.project.dto.SectionDTO;
import com.sots.project.dto.StateNodeProbDTO;
import com.sots.project.dto.TestDTO;
import com.sots.project.dto.TestDetailsDTO;
import com.sots.project.dto.TestPreviewDTO;
import com.sots.project.dto.UpdateTestDTO;
import com.sots.project.model.AllPossibleKnowledgeStates;
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
import com.sots.project.model.StateNode;
import com.sots.project.model.Student;
import com.sots.project.model.Teacher;
import com.sots.project.model.Test;
import com.sots.project.repository.AnswerRepository;
import com.sots.project.repository.CourseRepository;
import com.sots.project.repository.DomainRepository;
import com.sots.project.repository.DoneTestRepository;
import com.sots.project.repository.QuestionRepository;
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
	
	@Autowired
	private DomainRepository domainRepository;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private KnowledgeStateService knowledgeStateService;
	
	
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
		Course c;
		try {
			c = courseRepository.findById(courseId).get();
		}catch(Exception e){
			throw new InvalidDataException("Course not found!");
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
		Course c;
		try {
			c = courseRepository.findById(courseId).get();
		}catch(Exception e){
			throw new InvalidDataException("Course not found!");
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
		Test t;
		try {
			t = testRepository.findById(testId).get();
		}
		catch(Exception e){
			throw new InvalidDataException("Test not found!");
		}
		
//		System.out.println("Studenti: " + t.getCourse().getStudents());
		Student s = (Student) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		
//		System.out.println("Student: " + s);
		
		Student st = (Student) userRepository.findById(s.getId()).get();
		if(!(t.getCourse().getStudents().contains(st))) {
			throw new InvalidDataException("You are not assigned to this test!");
		}
		
		List<DoneTest> done = doneTestRepository.findAllByStudent(s);
//		System.out.println("DONE: "+ done);
		for(DoneTest d : done) {
			if(d.getTest().equals(t)) {
				throw new InvalidDataException("You have already done this test!");
			}
		}
		
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
		
		List<Section> sections = t.getSections();
		List<SectionDTO> sorted = this.sortSections(sections, relations);
		
		TestDetailsDTO dto = new TestDetailsDTO(t);
		dto.setSections(sorted);
		return dto;
	}
	
	private List<SectionDTO> sortSections(List<Section>sections, List<Relation>relations){
		List<SectionDTO> sectionsDTO = new ArrayList<>();
		for(Section sec : sections) {
			float sum = 0;
			List<QuestionDTO> questionsDTO = new ArrayList<>();
			for(Question q : sec.getQuestions()) {
				int n = this.findAncestorsForProblem(q.getProblem(), relations).size();
				sum += n;
				QuestionDTO qdto = new QuestionDTO(q);
				qdto.setRang(n);
				questionsDTO.add(qdto);	
			}
			SectionDTO secDTO = new SectionDTO(sec);
			secDTO.setRang(sum/sec.getQuestions().size());
			secDTO.setQuestions(questionsDTO);
			sectionsDTO.add(secDTO);
			
		}
		List<SectionDTO> sortedSections = sectionsDTO.stream()
	            .sorted(Comparator.comparingDouble(SectionDTO::getRang))
	            .collect(Collectors.toList());
		
		for(SectionDTO sSec: sortedSections) {
			List<QuestionDTO> sortedQuestions = sSec.getQuestions().stream()
		            .sorted(Comparator.comparingInt(QuestionDTO::getRang))
		            .collect(Collectors.toList());
			sSec.setQuestions(sortedQuestions);
		}
//		System.out.println(sortedSections);
		return sortedSections;
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
	
	public TestDetailsDTO getTestTeacher(Long testId) throws InvalidDataException {
		Test t;
		try {
		 t = testRepository.findById(testId).get();
		}catch(Exception e){
			throw new InvalidDataException("Test not found!");
		}
		
//		System.out.println("Studenti: " + t.getCourse().getStudents());
		Teacher s = (Teacher) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		
//		System.out.println("Student: " + s);
		
		Teacher st = (Teacher) userRepository.findById(s.getId()).get();
		if(!(t.getCourse().getTeachers().contains(st))) {
			throw new InvalidDataException("You are not assigned to this test!");
		}
		
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
		
		List<Section> sections = t.getSections();
		List<SectionDTO> sorted = this.sortSections(sections, relations);
		
		TestDetailsDTO dto = new TestDetailsDTO(t);
		dto.setSections(sorted);
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


	public DOMSource getImsQti(Long testId) throws InvalidDataException, ParserConfigurationException, FactoryConfigurationError, TransformerException {
		Test t;
		try {
			t = testRepository.findById(testId).get();
		}
		catch(Exception e){
			throw new InvalidDataException("Test not found!");
		}
		
		XMLBuilder xmlBuilder = XMLBuilder.create("qti-assessment-test");
		xmlBuilder.a("title", t.getTitle());
		
		for (Section section : t.getSections()) {
			
			XMLBuilder element = xmlBuilder.element("qti-assessment-section");
			element.a("title", section.getSectionTitle());
			
			for (Question question : section.getQuestions()) {
				XMLBuilder qel = element.element("qti-assessment-item");
				
				XMLBuilder respDecl = qel.element("qti-response-declaration");
				respDecl.a("identifier", "RESPONSE");
				XMLBuilder corrResponse = respDecl.element("qti-correct-response");
				
				List<Answer> corrAns = getCorrectResponses(question);
				for (Answer corAnsw : corrAns) {
					XMLBuilder qtiValue = corrResponse.element("qti-value");
					qtiValue =  qtiValue.text(corAnsw.getId().toString());
				}
				
				XMLBuilder body = element.element("qti-item-body");
				XMLBuilder p = body.element("p");
				p = p.text(question.getText());
				
				XMLBuilder choices = element.element("qti-choice-interaction");
				choices.a("max-choices", String.valueOf(question.getAnswers().size()));
				choices.a("response-identifier", "RESPONSE");
				
				for (Answer answ : question.getAnswers()) {
					XMLBuilder answerElement = choices.element("qti-simple-choice");
					answerElement.a("identifier", answ.getId().toString());
					answerElement = answerElement.text(answ.getText());
				}
			}
		}
		
		DOMSource ds = new DOMSource(xmlBuilder.getDocument());
		saveXmlFile(ds, t.getTitle() + t.getId());
		return ds;
	}
	
	public List<Answer> getCorrectResponses(Question question) {
		
		List<Answer> corrAnswers = question.getAnswers()
											.stream()
											.filter(ans -> ans.isCorrect() == true)
											.collect(Collectors.toList());
		return corrAnswers;
		
	}
	
	public static String docToString(DOMSource doc) {
		try {
	        StringWriter sw = new StringWriter();
	        TransformerFactory tf = TransformerFactory.newInstance();
	        Transformer transformer = tf.newTransformer();
	        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

	        transformer.transform(doc, new StreamResult(sw));
	        return sw.toString();
	    } catch (Exception ex) {
	        throw new RuntimeException("Error converting to String", ex);
	    }
	}
	
	public static void saveXmlFile(DOMSource doc, String name) throws TransformerException{
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
		    Transformer transformer = transformerFactory.newTransformer();
		    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		    StreamResult result = new StreamResult(new File("../" + name + ".xml"));
		    transformer.transform(doc, result);
		    System.out.println("File saved!");
		  } catch (TransformerException tfe) {
		    tfe.printStackTrace();
		} 
	}


	public KnowledgeStatesProbabilityDTO getTestForDrivenTesting(Long testId) throws InvalidDataException{
		
		KnowledgeStatesProbabilityDTO kspDTO = new KnowledgeStatesProbabilityDTO();
		
		Test t;
		try {
		 t = testRepository.findById(testId).get();
		}catch(Exception e){
			throw new InvalidDataException("Test not found!");
		}
		
		Domain d = domainRepository.findById(t.getCourse().getDomain().getId()).get();
		if (d == null) {
			throw new InvalidDataException("This domain doesn't exist.");
		}
		List<Problem> problems = d.getListOfProblems();
		kspDTO.setAllProblems(problems);

		AllPossibleKnowledgeStates all = knowledgeStateService.generateAllPossibleStates(t.getId());
		
		kspDTO.setStates(fromSNtoDTO(all));
		
		kspDTO = setProbabilitiesOfKnowledgeStates(kspDTO, t);
		return kspDTO;
	}
	
	public List<StateNodeProbDTO> fromSNtoDTO(AllPossibleKnowledgeStates allKS) {
		
		List<StateNodeProbDTO> nodes = new ArrayList<>();
		for (StateNode sn: allKS.getStates()) {
			List<Problem> ps = new ArrayList<Problem>();
			ps.addAll(sn.getProblems());
			nodes.add(new StateNodeProbDTO(ps, 0.0));
		}
		return nodes;
	}
	
	public KnowledgeStatesProbabilityDTO setProbabilitiesOfKnowledgeStates(KnowledgeStatesProbabilityDTO knowledgeStates, Test t) throws InvalidDataException {
		
		List<DoneTest> doneTests = doneTestRepository.findAllByTest(t);
		if (doneTests.isEmpty()) {
			throw new InvalidDataException("There are no done tests.");
		}
		List<List<Problem>> listpp = new ArrayList<>();

		
			
			for (Problem p : knowledgeStates.getAllProblems()) {
				List<Problem> pp = new ArrayList<>();
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
				for (DoneTest doneTest : doneTests) {
					List<ChosenAnswer> answers = doneTest.getChosenAnswers().stream()
						      .filter(el -> qu.getAnswers().contains(el.getAnswer()))
						      .collect(Collectors.toList());
//					chosenAnswers.addAll(answers);
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
					if (answInt == 1) {
						pp.add(p);
					} else {
						pp.add(null);
					}
				
				}
				
				listpp.add(pp);
			}
			
			for (int i = 0; i < knowledgeStates.getStates().size(); i++) {
				int count = 0;
				int countP = 0;
				boolean flag = true;
				for (int j = 0; j < listpp.get(0).size(); j++) {
					countP = 0;
					for (List<Problem> list : listpp) {
						
						if (knowledgeStates.getStates().get(i).getProblems().isEmpty()) {
							if (list.get(j) != null) {
								flag = false;
							}
						}
						else {
							if (knowledgeStates.getStates().get(i).getProblems().contains(list.get(j))) {
								countP += 1;
							} else if (list.get(j) != null && !knowledgeStates.getStates().get(i).getProblems().contains(list.get(j)) ) {
								flag = false;
							}
						}
						
					}
					if (flag) {
						if (countP == knowledgeStates.getStates().get(i).getProblems().size()) {
							count +=1;
						}
					}
					
				}
				
				knowledgeStates.getStates().get(i).setProbability(count);
		}
		
		fromCountToPercentage(knowledgeStates);
		chooseQuestion(knowledgeStates, t);
		return knowledgeStates;
	}
	
	public void fromCountToPercentage(KnowledgeStatesProbabilityDTO dto) {
		int sum = dto.getStates().stream().filter(o -> o.getProbability() > 0).mapToInt(o -> (int) o.getProbability()).sum();
		for (StateNodeProbDTO state : dto.getStates()) {
			state.setProbability(state.getProbability()/sum);
		}

	}
	
	public void chooseQuestion(KnowledgeStatesProbabilityDTO dto, Test t) {
		
		List<Problem> passedProblems = new ArrayList<>();
		for (QuestionDTO question : dto.getQuestions()) {
			Question q = questionRepository.findById(question.getId()).get();
			passedProblems.add(q.getProblem());
		}
		Problem chosenProblem = null; 
		double maxSum = 0;
		
		for (Problem p : dto.getAllProblems()) {
			if (!containsId(passedProblems, p.getId())) {
				double sum = dto.getStates().stream().filter(o -> containsId(o.getProblems(), p.getId())).mapToDouble(o -> o.getProbability()).sum();
				if (sum > maxSum) {
					maxSum = sum;
					chosenProblem = p;
				}
				
			}
		}
		
		System.out.println("max sum " + maxSum);
		Question qToAsk = null;
		final Problem pp = chosenProblem;
		System.out.println(pp.getName());
		for (Section sec : t.getSections()) {
			List<Question> ques = sec.getQuestions().stream().filter(q -> q.getProblem().getId() == pp.getId()).collect(Collectors.toList());
			if (!ques.isEmpty()) {
				qToAsk = ques.get(0);
				break;
			}
		}
		
		QuestionDTO questionDTO = new QuestionDTO(qToAsk);
		dto.setQuestionToAsk(questionDTO);
	}
	
	public KnowledgeStatesProbabilityDTO updateProbabilities(KnowledgeStatesProbabilityDTO dto, Long testId) throws InvalidDataException {
		
		if (dto.getQuestions().isEmpty()) {
			throw new InvalidDataException("There are no answers.");
		}
		boolean answCorr = checkIfAnswerIsCorrect(dto.getQuestions().get(dto.getQuestions().size()-1));
		double teta = 0;
		Question qu = questionRepository.findById(dto.getQuestions().get(dto.getQuestions().size()-1).getId()).get();
		Problem p = null;
		if (qu != null) {
			System.out.println("text problem: " + qu.getProblem().getName());
			p = qu.getProblem();
		} else {
			throw new InvalidDataException("Something went wrong.");
		}
		final Problem pr = p;
		if (answCorr) {
			teta = dto.getStates().stream().filter(o -> containsId(o.getProblems(), pr.getId())).mapToDouble(o -> o.getProbability()).sum();
		} else {
			teta = dto.getStates().stream().filter(o -> !containsId(o.getProblems(), pr.getId())).mapToDouble(o -> o.getProbability()).sum();
		}
		double lnkq = dto.getStates().stream().filter(o -> containsId(o.getProblems(), pr.getId())).mapToDouble(o -> o.getProbability()).sum();
		double lnkqnot = dto.getStates().stream().filter(o -> !containsId(o.getProblems(), pr.getId())).mapToDouble(o -> o.getProbability()).sum();
		double val = 0;
		if (answCorr) {
			val = 1;
		} else {
			val = 0;
		}
		for (StateNodeProbDTO state : dto.getStates()) {
			if (containsId(state.getProblems(), p.getId())) {
				double value = (1-teta)*state.getProbability() + teta*val*(state.getProbability()/lnkq);
				state.setProbability(value);
			} else {
				double value = (1-teta)*state.getProbability() + teta*(1-val)*(state.getProbability()/lnkqnot);
				if (Double.isNaN(value)) {
					state.setProbability(0.0);
				} else {
					state.setProbability(value);
				}
			}
		}
		
		double sum = dto.getStates().stream().filter(o -> o.getProbability() > 0).mapToDouble(o -> o.getProbability()).sum();
		for (StateNodeProbDTO state : dto.getStates()) {
			state.setProbability(state.getProbability()/sum);
		}
		dto.getStates().stream().map(o -> new StateNodeProbDTO(o.getProblems(), o.getProbability()/sum));
		dto.setFinalState(checkState(dto));
		
		Test t;
		try {
		 t = testRepository.findById(testId).get();
		}catch(Exception e){
			throw new InvalidDataException("Test not found!");
		}
		if (dto.getFinalState() == null) {
			chooseQuestion(dto, t);
		} else {
			saveDoneTest(dto, t);
		}
		return dto;
	}
	
	private void saveDoneTest(KnowledgeStatesProbabilityDTO dto, Test t) throws InvalidDataException{
		
		DoneTest done = new DoneTest();
		done.setTest(t);
		Long studentId = ((Student) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
		Student s = (Student) userRepository.findById(studentId).get();
		done.setStudent(s);
		for(QuestionDTO question : dto.getQuestions()) {
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
		
		for (Section sec : t.getSections()) {
			for (Question q : sec.getQuestions()) {
				if (!containsQuestion(q, dto.getFinalState().getProblems()) && !containsIdQuestion(dto.getQuestions(), q.getId())){
					for (Answer ans : q.getAnswers()) {
						if (!ans.isCorrect()) {
							Answer a = answerRepository.getOne(ans.getId());
							if (a == null) {
								throw new InvalidDataException("Answer not found!");
							}
							done.getChosenAnswers().add(new ChosenAnswer(a));
						}
					}
				}
			}
		}
		doneTestRepository.save(done);
	}


	public boolean checkIfAnswerIsCorrect(QuestionDTO questionDTO) {
		boolean correct = false;
		for (AnswerDTO answer : questionDTO.getAnswers()) {
			if (answer.isCorrect() && !answer.isChosen()) {
				correct = false;
				return correct;
			} else if (!answer.isCorrect() && answer.isChosen()) {
				correct = false;
				return correct;
			} else {
				correct = true;
			}
		}
		return correct;
	}
	
	public static boolean containsId(List<Problem> list, Long id) {
	    for (Problem problem : list) {
	        if (problem.getId() == id) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public static boolean containsIdQuestion(List<QuestionDTO> list, Long id) {
	    for (QuestionDTO problem : list) {
	        if (problem.getId() == id) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public static boolean containsQuestion(Question q, List<Problem> problems) {
        for (Problem problem : problems) {
			if (q.getProblem().getId() == problem.getId()) {
				return true;
			}
	    }
	    return false;
	}
	
	public StateNodeProbDTO checkState(KnowledgeStatesProbabilityDTO dto) {
		boolean flag = false;
		if (dto.getAllProblems().size() == dto.getQuestions().size()) {
			flag = true;
		}
		
		if (dto.getStates().isEmpty()) {
			return null;
		}
		if (flag) {
			StateNodeProbDTO state = dto.getStates().stream().max(Comparator.comparingDouble(StateNodeProbDTO :: getProbability)).get();
			return state;
		} else {
			return twoLargest(dto.getStates());
		}
		
	}
	
	public StateNodeProbDTO twoLargest(List<StateNodeProbDTO> states){

	    double largestA = Double.MIN_VALUE, largestB = Double.MIN_VALUE;
	    StateNodeProbDTO state = null;
	    
	    for (StateNodeProbDTO stateNodeDTO : states) {
			if (stateNodeDTO.getProbability() > largestA) {
				largestB = largestA;
				largestA = stateNodeDTO.getProbability();
				state = stateNodeDTO;
			} else if (stateNodeDTO.getProbability() > largestB) {
				largestB = stateNodeDTO.getProbability();
			}
		}
	    
	    if ((largestA-largestB) > largestA/2) {
	    	return state;
	    } else {
	    	return null;
	    }
	}
}
