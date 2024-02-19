package com.daviddev16.core.postgres;

import com.daviddev16.core.AbstractJsonConfigurationHandler;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class PostgresObjectMetadata {

	public static final String NON_PG_CLASSABLE_OBJECT = "nonPgClasseableObject";
	
	private Integer oid;
	private String relationName;
	private String relationNamespace;
	private Integer parentRelationOid;
	
	public static String printMetadata(Object current, PostgresObjectMetadata objectMetadata) {
		try {
			StringBuilder builder = new StringBuilder()
					.append("[DatabaseObject=" + AbstractJsonConfigurationHandler.objectMapper.writeValueAsString(current)).append(", ")
					.append("PostgreSQLMetadata=" + AbstractJsonConfigurationHandler.objectMapper.writeValueAsString(objectMetadata)).append("]");
			System.out.println(builder.toString());
			return builder.toString();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "?";
	}
	
}
