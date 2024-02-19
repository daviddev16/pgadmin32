package com.daviddev16.collector;

import com.daviddev16.core.DataCollectorQueries;
import com.daviddev16.core.PreparedStatementsConstants;
import com.daviddev16.core.SQLPreLoader;
import com.daviddev16.core.annotation.PreparedQuery;
import com.daviddev16.core.annotation.QueryNamedColumns;
import com.daviddev16.core.annotation.UnpreparedQuery;
import com.daviddev16.service.ServicesFacade;

public final class PostgresDataCollectorQueries implements DataCollectorQueries {

	public PostgresDataCollectorQueries() {}

	@Override
	@QueryNamedColumns(resultColumns = 
		{"d.datname", "d.oid"}
	)
	@UnpreparedQuery
	public String getEffectiveDatabasesQuery() 
	{
		return new StringBuilder()
				.append("SELECT ")
				.append("  d.datname,")
				.append("  d.oid ")
				.append("FROM ")
				.append("  pg_database d ")
				.append("WHERE ")
				.append("  d.datistemplate = false ")
				.append("ORDER BY ")
				.append("  d.datname DESC")
				.toString();
	}

	@Override
	@QueryNamedColumns(
			resultColumns = {"n.nspname", "d.oid"}
	)
	@UnpreparedQuery
	public String getEffectiveSchemasQuery() 
	{
		return new StringBuilder()
				.append("SELECT ")
				.append("  n.nspname,")
				.append("  n.oid ")
				.append("FROM ")
				.append("  pg_namespace n ")
				.append("WHERE")
				.append("  NOT n.nspname IN ('pg_catalog', 'information_schema') AND")
				.append("  NOT n.nspname LIKE ANY (array['pg_temp%', 'pg_toast%']) ")
				.append("ORDER BY")
				.append("  n.nspname DESC;")
				.toString();
	}

	@Override
	@QueryNamedColumns(resultColumns = 
		{"c.relname", "c.relnamespace", "c.relowner", "c.oid"}
	)
	@PreparedQuery(preparedWith = 
		{ PreparedStatementsConstants.NAMESPACE }
	)
	public String getAllTablesOfNamespaceQuery() 
	{
		return new StringBuilder()
				.append("SELECT ")
				.append("  c.relname,")
				.append("  c.relnamespace, ")
				.append("  c.relowner, ")
				.append("  c.oid ")
				.append("FROM ")
				.append("  pg_class c ")
				.append("LEFT JOIN ")
				.append("  pg_namespace n ")
				.append(" ON ")
				.append("  (n.oid = c.relnamespace)")
				.append("WHERE ")
				.append("  n.nspname = ? AND c.relkind = 'r' ")
				.append("ORDER BY ")
				.append("  c.relname DESC;")
				.toString();
	}
	
	@Override
	@QueryNamedColumns(
			resultColumns = 
		{"c.column_name", "c.data_type", "c.character_maximum_length"}
	)
	@PreparedQuery(preparedWith = 
		{ PreparedStatementsConstants.RELNAME, PreparedStatementsConstants.NAMESPACE }
	)
	public String getAllColumnsOfTableQuery() 
	{
		return new StringBuilder()
				.append("SELECT ")
				.append("  c.column_name,")
				.append("  c.data_type, ")
				.append("  c.character_maximum_length, ")
				.append(" c.column_default, ")
				.append("  c.is_nullable ")
				.append("FROM ")
				.append("  information_schema.columns c ")
				.append("WHERE ")
				.append("  c.table_name = ? AND ")
				.append("  c.table_schema = ? ")
				.append("ORDER BY ")
				.append("  c.ordinal_position ASC;")
				.toString();
	}

	@Override
	public String getAllCatalogsQuery() 
	{
		return new StringBuilder()
				.append("SELECT ")
				.append("  n.nspname,")
				.append("  n.oid ")
				.append("FROM ")
				.append("  pg_namespace n ")
				.append("WHERE ")
				.append("  n.nspname IN ('pg_catalog', 'information_schema') OR")
				.append("  n.nspname LIKE ANY (array['pg_temp%', 'pg_toast%']) ")
				.append("ORDER BY ")
				.append("  n.nspname DESC;")
				.toString();
	}

	@Override
	@QueryNamedColumns(
			resultColumns = 
		{"c.relname", "c.oid", "seq_last_value"}
	)
	@PreparedQuery(preparedWith = 
		{ PreparedStatementsConstants.NAMESPACE }
	)
	public String getAllSequencesOfNamespaceQuery() 
	{
		return new StringBuilder()
				.append("SELECT ")
				.append("  c.relname,")
				.append("  c.oid, ")
				.append("get_sequence_last_value((select distinct n.nspname from pg_namespace n where n.oid = c.relnamespace) || '.' || c.relname) as \"seq_last_value\"")
				.append("FROM ")
				.append("  pg_class c ")
				.append("WHERE ")
				.append("  c.relnamespace = ? AND relkind = 'S' ")
				.append("ORDER BY ")
				.append("  c.relname DESC;")
				.toString();
	}
	

	@Override
	@QueryNamedColumns(
			resultColumns = 
		{"c.relname", "i.indexrelid"}
	)
	@PreparedQuery(preparedWith = 
		{ PreparedStatementsConstants.RELATION_OID }
	)
	public String getAllIndexesOfTableQuery() 
	{
		return new StringBuilder()
				.append("SELECT ")
				.append("  c.relname,")
				.append("  i.indexrelid ")
				.append("FROM ")
				.append("  pg_index i ")
				.append("LEFT JOIN ")
				.append("  pg_class c ")
				.append(" ON ")
				.append("  (c.oid = i.indexrelid)")
				.append("WHERE ")
				.append("  i.indrelid = ? ")
				.append("ORDER BY ")
				.append("  c.relname DESC;")
				.toString();
	}

	@Override
	@QueryNamedColumns(
			resultColumns = 
		{"c.conname", "i.conindid"}
	)
	@PreparedQuery(preparedWith = 
		{ PreparedStatementsConstants.RELATION_OID }
	)
	public String getAllConstraintsOfTableQuery()
	{
		return new StringBuilder()
				.append("SELECT ")
				.append("  ct.conname,")
				.append("  ct.conindid ")
				.append("FROM ")
				.append("  pg_constraint ct ")
				.append("LEFT JOIN ")
				.append("  pg_class c ")
				.append(" ON ")
				.append("  (c.oid = ct.conrelid)")
				.append("WHERE ")
				.append("  c.oid = ? ")
				.append("ORDER BY ")
				.append("  ct.conname DESC;")
				.toString();
	}
	
	@Override
	public String getCreateOfFunctionSequenceLastValueSQL() 
	{
		return ServicesFacade.getServices()
				.getFileResourceLocator()
				.findTyped("CreateOfFunctionSequenceLastValueQuery", SQLPreLoader.class)
				.getPreloaderSqlScript();
	}

}
