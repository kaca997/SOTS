package com.sots.project.dto;

import java.util.List;

import com.sots.project.model.Question;

public class UpdateTestDTO {
	
	private Long id;
	private List<Question> questions;
	
	public UpdateTestDTO() {
		super();
	}

	public UpdateTestDTO(Long id, List<Question> questions) {
		super();
		this.id = id;
		this.questions = questions;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	
}
