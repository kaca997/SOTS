package com.sots.project.dto;

public class StateNodeDTO {
	
	private String name;
	
	private int group;

	public StateNodeDTO() {
		super();
	}

	public StateNodeDTO(String name, int group) {
		super();
		this.name = name;
		this.group = group;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return "StateNodeDTO [name=" + name + ", group=" + group + "]";
	}
}
