package com.sots.project.dto;

import java.util.ArrayList;
import java.util.List;

import com.sots.project.model.Problem;

public class KnowledgeStatesProbabilityDTO {
		
	private List<StateNodeProbDTO> states = new ArrayList<>();
	private List<QuestionDTO>  questions = new ArrayList<>();
	private List<Problem> allProblems;
	private QuestionDTO questionToAsk;
	private StateNodeProbDTO finalState = null;
	
	public KnowledgeStatesProbabilityDTO() {
		super();
	}
	
	public KnowledgeStatesProbabilityDTO(List<StateNodeProbDTO> states, List<QuestionDTO> questions,
			List<Problem> allProblems, QuestionDTO questionToAsk, StateNodeProbDTO finalState) {
		super();
		this.states = states;
		this.questions = questions;
		this.allProblems = allProblems;
		this.questionToAsk = questionToAsk;
		this.finalState = finalState;
	}

	public List<StateNodeProbDTO> getStates() {
		return states;
	}
	public void setStates(List<StateNodeProbDTO> states) {
		this.states = states;
	}
	public List<QuestionDTO> getQuestions() {
		return questions;
	}
	public void setQuestions(List<QuestionDTO> questions) {
		this.questions = questions;
	}

	public List<Problem> getAllProblems() {
		return allProblems;
	}

	public void setAllProblems(List<Problem> allProblems) {
		this.allProblems = allProblems;
	}

	public QuestionDTO getQuestionToAsk() {
		return questionToAsk;
	}

	public void setQuestionToAsk(QuestionDTO questionToAsk) {
		this.questionToAsk = questionToAsk;
	}

	public StateNodeProbDTO getFinalState() {
		return finalState;
	}

	public void setFinalState(StateNodeProbDTO finalState) {
		this.finalState = finalState;
	}
	
}
