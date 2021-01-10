package com.sots.project.dto;

import java.util.List;

import com.sots.project.model.Problem;

public class StateNodeProbDTO {
	
	private List<Problem> problems;
	private double probability;
	
	public StateNodeProbDTO() {
		super();
	}
	public StateNodeProbDTO(List<Problem> problems, double probability) {
		super();
		this.problems = problems;
		this.probability = probability;
	}
	public List<Problem> getProblems() {
		return problems;
	}
	public void setProblems(List<Problem> problems) {
		this.problems = problems;
	}
	public double getProbability() {
		return probability;
	}
	public void setProbability(double probability) {
		this.probability = probability;
	}
	
}
