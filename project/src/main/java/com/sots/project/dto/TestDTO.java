package com.sots.project.dto;

import java.util.List;

import com.sots.project.model.Section;

public class TestDTO {
	
	private String testTitle;
	private Long courseId;
	private List<Section> sections;
	
	public TestDTO(String testTitle, Long courseId, List<Section> sections) {
		super();
		this.testTitle = testTitle;
		this.courseId = courseId;
		this.sections = sections;
	}

	public TestDTO() {
		super();
	}

	public String getTestTitle() {
		return testTitle;
	}

	public void setTestTitle(String testTitle) {
		this.testTitle = testTitle;
	}

	public Long getCourseId() {
		return this.courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}
	
}
