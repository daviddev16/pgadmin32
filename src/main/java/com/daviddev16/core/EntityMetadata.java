package com.daviddev16.core;

import lombok.Builder;

@Builder
public class EntityMetadata {

	public static final String DUMMY = "dummy";
	
	private Integer id;
	private String relationName;
	private String relationNamespace;
	private Integer parentRelationId;

	public EntityMetadata() {}
	
	public EntityMetadata(Integer id, String relationName, 
						  String relationNamespace, Integer parentRelationId) 
	{
		this.id = id;
		this.relationName = relationName;
		this.relationNamespace = relationNamespace;
		this.parentRelationId = parentRelationId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRelationName() {
		return relationName;
	}

	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}

	public String getRelationNamespace() {
		return relationNamespace;
	}

	public void setRelationNamespace(String relationNamespace) {
		this.relationNamespace = relationNamespace;
	}

	public Integer getParentRelationId() {
		return parentRelationId;
	}

	public void setParentRelationId(Integer parentRelationId) {
		this.parentRelationId = parentRelationId;
	}
	
}
