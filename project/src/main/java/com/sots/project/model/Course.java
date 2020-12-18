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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "course")
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "courses")
	private List<Teacher> teachers;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "courses")
	private List<Student> students;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course")
	@JsonBackReference(value="course-test")
	private List<Test> tests;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "domain_id")
//	@JsonIgnore
	private Domain domain;

	public Course() {
		super();
	}

	public Course(Long id, String name, List<Teacher> teachers, List<Student> students, List<Test> tests) {
		super();
		this.id = id;
		this.name = name;
		this.teachers = teachers;
		this.students = students;
		this.tests = tests;
	}

	public Course(Long id, String name, List<Teacher> teachers, List<Student> students, List<Test> tests,
			Domain domain) {
		super();
		this.id = id;
		this.name = name;
		this.teachers = teachers;
		this.students = students;
		this.tests = tests;
		this.domain = domain;
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

	public List<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public List<Test> getTests() {
		return tests;
	}

	public void setTests(List<Test> tests) {
		this.tests = tests;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}
}
