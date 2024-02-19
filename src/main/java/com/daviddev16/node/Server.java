package com.daviddev16.node;

import java.sql.Connection;

import com.daviddev16.core.Connector;
import com.daviddev16.core.JdbcArtifact;
import com.daviddev16.core.ResourcedEntityDataNode;
import com.daviddev16.core.annotation.Resourced;
import com.daviddev16.service.AES256Manager;
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
@Resourced(resourceIdentifier = "Server16px")
public class Server implements ResourcedEntityDataNode, Connector {

	public static final String DEFAULT_DATABASE_NAME = "postgres";
	public static final Server LOCALHOST;
	
	static 
	{
		LOCALHOST = 
				builder()
					.password("abc@123")
					.host("127.0.0.1")
					.port(5432)
					.serverName("Localhost")
					.username("postgres")
				.build();
	}
	
	private String serverName;
	private String host;
	private Integer port;
	private String username;
	private String password;

	
	private volatile Connection connection;

	@Override
	@JsonIgnore
	public synchronized Connection getConnection() {
		return connection;
	}
	
	@Override
	public synchronized void setConnection(Connection connection) {
		this.connection = connection;
	}

	@Override
	@JsonIgnore
	public String getNodeIdentifier() {
		return serverName;
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
	public String toString() {
		return getNodeIdentifier();
	}

}
