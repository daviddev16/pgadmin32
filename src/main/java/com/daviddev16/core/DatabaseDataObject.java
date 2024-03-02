package com.daviddev16.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class DatabaseDataObject implements ResourcedEntityDataNode {
	
	@JsonIgnore private Connector connector;
	@JsonIgnore private DatabaseDataObject parent;
	@JsonIgnore private EntityMetadata entityMetadata;
	@JsonIgnore private NodeState nodeState;

	public Connector getConnector() {
		return connector;
	}
	
	public void setConnector(Connector connector) {
		this.connector = connector;
	}
	
	public DatabaseDataObject getParent() {
		return parent;
	}
	
	public void setParent(DatabaseDataObject parent) {
		this.parent = parent;
	}
	
	public EntityMetadata getPostgresObjectMetadata() {
		return entityMetadata;
	}
	
	public void setPostgresObjectMetadata(EntityMetadata entityMetadata) {
		this.entityMetadata = entityMetadata;
	}

	public NodeState getNodeState() {
		return nodeState;
	}

	public void setNodeState(NodeState nodeState) {
		this.nodeState = nodeState;
	}

}
