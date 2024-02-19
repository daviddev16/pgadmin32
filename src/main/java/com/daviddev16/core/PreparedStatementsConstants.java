package com.daviddev16.core;

public enum PreparedStatementsConstants {

	RELATION_OID("oid", new String[] {"objectIdentifier"}),
	NAMESPACE("nspname", new String[] {"schemaName"}),
	RELNAME("relname", new String[] {"tableName", "relationName", /* mais em pg_class */});
	
	private final String parameterName;
	private final String[] constantAlias;
	
	private PreparedStatementsConstants(String parameterName, String[] constantAlias) 
	{
		this.parameterName = parameterName;
		this.constantAlias = constantAlias;
	}

	public String[] getConstantAlias() {
		return constantAlias;
	}
	
	public String getParameterName() {
		return parameterName;
	}
	
}
