package com.daviddev16.listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.tree.DefaultMutableTreeNode;

import com.daviddev16.component.ServerTreeViewer;
import com.daviddev16.core.Connector;
import com.daviddev16.core.DataCollectorQueries;
import com.daviddev16.core.EntityMetadata;
import com.daviddev16.core.EventListener;
import com.daviddev16.core.NodeState;
import com.daviddev16.core.annotation.EventHandler;
import com.daviddev16.event.server.SchemaNodeInteractEvent;
import com.daviddev16.node.Schema;
import com.daviddev16.node.Table;
import com.daviddev16.node.group.SequencesGroup;
import com.daviddev16.service.ConnectionManager;
import com.daviddev16.service.ServicesFacade;

public class SchemaEventListener implements EventListener {

	/**
	 * onInteractWithSchemaGroupNodeEvent faz o carregamento das tabelas do namespace do SchemaGroup
	 * na hierarquia do schema no ServerTreeViewer.
	 **/
	@EventHandler
	public void onInteractWithSchemaGroupNodeEvent(SchemaNodeInteractEvent schemaGroupNodeInteractEvent) throws SQLException 
	{
		DefaultMutableTreeNode clickedNodeTree = schemaGroupNodeInteractEvent.getInteractedTreeNode();
		ServerTreeViewer serverTreeViewer = (ServerTreeViewer) schemaGroupNodeInteractEvent.getSender();
		Schema schema = (Schema) clickedNodeTree.getUserObject();

		Connector schemaConnector = schema.getConnector();
		Connection connection = ConnectionManager.validateConnectionParent(schemaConnector);

		final DataCollectorQueries dataCollectorQueries = ServicesFacade.getServices().getDataCollectorQueries();

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String allTablesOfNamespaceQuery = dataCollectorQueries.getAllTablesOfNamespaceQuery();
			preparedStatement = connection.prepareStatement(allTablesOfNamespaceQuery);
			preparedStatement.setString(1, schema.getSchemaName());
			resultSet = preparedStatement.executeQuery();
			clickedNodeTree.removeAllChildren();
			while (resultSet.next()) 
			{
				String tableName = resultSet.getString(1);
				Integer tableOid = resultSet.getInt(4);

				Table table = new Table();
				table.setTableName(tableName);
				table.setConnector(schemaConnector);
				table.setParent(schema);

				EntityMetadata entityMetadata = new EntityMetadata();
				entityMetadata.setParentRelationId( schema.getPostgresObjectMetadata().getId() );
				entityMetadata.setRelationNamespace( schema.getSchemaName() );
				entityMetadata.setRelationName(tableName);
				entityMetadata.setId(tableOid);

				table.setPostgresObjectMetadata(entityMetadata);
				clickedNodeTree.insert(new DefaultMutableTreeNode(table), 0);
			}

			createSequencesLabelInTableNode(schema, clickedNodeTree);
			serverTreeViewer.reloadAndRestoreExpandedState(clickedNodeTree);
			schema.setNodeState(NodeState.LOADED);

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

	private void createSequencesLabelInTableNode(Schema schema, DefaultMutableTreeNode tableNodeTree)
	{
		SequencesGroup sequencesGroup = new SequencesGroup();
		sequencesGroup.setConnector(schema.getConnector());
		sequencesGroup.setParent(schema);

		EntityMetadata entityMetadata = new EntityMetadata();
		entityMetadata.setParentRelationId( schema.getPostgresObjectMetadata().getId() );
		entityMetadata.setRelationNamespace( schema.getPostgresObjectMetadata().getRelationNamespace() );
		entityMetadata.setRelationName( EntityMetadata.DUMMY );

		sequencesGroup.setPostgresObjectMetadata(entityMetadata);

		tableNodeTree.insert(new DefaultMutableTreeNode(sequencesGroup), 0);
	}

}
