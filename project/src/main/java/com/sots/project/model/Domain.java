package com.sots.project.model;

import java.util.ArrayList;
import java.util.List;

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

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "domain")
public class Domain {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "domain")
	@JsonBackReference(value="domain-ks")
	private List<KnowledgeSpace> knowledgeSpaces;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "domain_id")
	private List<Problem> listOfProblems = new ArrayList<>();

	public Domain() {
		super();
	}
	
	public Domain(Long id, String name, List<KnowledgeSpace> knowledgeSpaces) {
		super();
		this.id = id;
		this.name = name;
		this.knowledgeSpaces = knowledgeSpaces;
	}

	public Domain(String name, List<KnowledgeSpace> knowledgeSpaces, List<Problem> listOfProblems) {
		super();
		this.name = name;
		this.knowledgeSpaces = knowledgeSpaces;
		this.listOfProblems = listOfProblems;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<KnowledgeSpace> getKnowledgeSpaces() {
		return knowledgeSpaces;
	}

	public void setKnowledgeSpaces(List<KnowledgeSpace> knowledgeSpaces) {
		this.knowledgeSpaces = knowledgeSpaces;
	}

	@Override
	public String toString() {
		return "Domain [id=" + id + ", name=" + name + ", knowledgeSpaces=" + knowledgeSpaces + ", listOfProblems" + listOfProblems + "]";
	}

	public List<Problem> getListOfProblems() {
		return listOfProblems;
	}

	public void setListOfProblems(List<Problem> listOfProblems) {
		this.listOfProblems = listOfProblems;
	}
	
}
