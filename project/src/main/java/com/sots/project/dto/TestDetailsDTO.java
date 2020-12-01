package com.sots.project.dto;

import java.util.ArrayList;
import java.util.List;

import com.sots.project.model.Section;
import com.sots.project.model.Test;

public class TestDetailsDTO {
	
	private Long testId;
	
	private Long doneTestId;
	
	private String title;
	
	private String teacherFirstName;
	
	private String teacherLastName;
	
	private String courseName;
	
	private List<SectionDTO> sections = new ArrayList<SectionDTO>();

	public TestDetailsDTO() {
		super();
	}
	
	public TestDetailsDTO(Long testId, Long doneTestId, String title, String teacherFirstName, String teacherLastName,
			String courseName, List<SectionDTO> sections) {
		super();
		this.testId = testId;
		this.doneTestId = doneTestId;
		this.title = title;
		this.teacherFirstName = teacherFirstName;
		this.teacherLastName = teacherLastName;
		this.courseName = courseName;
		this.sections = sections;
	}

	public TestDetailsDTO(Test t) {
		super();
		this.testId = t.getId();
		this.doneTestId = null;
		this.title = t.getTitle();
		this.teacherFirstName = t.getTeacher().getFirstName();
		this.teacherLastName = t.getTeacher().getLastName();
		this.courseName = t.getCourse().getName();
		for(Section s : t.getSections()) {
			this.sections.add(new SectionDTO(s));
		}
	}

	public Long getTestId() {
		return testId;
	}

	public void setTestId(Long testId) {
		this.testId = testId;
	}

	public Long getDoneTestId() {
		return doneTestId;
	}

	public void setDoneTestId(Long doneTestId) {
		this.doneTestId = doneTestId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTeacherFirstName() {
		return teacherFirstName;
	}

	public void setTeacherFirstName(String teacherFirstName) {
		this.teacherFirstName = teacherFirstName;
	}

	public String getTeacherLastName() {
		return teacherLastName;
	}

	public void setTeacherLastName(String teacherLastName) {
		this.teacherLastName = teacherLastName;
	}

	public String getCourseTitle() {
		return courseName;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseName = courseTitle;
	}

	public List<SectionDTO> getSections() {
		return sections;
	}

	public void setSections(List<SectionDTO> sections) {
		this.sections = sections;
	}

	@Override
	public String toString() {
		return "DoingTestDTO [testId=" + testId + ", doneTestId=" + doneTestId + ", title=" + title
				+ ", teacherFirstName=" + teacherFirstName + ", teacherLastName=" + teacherLastName + ", courseName="
				+ courseName + ", sections=" + sections + "]";
	}
}
