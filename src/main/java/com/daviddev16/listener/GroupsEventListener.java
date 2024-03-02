package com.daviddev16.listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.tree.DefaultMutableTreeNode;

import com.daviddev16.component.ServerTreeViewer;
import com.daviddev16.core.Connector;
import com.daviddev16.core.DataCollectorQueries;
import com.daviddev16.core.EntityMetadata;
import com.daviddev16.core.EventListener;
import com.daviddev16.core.NodeState;
import com.daviddev16.core.annotation.EventHandler;
import com.daviddev16.event.server.ConstraintsGroupNodeInteractEvent;
import com.daviddev16.event.server.IndexGroupNodeInteractEvent;
import com.daviddev16.event.server.SchemaGroupNodeInteractEvent;
import com.daviddev16.event.server.SequenceGroupNodeInteractEvent;
import com.daviddev16.node.Constraint;
import com.daviddev16.node.Index;
import com.daviddev16.node.Schema;
import com.daviddev16.node.Schema.SchemaType;
import com.daviddev16.node.Sequence;
import com.daviddev16.node.group.ConstraintsGroup;
import com.daviddev16.node.group.IndexesGroup;
import com.daviddev16.node.group.SchemaGroup;
import com.daviddev16.node.group.SequencesGroup;
import com.daviddev16.service.ConnectionManager;
import com.daviddev16.service.ServicesFacade;

public class GroupsEventListener implements EventListener {
	
	/**
	 * 
	 * onInteractWithSchemaGroupNodeEvent faz o carregamento dos schemas do banco de dados
	 * selecionado no ServerTreeViewer. Essa função associa 2 tipos de schemas: 
	 * 
	 * {@link SchemaType#CATALOG}:
	 * Renderiza os schemas do catálogo do banco de dados selecionado.
	 * 
	 * {@link SchemaType#COMMON}:
	 * Renderiza os schemas criados por usuário, do banco de dados selecionado.
	 * 
	 **/
	@EventHandler
	public void onInteractWithSchemaGroupNodeEvent(SchemaGroupNodeInteractEvent schemaGroupNodeInteractEvent) throws SQLException 
	{

		DefaultMutableTreeNode clickedNodeTree = schemaGroupNodeInteractEvent.getInteractedTreeNode();
		ServerTreeViewer serverTreeViewer = (ServerTreeViewer) schemaGroupNodeInteractEvent.getSender();
		SchemaGroup schemaGroupLabel = (SchemaGroup) clickedNodeTree.getUserObject();
	
		DataCollectorQueries dataCollectorQueries = ServicesFacade.getServices().getDataCollectorQueries();
		
		Connector schemaGroupConnector = schemaGroupLabel.getConnector();
		Connection connection = ConnectionManager.validateConnectionParent(schemaGroupConnector);
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			
			String effectiveSchemasQuery =  schemaGroupLabel.getSchemaType() == SchemaType.CATALOG ? 
					dataCollectorQueries.getAllCatalogsQuery() : dataCollectorQueries.getEffectiveSchemasQuery();

			resultSet = statement.executeQuery(effectiveSchemasQuery);
			clickedNodeTree.removeAllChildren();
			
			while (resultSet.next()) 
			{
				String schemaName = resultSet.getString(1);
				Integer schemaOid = resultSet.getInt(2);

				Schema schema = new Schema();
				schema.setSchemaType(schemaGroupLabel.getSchemaType());
				schema.setConnector(schemaGroupConnector);
				schema.setSchemaName(schemaName);
				schema.setParent(schemaGroupLabel);
				
				EntityMetadata entityMetadata = new EntityMetadata();
				entityMetadata.setParentRelationId( schemaGroupLabel.getPostgresObjectMetadata().getId() );
				entityMetadata.setRelationName(schemaName);
				entityMetadata.setId(schemaOid);

				schema.setPostgresObjectMetadata(entityMetadata);
				clickedNodeTree.insert(new DefaultMutableTreeNode(schema), 0);
				
			}
			serverTreeViewer.reloadAndRestoreExpandedState(clickedNodeTree);
			schemaGroupLabel.setNodeState(NodeState.LOADED);
			
		} catch (Exception e) {
			e.printStackTrace();
		
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (resultSet != null) {
				resultSet.close();
			}
		}
	}
	
	/**
	 * 
	 * onInteractWithSequenceGroupNodeEvent faz o carregamento das sequences do SequencesGroup
	 * selecionado no ServerTreeViewer. As sequencias vem do namespace do schema parente ao 
	 * SequenceGroup.
	 * 
	 **/
	@EventHandler
	public void onInteractWithSequenceGroupNodeEvent(SequenceGroupNodeInteractEvent sequenceGroupNodeInteractEvent) throws SQLException 
	{

		DefaultMutableTreeNode clickedNodeTree = sequenceGroupNodeInteractEvent.getInteractedTreeNode();
		ServerTreeViewer serverTreeViewer = (ServerTreeViewer) sequenceGroupNodeInteractEvent.getSender();
		SequencesGroup sequenceGroupLabel = (SequencesGroup) clickedNodeTree.getUserObject();
	
		DataCollectorQueries dataCollectorQueries = ServicesFacade.getServices().getDataCollectorQueries();
		
		Connector schemaGroupConnector = sequenceGroupLabel.getConnector();
		Connection connection = ConnectionManager.validateConnectionParent(schemaGroupConnector);
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String allSequencesOfNamespaceQuery = dataCollectorQueries.getAllSequencesOfNamespaceQuery();
			Integer sequenceNamespaceOid = sequenceGroupLabel.getPostgresObjectMetadata().getParentRelationId();
			preparedStatement = connection.prepareStatement(allSequencesOfNamespaceQuery);
			preparedStatement.setInt(1, sequenceNamespaceOid);

			resultSet = preparedStatement.executeQuery();
			clickedNodeTree.removeAllChildren();

			while (resultSet.next()) 
			{
				String sequenceName = resultSet.getString(1);
				Integer sequenceOid = resultSet.getInt(2);
				Integer sequenceLastValue = resultSet.getInt(3);

				Sequence sequence = new Sequence();
				sequence.setSequenceName(sequenceName);
				sequence.setSequencelastValue(sequenceLastValue);
				sequence.setParent(sequenceGroupLabel);
				
				EntityMetadata entityMetadata = new EntityMetadata();
				entityMetadata.setParentRelationId(sequenceNamespaceOid);
				entityMetadata.setRelationName(sequenceName);
				entityMetadata.setId(sequenceOid);

				sequence.setPostgresObjectMetadata(entityMetadata);
				clickedNodeTree.insert(new DefaultMutableTreeNode(sequence), 0);
			}
			serverTreeViewer.reloadAndRestoreExpandedState(clickedNodeTree);
			sequenceGroupLabel.setNodeState(NodeState.LOADED);
			
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
	
	/**
	 * 
	 * onInteractWithIndexesGroupNodeEvent faz o carregamento dos indexes do IndexesGroup
	 * selecionado no ServerTreeViewer.
	 * 
	 **/
	@EventHandler
	public void onInteractWithIndexesGroupNodeEvent(IndexGroupNodeInteractEvent indexGroupNodeInteractEvent) throws SQLException 
	{
		DefaultMutableTreeNode clickedNodeTree = indexGroupNodeInteractEvent.getInteractedTreeNode();
		ServerTreeViewer serverTreeViewer = (ServerTreeViewer) indexGroupNodeInteractEvent.getSender();
		IndexesGroup indexesGroup = (IndexesGroup) clickedNodeTree.getUserObject();
	
		DataCollectorQueries dataCollectorQueries = ServicesFacade.getServices().getDataCollectorQueries();
		
		Connector indexesGroupConnector = indexesGroup.getConnector();
		Connection connection = ConnectionManager.validateConnectionParent(indexesGroupConnector);
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String allIndexesOfTableQuery = dataCollectorQueries.getAllIndexesOfTableQuery();
			Integer parentTableOid = indexesGroup.getPostgresObjectMetadata().getParentRelationId();
			preparedStatement = connection.prepareStatement(allIndexesOfTableQuery);
			preparedStatement.setInt(1, parentTableOid);

			resultSet = preparedStatement.executeQuery();
			clickedNodeTree.removeAllChildren();

			while (resultSet.next()) 
			{
				String indexName = resultSet.getString(1);
				Integer indexOid = resultSet.getInt(2);

				Index index = new Index();
				index.setIndexName(indexName);
				index.setParent(indexesGroup);
				
				EntityMetadata entityMetadata = new EntityMetadata();
				entityMetadata.setRelationNamespace(indexesGroup.getPostgresObjectMetadata().getRelationNamespace());
				entityMetadata.setParentRelationId(parentTableOid);
				entityMetadata.setRelationName(indexName);
				entityMetadata.setId(indexOid);

				index.setPostgresObjectMetadata(entityMetadata);
				clickedNodeTree.insert(new DefaultMutableTreeNode(index), 0);
				
			}
			serverTreeViewer.reloadAndRestoreExpandedState(clickedNodeTree);
			indexesGroup.setNodeState(NodeState.LOADED);
			
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
	
	/**
	 * 
	 * onInteractWithIndexesGroupNodeEvent faz o carregamento dos indexes do IndexesGroup
	 * selecionado no ServerTreeViewer.
	 * 
	 **/
	@EventHandler
	public void onInteractWithConstraintsGroupNodeEvent(ConstraintsGroupNodeInteractEvent constraintsGroupNodeInteractEvent) throws SQLException 
	{
		DefaultMutableTreeNode clickedNodeTree = constraintsGroupNodeInteractEvent.getInteractedTreeNode();
		ServerTreeViewer serverTreeViewer = (ServerTreeViewer) constraintsGroupNodeInteractEvent.getSender();
		ConstraintsGroup constraintsGroup = (ConstraintsGroup) clickedNodeTree.getUserObject();
	
		DataCollectorQueries dataCollectorQueries = ServicesFacade.getServices().getDataCollectorQueries();
		
		Connector indexesGroupConnector = constraintsGroup.getConnector();
		Connection connection = ConnectionManager.validateConnectionParent(indexesGroupConnector);
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String allConstraintsOfTableQuery = dataCollectorQueries.getAllConstraintsOfTableQuery();
			Integer parentTableOid = constraintsGroup.getPostgresObjectMetadata().getParentRelationId();
			preparedStatement = connection.prepareStatement(allConstraintsOfTableQuery);
			preparedStatement.setInt(1, parentTableOid);

			resultSet = preparedStatement.executeQuery();
			clickedNodeTree.removeAllChildren();

			while (resultSet.next()) 
			{
				String constraintName = resultSet.getString(1);
				Integer conIndOid = resultSet.getInt(2);

				Constraint constraint = new Constraint();
				constraint.setConstraintName(constraintName);
				constraint.setParent(constraintsGroup);
				
				EntityMetadata entityMetadata = new EntityMetadata();
				entityMetadata.setRelationNamespace(constraintsGroup
						.getPostgresObjectMetadata().getRelationNamespace());
				
				entityMetadata.setParentRelationId(parentTableOid);
				entityMetadata.setRelationName(constraintName);
				entityMetadata.setId(conIndOid);

				constraint.setPostgresObjectMetadata(entityMetadata);
				clickedNodeTree.insert(new DefaultMutableTreeNode(constraint), 0);
				
			}
			serverTreeViewer.reloadAndRestoreExpandedState(clickedNodeTree);
			constraintsGroup.setNodeState(NodeState.LOADED);
			
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
	
}
