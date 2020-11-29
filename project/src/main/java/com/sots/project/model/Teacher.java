package com.sots.project.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Teacher extends User{

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "teacher_course", joinColumns = @JoinColumn(name = "teacher_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"))
	@JsonBackReference(value = "teacher-course")
	private List<Course> courses;
	
	public Teacher() {
		super();
	}

	public Teacher(String username, String password, String firstName, String lastName) {
		super(username, password, firstName, lastName);
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	
	
}
