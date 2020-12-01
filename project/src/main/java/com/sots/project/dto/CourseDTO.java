package com.sots.project.dto;

public class CourseDTO {
	
	private Long id;
	
	private String name;

	public CourseDTO(Long long1, String name) {
		super();
		this.id = long1;
		this.name = name;
	}

	public CourseDTO() {
		super();
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
	
}
