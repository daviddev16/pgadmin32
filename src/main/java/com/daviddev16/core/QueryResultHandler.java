package com.daviddev16.core;

import java.sql.ResultSet;

public interface QueryResultHandler {

	void handle(final ResultSet resultSet);
	
}
