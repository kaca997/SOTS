package com.sots.project.dto;

import java.util.ArrayList;
import java.util.List;

import com.sots.project.model.Question;
import com.sots.project.model.Section;

public class SectionDTO {
	private Long id;
	
	private String sectionTitle;
	
	private double rang;
	
	private List<QuestionDTO> questions = new ArrayList<QuestionDTO>();

	public SectionDTO() {
		super();
	}

	public SectionDTO(Long id, String sectionTitle, List<QuestionDTO> questions) {
		super();
		this.id = id;
		this.sectionTitle = sectionTitle;
		this.questions = questions;
	}
	
	public SectionDTO(Section s) {
		super();
		this.id = s.getId();
		this.sectionTitle = s.getSectionTitle();
		for(Question q : s.getQuestions()) {
			this.questions.add(new QuestionDTO(q));
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSectionTitle() {
		return sectionTitle;
	}

	public void setSectionTitle(String sectionTitle) {
		this.sectionTitle = sectionTitle;
	}

	public List<QuestionDTO> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionDTO> questions) {
		this.questions = questions;
	}

	public double getRang() {
		return rang;
	}

	public void setRang(double rang) {
		this.rang = rang;
	}

	@Override
	public String toString() {
		return "SectionDTO [id=" + id + ", sectionTitle=" + sectionTitle + ", rang=" + rang + ", questions=" + questions
				+ "]";
	}
	
}
