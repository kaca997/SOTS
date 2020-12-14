package com.sots.project.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "knowledge_space")
public class KnowledgeSpace {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "knowledge_space_type")
	@Enumerated(EnumType.STRING)
	private KnowledgeSpaceType knowledgeSpaceType;
		
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JoinColumn(name = "knowledge_space_id")
//	private List<Problem> listOfProblems;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "knowledge_space_id")
	private List<Relation> relations;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "domain_id")
	private Domain domain;

	public KnowledgeSpace(List<Relation> relations) {
		super();
		this.relations = relations;
	}

	public KnowledgeSpace(KnowledgeSpaceType knowledgeSpaceType, List<Relation> relations, Domain domain) {
		super();
		this.knowledgeSpaceType = knowledgeSpaceType;
		this.relations = relations;
		this.domain = domain;
	}


	public KnowledgeSpace() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Relation> getRelations() {
		return relations;
	}

	public void setRelations(List<Relation> relations) {
		this.relations = relations;
	}

	public KnowledgeSpaceType getKnowledgeSpaceType() {
		return knowledgeSpaceType;
	}

	public void setKnowledgeSpaceType(KnowledgeSpaceType knowledgeSpaceType) {
		this.knowledgeSpaceType = knowledgeSpaceType;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}
	
}
