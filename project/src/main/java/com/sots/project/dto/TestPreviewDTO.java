package com.sots.project.dto;

import com.sots.project.model.DoneTest;
import com.sots.project.model.Test;

public class TestPreviewDTO {
	private Long testId;
	private Long doneTestId;
	private String courseName;
	private String title;
	private String teacherFirstName;
	private String teacherLastName;
	
	public TestPreviewDTO() {
		super();
	}

	public TestPreviewDTO(Test t) {
		super();
		this.testId = t.getId();
		this.doneTestId = null;
		this.courseName = t.getCourse().getName();
		this.title = t.getTitle();
		this.teacherFirstName = t.getTeacher().getFirstName();
		this.teacherLastName = t.getTeacher().getLastName();
	}

	public TestPreviewDTO(DoneTest dt) {
		super();
		this.testId = dt.getTest().getId();
		this.doneTestId = dt.getId();
		this.courseName = dt.getTest().getCourse().getName();
		this.title = dt.getTest().getTitle();
		this.teacherFirstName = dt.getTest().getTeacher().getFirstName();
		this.teacherLastName = dt.getTest().getTeacher().getLastName();
	}

	
	public TestPreviewDTO(Long testId, Long doneTestId, String courseName, String title, String teacherFirstName,
			String teacherLastName) {
		super();
		this.testId = testId;
		this.doneTestId = doneTestId;
		this.courseName = courseName;
		this.title = title;
		this.teacherFirstName = teacherFirstName;
		this.teacherLastName = teacherLastName;
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

	@Override
	public String toString() {
		return "TestPreviewDTO [testId=" + testId + ", doneTestId=" + doneTestId + ", courseName=" + courseName
				+ ", title=" + title + ", teacherFirstName=" + teacherFirstName + ", teacherLastName=" + teacherLastName
				+ "]";
	}
	
}
