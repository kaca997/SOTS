package com.sots.project.dto;

import com.sots.project.model.Answer;

public class AnswerDTO {
	private Long id;
	
	private String text;
	
	private boolean correct;
	
	private boolean chosen;

	public AnswerDTO(Long id, String text, boolean correct, boolean chosen) {
		super();
		this.id = id;
		this.text = text;
		this.correct = correct;
		this.chosen = chosen;
	}

	public AnswerDTO() {
		super();
	}

	
	public AnswerDTO(Answer answer) {
		this.id = answer.getId();
		this.text = answer.getText();
		this.correct = answer.isCorrect();
		this.chosen = false;
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

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	public boolean isChosen() {
		return chosen;
	}

	public void setChosen(boolean chosen) {
		this.chosen = chosen;
	}

	@Override
	public String toString() {
		return "AnswerDTO [id=" + id + ", text=" + text + ", correct=" + correct + ", chosen=" + chosen + "]";
	}
	
}
