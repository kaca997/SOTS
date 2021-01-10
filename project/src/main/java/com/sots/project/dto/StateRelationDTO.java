package com.sots.project.dto;

public class StateRelationDTO {
	private String surmiseFrom;

	private String surmiseTo;

	public StateRelationDTO() {
		super();
	}
	
	public StateRelationDTO(String surmiseFrom, String surmiseTo) {
		super();
		this.surmiseFrom = surmiseFrom;
		this.surmiseTo = surmiseTo;
	}

	public String getSurmiseFrom() {
		return surmiseFrom;
	}

	public void setSurmiseFrom(String surmiseFrom) {
		this.surmiseFrom = surmiseFrom;
	}

	public String getSurmiseTo() {
		return surmiseTo;
	}

	public void setSurmiseTo(String surmiseTo) {
		this.surmiseTo = surmiseTo;
	}

	@Override
	public String toString() {
		return "\n StateRelationDTO [surmiseFrom=" + surmiseFrom + ", surmiseTo=" + surmiseTo + "]";
	}

}
