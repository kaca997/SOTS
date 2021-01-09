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
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "all_possible_knowledge_states")
public class AllPossibleKnowledgeStates {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "domain_id")
	private Domain domain;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "knowledge_states_id")
	private List<StateNode> states = new ArrayList<>();

	
	public AllPossibleKnowledgeStates() {
		super();
	}

	public AllPossibleKnowledgeStates(Long id, Domain domain, List<StateNode> states) {
		super();
		this.id = id;
		this.domain = domain;
		this.states = states;
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}


	public List<StateNode> getStates() {
		return states;
	}


	public void setStates(List<StateNode> states) {
		this.states = states;
	}

	@Override
	public String toString() {
		return "AllPossibleKnowledgeStates [id=" + id + ", domain=" + domain + ", states=" + states + "]";
	}
	
}
