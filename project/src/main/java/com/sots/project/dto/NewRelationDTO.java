package com.sots.project.dto;

public class NewRelationDTO {
	
	private String surmiseFrom;

	private String surmiseTo;

	public NewRelationDTO() {
		super();
	}
	
	public NewRelationDTO(String surmiseFrom, String surmiseTo) {
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
		return "NewRelationDTO [surmiseFrom=" + surmiseFrom + ", surmiseTo=" + surmiseTo + "]";
	}

}
