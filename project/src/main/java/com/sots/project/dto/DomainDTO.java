package com.sots.project.dto;

import java.util.ArrayList;
import java.util.List;

public class DomainDTO {
	
	private String domainName;
	
	private String courseName;
	
	private List<String> problems = new ArrayList<>();
	
	private List<NewRelationDTO> realKnowledgeSpace = new ArrayList<>();
	
	private List<NewRelationDTO> expectedKnowledgeSpace =new ArrayList<>();

	public DomainDTO() {
		super();
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public List<NewRelationDTO> getRealKnowledgeSpace() {
		return realKnowledgeSpace;
	}

	public void setRealKnowledgeSpace(List<NewRelationDTO> realKnowledgeSpace) {
		this.realKnowledgeSpace = realKnowledgeSpace;
	}

	public List<NewRelationDTO> getExpectedKnowledgeSpace() {
		return expectedKnowledgeSpace;
	}

	public void setExpectedKnowledgeSpace(List<NewRelationDTO> expectedKnowledgeSpace) {
		this.expectedKnowledgeSpace = expectedKnowledgeSpace;
	}

	public List<String> getProblems() {
		return problems;
	}

	public void setProblems(List<String> problems) {
		this.problems = problems;
	}
	
}
