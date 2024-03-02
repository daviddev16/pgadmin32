package com.daviddev16.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.daviddev16.component.dialog.DlgErrorDetails;
import com.daviddev16.core.Connector;
import com.daviddev16.core.JdbcArtifact;

public class ConnectionManager implements Runnable {

	private final Map<String, Connection> connections;

	public ConnectionManager() {
		connections = Collections.synchronizedMap(new HashMap<String, Connection>());
		Runtime.getRuntime().addShutdownHook(new Thread(this));
	}

	public Connection createConnection(String nodeIdentifier, JdbcArtifact jdbcArtifact, String connectionDatabase) {
		try {
			Class.forName("org.postgresql.Driver");
			String jdbcUsername = jdbcArtifact.getJdbcUsername();
			String jdbcPassword = jdbcArtifact.getJdbcPassword();
			String jdbcString = JdbcArtifact.toJdbcString(jdbcArtifact, connectionDatabase);
			Connection connection = DriverManager.getConnection(jdbcString, jdbcUsername, jdbcPassword);
			synchronized (connection) {
				connections.put(nodeIdentifier, connection);
				return connection;
			}
		} catch (ClassNotFoundException | SQLException e) {
			DlgErrorDetails.showForExcetion(e);
			throw new RuntimeException("A error ocurred while connecting to the PostgreSQL cluster.", e);
		}
	}
	
	public synchronized void disposeConnection(String nodeIdentifier) {
		Connection connection = connections.get(nodeIdentifier);
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection validateConnectionParent(Connector connector) {
		if (connector == null) {
			throw new IllegalStateException("The database object has no connector defined.");
		}
		Connection connection = connector.getConnection();
		if (connection == null) {
			throw new IllegalStateException("Tried to open a database object with no connection established.");
		}
		return connection;
	}
	
	@Override
	public void run() {
		try {
			for (Connection connection : connections.values()) {
				if (!connection.isClosed())
					connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection(String nodeIdentifier) {
		return connections.get(nodeIdentifier);
	}

}
