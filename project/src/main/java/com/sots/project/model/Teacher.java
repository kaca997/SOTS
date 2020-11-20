package com.sots.project.model;

import javax.persistence.Entity;

@Entity
public class Teacher extends User{

	public Teacher() {
		super();
	}

	public Teacher(String username, String password, String firstName, String lastName) {
		super(username, password, firstName, lastName);
	}

	
}
