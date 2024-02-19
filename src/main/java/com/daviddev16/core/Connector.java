package com.daviddev16.core;

import java.sql.Connection;

public interface Connector {
	
	Connection getConnection();

	void setConnection(Connection connection);
	
}
