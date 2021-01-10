package com.sots.project.dto;

import java.util.ArrayList;
import java.util.List;

import com.sots.project.model.StateNode;

public class AllPossibleStatesDTO {
	
	private List<StateNodeDTO> nodes = new ArrayList<>();
	
	private List<StateRelationDTO> relations = new ArrayList<>();

	public AllPossibleStatesDTO() {
		super();
	}

	public AllPossibleStatesDTO(List<StateNodeDTO> nodes, List<StateRelationDTO> relations) {
		super();
		this.nodes = nodes;
		this.relations = relations;
	}

	public List<StateNodeDTO> getNodes() {
		return nodes;
	}

	public void setNodes(List<StateNodeDTO> nodes) {
		this.nodes = nodes;
	}

	public List<StateRelationDTO> getRelations() {
		return relations;
	}

	public void setRelations(List<StateRelationDTO> relations) {
		this.relations = relations;
	}

	@Override
	public String toString() {
		return "AllPossibleStatesDTO [nodes=" + nodes + ", relations=" + relations + "]";
	}

}
