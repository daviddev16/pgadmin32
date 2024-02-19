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
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import com.daviddev16.FrmApplicationMain;
import com.daviddev16.component.ServerTreeViewer;
import com.daviddev16.core.Connector;
import com.daviddev16.core.DataCollectorQueries;
import com.daviddev16.core.EventListener;
import com.daviddev16.core.annotation.EventHandler;
import com.daviddev16.core.component.TableViewer;
import com.daviddev16.core.postgres.PostgresObjectMetadata;
import com.daviddev16.entity.DefaultStatistic;
import com.daviddev16.entity.Statistic;
import com.daviddev16.entity.StatisticResourcedProperty;
import com.daviddev16.event.server.TableNodeInteractEvent;
import com.daviddev16.node.Column;
import com.daviddev16.node.Table;
import com.daviddev16.node.group.ConstraintsGroup;
import com.daviddev16.node.group.IndexesGroup;
import com.daviddev16.service.ConnectionManager;
import com.daviddev16.service.ServicesFacade;

public class TableEventListener implements EventListener {


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

				PostgresObjectMetadata postgresObjectMetadata = new PostgresObjectMetadata();
				postgresObjectMetadata.setRelationNamespace( table.getPostgresObjectMetadata().getRelationNamespace() );
				postgresObjectMetadata.setParentRelationOid( table.getPostgresObjectMetadata().getOid() );
				postgresObjectMetadata.setRelationName(columnName);
				//postgresObjectMetadata.setOid(null); Colunas não tem OID

				column.setPostgresObjectMetadata(postgresObjectMetadata);
				clickedNodeTree.insert(new DefaultMutableTreeNode(column), 0);
				columns.add(column);
			}
			createConstraintsLabelInTableNode(table, clickedNodeTree);
			createIndexesLabelInTableNode(table, clickedNodeTree);
			serverTreeViewer.expandPath(new TreePath(clickedNodeTree.getPath()));
			String state = serverTreeViewer.expansionUtil.getExpansionState();
			serverTreeViewer.getDefaultModel().reload();
			table.markAsLoaded();
			serverTreeViewer.expansionUtil.setExpansionState(state);

			createDDLCommandInScreen(connection, table, columns);
			createStatistics(table, connection);

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
			preparedStatement.setInt(1, table.getPostgresObjectMetadata().getOid());
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

		RSyntaxTextArea textArea = FrmApplicationMain.getMainUI().sntxtxtrNoSql;
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
		textArea.setText(sqlBuilder.toString().trim());
	}

	/*TODO:*/
	private void createStatistics(Table table, Connection connection) throws SQLException {
		TableViewer tableViewer = FrmApplicationMain.getMainUI().getDataSetTableViewer();

		final DefaultTableModel defaultTableModel = new DefaultTableModel() {
			private static final long serialVersionUID = -6901805822459648889L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableViewer.setColumnModel(new DefaultTableColumnModel());
		tableViewer.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		defaultTableModel.setColumnIdentifiers(new String[] {"Property", "Value"});
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String statsCols = "seq_scan, seq_tup_read, idx_scan, idx_tup_fetch, n_tup_ins, n_tup_upd, n_tup_del, n_tup_hot_upd, n_live_tup, "
					+ "n_dead_tup, n_mod_since_analyze, last_vacuum, last_autovacuum, last_analyze, last_autoanalyze, vacuum_count, "
					+ "autovacuum_count, analyze_count, autoanalyze_count";
			String query = "select "+statsCols+"  from pg_stat_all_tables where relid = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, table.getPostgresObjectMetadata().getOid());
			resultSet = preparedStatement.executeQuery();
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			List<Statistic> statistics = new ArrayList<Statistic>();
			int[] widths = new int[resultSetMetaData.getColumnCount()];
			while (resultSet.next()) {
				for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
					Object dataObject = resultSet.getObject(i);
					if (dataObject == null) {
						dataObject = "";
					}
					if (dataObject instanceof Clob || dataObject instanceof Blob) {
						dataObject = "<binary data>";
					}
					statistics.add(new DefaultStatistic(resultSetMetaData.getColumnLabel(i), dataObject));
				}
			}
			int j = 0;
			for (Statistic statistic : statistics) {
				Vector<Object> dataVector = new Vector<Object>();
				boolean showStatisticBracketInformation = ServicesFacade.getServices()
						.getOptionsConfiguration().isStatisticBracketInformationEnabled();
				StatisticResourcedProperty statisticResourcedProperty = new StatisticResourcedProperty(statistic, showStatisticBracketInformation);
				dataVector.add(statisticResourcedProperty);
				String descricaoStat = statisticResourcedProperty.getNodeName();
				int fontStrWidth = tableViewer.getFontMetrics(tableViewer.getFont()).stringWidth(descricaoStat) + 40;
				if (widths[j] < fontStrWidth) {
					widths[j] = fontStrWidth;
				}
				j++;
				dataVector.add(statistic.getStatisticValue().toString());
				defaultTableModel.addRow(dataVector);
			}

			tableViewer.setModel(defaultTableModel);
			TableColumn column = null;
			for (int i = 0; i < tableViewer.getColumnModel().getColumnCount(); i++) {
				column = tableViewer.getColumnModel().getColumn(i);
				column.setPreferredWidth(widths[i]);
			}
			tableViewer.revalidate();
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

		PostgresObjectMetadata postgresObjectMetadata = new PostgresObjectMetadata();
		postgresObjectMetadata.setParentRelationOid( table.getPostgresObjectMetadata().getOid() );
		postgresObjectMetadata.setRelationNamespace( table.getPostgresObjectMetadata().getRelationNamespace() );
		postgresObjectMetadata.setRelationName( PostgresObjectMetadata.NON_PG_CLASSABLE_OBJECT );

		constraintLabel.setPostgresObjectMetadata(postgresObjectMetadata);

		tableNodeTree.insert(new DefaultMutableTreeNode(constraintLabel), 0);
	}

	private void createIndexesLabelInTableNode(Table table, DefaultMutableTreeNode tableNodeTree)
	{
		IndexesGroup indexesLabel = new IndexesGroup();
		indexesLabel.setConnector(table.getConnector());
		indexesLabel.setParent(table);

		PostgresObjectMetadata postgresObjectMetadata = new PostgresObjectMetadata();
		postgresObjectMetadata.setParentRelationOid( table.getPostgresObjectMetadata().getOid() );
		postgresObjectMetadata.setRelationNamespace( table.getPostgresObjectMetadata().getRelationNamespace() );
		postgresObjectMetadata.setRelationName( PostgresObjectMetadata.NON_PG_CLASSABLE_OBJECT );

		indexesLabel.setPostgresObjectMetadata(postgresObjectMetadata);

		tableNodeTree.insert(new DefaultMutableTreeNode(indexesLabel), 1);

	}

}
