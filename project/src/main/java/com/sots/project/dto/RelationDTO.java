package com.sots.project.dto;

public class RelationDTO {
	
	private Long orderNum1;
	private Long orderNum2;
	
	public RelationDTO(Long orderNum1, Long orderNum2) {
		super();
		this.orderNum1 = orderNum1;
		this.orderNum2 = orderNum2;
	}
	
	public RelationDTO() {
		super();
	}

	public Long getOrderNum1() {
		return orderNum1;
	}

	public void setOrderNum1(Long orderNum1) {
		this.orderNum1 = orderNum1;
	}

	public Long getOrderNum2() {
		return orderNum2;
	}

	public void setOrderNum2(Long orderNum2) {
		this.orderNum2 = orderNum2;
	}
	
}
