package com.sots.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "relation")
public class Relation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="surmise_from_id")
	private Problem surmiseFrom;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="surmise_to_id")
	private Problem surmiseTo;

	public Relation(Problem surmiseFrom, Problem surmiseTo) {
		super();
		this.surmiseFrom = surmiseFrom;
		this.surmiseTo = surmiseTo;
	}

	public Relation() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Problem getSurmiseFrom() {
		return surmiseFrom;
	}

	public void setSurmiseFrom(Problem surmiseFrom) {
		this.surmiseFrom = surmiseFrom;
	}

	public Problem getSurmiseTo() {
		return surmiseTo;
	}

	public void setSurmiseTo(Problem surmiseTo) {
		this.surmiseTo = surmiseTo;
	}
	
}
