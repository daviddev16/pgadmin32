package com.daviddev16.node;


import java.sql.Connection;

import com.daviddev16.core.Connector;
import com.daviddev16.core.DatabaseDataObject;
import com.daviddev16.core.JdbcArtifact;
import com.daviddev16.core.annotation.Resourced;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Resourced(resourceIdentifier = "Database16px")
public class Database extends DatabaseDataObject implements Connector {

	private String databaseName;
	private JdbcArtifact parentJdbcArtifact;

	@JsonIgnore
	private volatile Connection connection;
	
	@Override
	public synchronized Connection getConnection() {
		return connection;
	}
	
	@Override
	public synchronized void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public String getNodeName() {
		return databaseName;
	}

	@Override
	public String getNodeIdentifier() {
		return databaseName;
	}
	
	@Override
	public String toString() {
		return databaseName;
	}

}
