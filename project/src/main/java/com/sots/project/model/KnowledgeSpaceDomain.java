package com.sots.project.model;

import java.util.List;
import java.util.Map.Entry;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "relation")
public class KnowledgeSpaceDomain {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "domain_id")
	private List<Problem> listOfProblems;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "domain_id")
	private List<Relation> relations;

	public KnowledgeSpaceDomain(List<Problem> listOfProblems, List<Relation> relations) {
		super();
		this.listOfProblems = listOfProblems;
		this.relations = relations;
	}

	public KnowledgeSpaceDomain() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Problem> getListOfProblems() {
		return listOfProblems;
	}

	public void setListOfProblems(List<Problem> listOfProblems) {
		this.listOfProblems = listOfProblems;
	}

	public List<Relation> getRelations() {
		return relations;
	}

	public void setRelations(List<Relation> relations) {
		this.relations = relations;
	}
	
}
