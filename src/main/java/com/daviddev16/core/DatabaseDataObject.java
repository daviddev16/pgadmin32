package com.daviddev16.core;

import com.daviddev16.core.postgres.PostgresObjectMetadata;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties
public abstract class DatabaseDataObject implements ResourcedEntityDataNode {

	private Connector connector;
	private PostgresObjectMetadata postgresObjectMetadata;
	private DatabaseDataObject parent;
	private boolean loaded = false;
	
	public void markAsLoaded() {
		setLoaded(true);
	}
	
}
