package com.daviddev16.listener;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.daviddev16.component.ServerTreeViewer;
import com.daviddev16.core.Connector;
import com.daviddev16.core.DataCollectorQueries;
import com.daviddev16.core.EntityMetadata;
import com.daviddev16.core.EventListener;
import com.daviddev16.core.annotation.EventHandler;
import com.daviddev16.core.data.Statistic;
import com.daviddev16.core.data.TableProperty;
import com.daviddev16.event.interaction.HandleCreateScriptRequestEvent;
import com.daviddev16.event.interaction.HandleStatisticRequestEvent;
import com.daviddev16.event.interaction.HandleTablePropertyRequestEvent;
import com.daviddev16.event.server.TableNodeInteractEvent;
import com.daviddev16.node.Column;
import com.daviddev16.node.Table;
import com.daviddev16.node.group.ConstraintsGroup;
import com.daviddev16.node.group.IndexesGroup;
import com.daviddev16.service.ConnectionManager;
import com.daviddev16.service.EventManager;
import com.daviddev16.service.ServicesFacade;

public class TableEventListener implements EventListener {
	
	private final EventManager eventManager = ServicesFacade.getServices().getEventManager();

	/**
	 * 
	 * onInteractWithSchemaGroupNodeEvent faz o carregamento das tabelas do schema selecionado no
	 * ServerTreeViewer.
	 * 
	 **/
	@EventHandler
	public void onInteractWithTableNodeEvent(TableNodeInteractEvent tableNodeInteractEvent) throws SQLException 
	{
		DefaultMutableTreeNode clickedNodeTree = tableNodeInteractEvent.getInteractedTreeNode();
		ServerTreeViewer serverTreeViewer = (ServerTreeViewer) tableNodeInteractEvent.getSender();
		Table table = (Table) clickedNodeTree.getUserObject();

		final DataCollectorQueries dataCollectorQueries = ServicesFacade.getServices().getDataCollectorQueries();

		Connector tableConnector = table.getConnector();
		Connection connection = ConnectionManager.validateConnectionParent(tableConnector);
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String allColumnsOfTableQuery = dataCollectorQueries.getAllColumnsOfTableQuery();
			preparedStatement = connection.prepareStatement(allColumnsOfTableQuery);
			preparedStatement.setString(1, table.getTableName());
			preparedStatement.setString(2, table.getPostgresObjectMetadata()
					.getRelationNamespace());

			resultSet = preparedStatement.executeQuery();
			Set<Column> columns = new LinkedHashSet<Column>();

			clickedNodeTree.removeAllChildren();
			while (resultSet.next()) 
			{
				String columnName = resultSet.getString(1); 
				String columnTypeName = resultSet.getString(2); 
				Integer columnDataTypeLength = resultSet.getInt(3); 
				String columnDefaultDefinition = resultSet.getString(4);
				boolean isColumnNullable = resultSet.getBoolean(5);

				Column column = new Column();
				column.setColumnDataTypeLength(columnDataTypeLength);
				column.setColumnName(columnName);
				column.setColumnTypeName(columnTypeName);
				column.setConnector(tableConnector);
				column.setColumnDefaultDefinition(columnDefaultDefinition);
				column.setNullable(isColumnNullable);
				column.setParent(table);

				EntityMetadata entityMetadata = new EntityMetadata();
				entityMetadata.setRelationNamespace( table.getPostgresObjectMetadata().getRelationNamespace() );
				entityMetadata.setParentRelationId( table.getPostgresObjectMetadata().getId() );
				entityMetadata.setRelationName(columnName);
				//postgresObjectMetadata.setOid(null); Colunas não tem OID

				column.setPostgresObjectMetadata(entityMetadata);
				clickedNodeTree.insert(new DefaultMutableTreeNode(column), 0);
				columns.add(column);
			}
			createConstraintsLabelInTableNode(table, clickedNodeTree);
			createIndexesLabelInTableNode(table, clickedNodeTree);
			serverTreeViewer.expandPath(new TreePath(clickedNodeTree.getPath()));
			String state = serverTreeViewer.expansionUtil.getExpansionState();
			serverTreeViewer.getDefaultModel().reload();
			serverTreeViewer.expansionUtil.setExpansionState(state);

			createDDLCommandInScreen(connection, table, columns);
			createStatistics(table, connection);
			createTableProperties(table, connection);

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (resultSet != null) {
				resultSet.close();
			}
		}
	}

	/*TODO:*/
	private void createDDLCommandInScreen(Connection connection, Table table, Set<Column> columns) throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Set<String> conDefs = new HashSet<String>(); 
		try {
			String constraintDefQuery = "SELECT c.conname, pg_get_constraintdef(c.oid) FROM pg_constraint c WHERE conrelid = ?"
					+ " ORDER  BY conrelid::regclass::text, contype DESC;";
			preparedStatement = connection.prepareStatement(constraintDefQuery);
			preparedStatement.setInt(1, table.getPostgresObjectMetadata().getId());
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) 
			{
				String conname = resultSet.getString(1);
				String conDef = resultSet.getString(2);
				conDefs.add("CONSTRAINT " + conname + " " + conDef);
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (resultSet != null) {
				resultSet.close();
			}
		}

		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(String.format("CREATE TABLE %s.%s\n(\n", table.getPostgresObjectMetadata()
				.getRelationNamespace(), table.getTableName()));
		StringJoiner columnsJoiner = new StringJoiner(",\n");
		for (Column column : columns) {
			String columnDefinition = column.getColumnName() + " ";
			if (column.getColumnTypeName().equals("character varying")) {
				columnDefinition += column.getColumnTypeName() + "(" + column.getColumnDataTypeLength() + ")";
			} else {
				columnDefinition += column.getColumnTypeName();
			}
			if (column.isNullable()) {
				columnDefinition += " NOT NULL";
			}
			if (column.getColumnDefaultDefinition() != null) {
				columnDefinition += " DEFAULT " + column.getColumnDefaultDefinition();
			}
			columnsJoiner.add("    " + columnDefinition);
		}
		conDefs.forEach(constraintDef -> {
			columnsJoiner.add("    " + constraintDef);
		});
		sqlBuilder.append(columnsJoiner.toString() + "\n);");
		
		eventManager.dispatchEvent(new HandleCreateScriptRequestEvent(this, sqlBuilder.toString()));
		
	}

	/*TODO:*/
	private void createStatistics(Table table, Connection connection) throws SQLException {
		
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String statsCols = "seq_scan, seq_tup_read, idx_scan, idx_tup_fetch, n_tup_ins, n_tup_upd, n_tup_del, n_tup_hot_upd, n_live_tup, "
					+ "n_dead_tup, n_mod_since_analyze, last_vacuum, last_autovacuum, last_analyze, last_autoanalyze, vacuum_count, "
					+ "autovacuum_count, analyze_count, autoanalyze_count";
			String query = "select "+statsCols+"  from pg_stat_all_tables where relid = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, table.getPostgresObjectMetadata().getId());
			resultSet = preparedStatement.executeQuery();
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			List<Statistic> statistics = new ArrayList<Statistic>();
			while (resultSet.next()) {
				for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
					Object dataObject = resultSet.getObject(i);
					if (dataObject == null) {
						dataObject = "";
					}
					if (dataObject instanceof Clob || dataObject instanceof Blob) {
						dataObject = "<binary data>";
					}
					statistics.add(new Statistic(resultSetMetaData.getColumnLabel(i), dataObject));
				}
			}
			eventManager.dispatchEvent(new HandleStatisticRequestEvent(this, statistics));
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (resultSet != null) {
				resultSet.close();
			}
		}
	}
	
	/*TODO:*/
	private void createTableProperties(Table table, Connection connection) throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			/*
			 *  não devemos recuperar informações que já são cacheadas entre a estrutura do objeto Table.
			 *  Deve ser usando o máximo de PostgresObjectMetadata para a criação de objetos para renderização.
			 */
			StringBuilder sqlBuilder = new StringBuilder()
					.append("SELECT ")
					.append("	a.rolname, ")
					.append("	(select pg_relation_filepath(c.oid)) AS \"physical_location_path\", ")
					.append("	coalesce(pg_size_pretty(pg_relation_size(c.oid) + pg_relation_size(c.reltoastrelid)),'none') AS \"total_size\", ")
					.append("	coalesce((select c0.relname from pg_class c0 where c0.oid = c.reltoastrelid), 'no toast') AS \"toast_name\", ")
					.append("	coalesce((select pg_size_pretty(pg_relation_size(c.reltoastrelid))), 'no toast') AS \"toast_size\", ")
					.append("   pg_size_pretty(pg_relation_size(c.oid)) AS \"table_size\", ")
					.append("   c.reltoastrelid ")
					.append("FROM ")
					.append("	pg_class c ")
					.append("LEFT JOIN ")
					.append("	pg_authid a ")
					.append("ON ")
					.append("	(a.oid = c.relowner) ")
					.append("WHERE ")
					.append("	c.relkind = 'r' ")
					.append("	AND c.oid = ? ")
					.append("	AND relnamespace = ?; ");
					
			preparedStatement = connection.prepareStatement(sqlBuilder.toString());
			preparedStatement.setInt(1, table.getPostgresObjectMetadata().getId());
			preparedStatement.setInt(2, table.getPostgresObjectMetadata().getParentRelationId());
			resultSet = preparedStatement.executeQuery();
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			List<TableProperty> tableProperties = new ArrayList<TableProperty>();
			tableProperties.add(new TableProperty("oid", table.getPostgresObjectMetadata().getId()));
			tableProperties.add(new TableProperty("relname", table.getPostgresObjectMetadata().getRelationName()));
			tableProperties.add(new TableProperty("nspname", table.getPostgresObjectMetadata().getRelationNamespace()));
			while (resultSet.next()) {
				for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
					Object dataObject = resultSet.getObject(i);
					if (dataObject == null) {
						dataObject = "";
					}
					if (dataObject instanceof Clob || dataObject instanceof Blob) {
						dataObject = "<binary data>";
					}
					tableProperties.add(new TableProperty(resultSetMetaData.getColumnLabel(i), dataObject));
				}
			}
			/* adicionando valores comuns */
			eventManager.dispatchEvent(new HandleTablePropertyRequestEvent(this, tableProperties));
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (resultSet != null) {
				resultSet.close();
			}
		}
	}

	private void createConstraintsLabelInTableNode(Table table, DefaultMutableTreeNode tableNodeTree)
	{
		ConstraintsGroup constraintLabel = new ConstraintsGroup();
		constraintLabel.setConnector(table.getConnector());
		constraintLabel.setParent(table);

		EntityMetadata entityMetadata = new EntityMetadata();
		entityMetadata.setParentRelationId( table.getPostgresObjectMetadata().getId() );
		entityMetadata.setRelationNamespace( table.getPostgresObjectMetadata().getRelationNamespace() );
		entityMetadata.setRelationName( EntityMetadata.DUMMY );

		constraintLabel.setPostgresObjectMetadata(entityMetadata);

		tableNodeTree.insert(new DefaultMutableTreeNode(constraintLabel), 0);
	}

	private void createIndexesLabelInTableNode(Table table, DefaultMutableTreeNode tableNodeTree)
	{
		IndexesGroup indexesLabel = new IndexesGroup();
		indexesLabel.setConnector(table.getConnector());
		indexesLabel.setParent(table);

		EntityMetadata entityMetadata = new EntityMetadata();
		entityMetadata.setParentRelationId( table.getPostgresObjectMetadata().getId() );
		entityMetadata.setRelationNamespace( table.getPostgresObjectMetadata().getRelationNamespace() );
		entityMetadata.setRelationName( EntityMetadata.DUMMY );

		indexesLabel.setPostgresObjectMetadata(entityMetadata);

		tableNodeTree.insert(new DefaultMutableTreeNode(indexesLabel), 1);

	}

}
