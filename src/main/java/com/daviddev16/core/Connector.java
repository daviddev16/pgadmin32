package com.daviddev16.core;

import java.sql.Connection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public interface Connector {
	
	Connection getConnection();

	void setConnection(Connection connection);

}
