package com.sots.project.dto;

import java.util.ArrayList;
import java.util.List;

import com.sots.project.model.Answer;
import com.sots.project.model.Question;

public class QuestionDTO {
	private Long id;

	private String text;
	
	private List<AnswerDTO> answers = new ArrayList<AnswerDTO>();

	public QuestionDTO(Long id, String text, List<AnswerDTO> answers) {
		super();
		this.id = id;
		this.text = text;
		this.answers = answers;
	}

	public QuestionDTO() {
		super();
	}

	public QuestionDTO(Question q) {
		super();
		this.id = q.getId();
		this.text = q.getText();
		for(Answer a: q.getAnswers()) {
			answers.add(new AnswerDTO(a));
		}
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<AnswerDTO> getAnswers() {
		return answers;
	}

	public void setAnswers(List<AnswerDTO> answers) {
		this.answers = answers;
	}

}
