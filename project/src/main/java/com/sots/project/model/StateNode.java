package com.sots.project.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	private Set<Problem> problems = new HashSet<Problem>();

	public StateNode() {
		super();
	}

	public StateNode(Long id, Set<Problem> problems) {
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

	public Set<Problem> getProblems() {
		return problems;
	}

	public void setProblems(Set<Problem> problems) {
		this.problems = problems;
	}

	@Override
	public String toString() {
		return "\n StateNode [id=" + id + ", problems=" + problems + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StateNode other = (StateNode) obj;
		if (problems == null) {
			if (other.problems != null)
				return false;
		} else if (!problems.equals(other.problems))
			return false;
		return true;
	}
	
	
}
