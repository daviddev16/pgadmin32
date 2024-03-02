package com.daviddev16.node;

import java.sql.Connection;

import javax.swing.JOptionPane;

import com.daviddev16.core.Connector;
import com.daviddev16.core.DatabaseDataObject;
import com.daviddev16.core.JdbcArtifact;
import com.daviddev16.core.NodeState;
import com.daviddev16.core.annotation.Resourced;
import com.daviddev16.service.AES256Manager;
import com.daviddev16.service.ServicesFacade;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
@Resourced(resourceIdentifier = "Server16px")
public class Server extends DatabaseDataObject implements Connector {

	public static final String DEFAULT_DATABASE_NAME = "postgres";

	@JsonIgnore
	private Connection connection;
	
	private String serverName;
	private String host;
	private Integer port;
	private String username;
	private String password;
	
	public Server() {
		this.setConnector(Server.this);
	}
	
	private void checkObjectsAndDettachConnectionIfNeed(Object old, Object newer) {
		if (!old.equals(newer))
			dettachSelfConnection();
	}
	
	private void dettachSelfConnection() {
		if (connection == null) {
			return;
		}
		try {
			ServicesFacade.getServices().getConnectionManager()
				.disposeConnection(getNodeIdentifier());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				connection = null;
				setNodeState(NodeState.CHANGED);
				JOptionPane.showMessageDialog(null, "Conexão de " + getNodeIdentifier() +
						" foi encerrada por alteração em sua estrutura de conexão.");
			}
	}
	
	@JsonIgnore
	public JdbcArtifact createArtifact() {
		return new JdbcArtifact() {
			@Override
			public String getJdbcUsername() {
				return Server.this.getUsername();
			}
			@Override
			public String getJdbcPort() {
				return Integer.toString(Server.this.getPort());
			}
			@Override
			public String getJdbcPassword() {
				return Server.this.getDecryptedPassword();
			}
			@Override
			public String getJdbcHost() {
				return Server.this.getHost();
			}
		};
	}
	
	@JsonIgnore
	public String getDecryptedPassword() {
		try {
			return AES256Manager.decrypt(password);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to decrypt the password.", e);
		}
	}
	
	@Override
	public String getNodeName() {
		return String.format(
				"<html><b>%s</b> - <i>%s:%d</i> </font></html>", 
				getServerName(), 
				getHost(), 
				getPort());
	}
	
	@Override
	@JsonIgnore
	public String getNodeIdentifier() {
		return serverName;
	}
	
	@Override
	public String toString() {
		return getNodeIdentifier();
	}

	public String getServerName() {
		return serverName;
	}


	public void setServerName(String serverName) {
		this.serverName = serverName;
	}


	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		checkObjectsAndDettachConnectionIfNeed(this.host, host);
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}


	public void setPort(Integer port) {
		checkObjectsAndDettachConnectionIfNeed(this.port, port);
		this.port = port;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		checkObjectsAndDettachConnectionIfNeed(this.username, username);
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		checkObjectsAndDettachConnectionIfNeed(this.password, password);
		this.password = password;
	}

	@Override
	public Connection getConnection() {
		return connection;
	}

	@Override
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}
