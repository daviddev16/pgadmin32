package com.daviddev16.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public interface JdbcArtifact {

	String getJdbcUsername();
	
	String getJdbcPassword();
	
	String getJdbcHost();
	
	String getJdbcPort();
	
	static String toJdbcString(JdbcArtifact artifact, String connectionDatabase) {
		return String.format("jdbc:postgresql://%s:%s/%s", artifact.getJdbcHost(), artifact.getJdbcPort(), connectionDatabase);
	}
	
}
