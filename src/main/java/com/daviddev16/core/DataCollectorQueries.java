package com.daviddev16.core;

public interface DataCollectorQueries extends DataColletorPreLoaderQueries {

	String getEffectiveDatabasesQuery();

	String getEffectiveSchemasQuery();
	
	String getAllTablesOfNamespaceQuery();
	
	String getAllCatalogsQuery();
	
	String getAllColumnsOfTableQuery();

	String getAllSequencesOfNamespaceQuery();
	
	String getAllIndexesOfTableQuery();
	
	String getAllConstraintsOfTableQuery();
	
}
