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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "state_node")
public class StateNode {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "state_problem", joinColumns = @JoinColumn(name = "state_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "problem_id", referencedColumnName = "id"))
	@JsonBackReference(value = "state-problem")
	private List<Problem> problems = new ArrayList<Problem>();

	public StateNode() {
		super();
	}

	public StateNode(Long id, List<Problem> problems) {
		super();
		this.id = id;
		this.problems = problems;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Problem> getProblems() {
		return problems;
	}

	public void setProblems(List<Problem> problems) {
		this.problems = problems;
	}

	@Override
	public String toString() {
		return "StateNode [id=" + id + ", problems=" + problems + "]";
	}
}
