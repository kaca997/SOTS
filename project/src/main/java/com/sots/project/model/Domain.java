package com.sots.project.model;

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

	public Domain() {
		super();
	}
	
	public Domain(Long id, String name, List<KnowledgeSpace> knowledgeSpaces) {
		super();
		this.id = id;
		this.name = name;
		this.knowledgeSpaces = knowledgeSpaces;
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
		return "Domain [id=" + id + ", name=" + name + ", knowledgeSpaces=" + knowledgeSpaces + "]";
	}
	
}
