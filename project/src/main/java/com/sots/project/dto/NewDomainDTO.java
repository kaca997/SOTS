package com.sots.project.dto;

import java.util.List;

public class NewDomainDTO {

	private String domainName;
	
	private Long courseId;
	
	private List<String> problemList;
	
	private List<RelationDTO> relations;

	public NewDomainDTO() {
		super();
	}

	public NewDomainDTO(String domainName, Long courseId, List<String> problemList, List<RelationDTO> relations) {
		super();
		this.domainName = domainName;
		this.courseId = courseId;
		this.problemList = problemList;
		this.relations = relations;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public List<String> getProblemList() {
		return problemList;
	}

	public void setProblemList(List<String> problemList) {
		this.problemList = problemList;
	}

	public List<RelationDTO> getRelations() {
		return relations;
	}

	public void setRelations(List<RelationDTO> relations) {
		this.relations = relations;
	}

	@Override
	public String toString() {
		return "NewDomainDTO [domainName=" + domainName + ", courseId=" + courseId + ", problemList=" + problemList
				+ ", relations=" + relations + "]";
	}

}