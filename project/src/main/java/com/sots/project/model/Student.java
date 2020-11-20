package com.sots.project.model;

import javax.persistence.Entity;

@Entity
public class Student extends User {
	
	private String jmbg;

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
	
}
