package com.sots.project.dto;

import java.util.List;

import com.sots.project.model.Problem;
import com.sots.project.model.Relation;

public class KnowledgeSpaceDomainDTO {
	
	private Long courseId;
	
	private List<Problem> problems;
	
	private List<Relation> relations;

	public KnowledgeSpaceDomainDTO(Long courseId, List<Problem> problems, List<Relation> relations) {
		super();
		this.courseId = courseId;
		this.problems = problems;
		this.relations = relations;
	}

	public KnowledgeSpaceDomainDTO() {
		super();
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public List<Problem> getProblems() {
		return problems;
	}

	public void setProblems(List<Problem> problems) {
		this.problems = problems;
	}

	public List<Relation> getRelations() {
		return relations;
	}

	public void setRelations(List<Relation> relations) {
		this.relations = relations;
	}
	
}
