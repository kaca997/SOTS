package com.sots.project.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Student extends User {
	
	private String jmbg;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "student_course", joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"))
	@JsonBackReference(value = "student-course")
	private List<Course> courses;

	public Student() {
		super();
	}

	public Student(String username, String password, String firstName, String lastName, String jmbg) {
		super(username, password, firstName, lastName);
		this.jmbg = jmbg;
	}

	public String getJmbg() {
		return jmbg;
	}

	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	
	
}
