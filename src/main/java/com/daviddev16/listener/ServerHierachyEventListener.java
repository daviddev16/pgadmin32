package com.daviddev16.listener;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.tree.DefaultMutableTreeNode;

import com.daviddev16.component.ServerTreeViewer;
import com.daviddev16.core.Connector;
import com.daviddev16.core.DataCollectorQueries;
import com.daviddev16.core.EntityMetadata;
import com.daviddev16.core.EventListener;
import com.daviddev16.core.JdbcArtifact;
import com.daviddev16.core.NodeState;
import com.daviddev16.core.annotation.EventHandler;
import com.daviddev16.event.server.DatabaseNodeInteractEvent;
import com.daviddev16.event.server.ServerNodeInteractEvent;
import com.daviddev16.node.Database;
import com.daviddev16.node.Schema.SchemaType;
import com.daviddev16.node.Server;
import com.daviddev16.node.group.SchemaGroup;
import com.daviddev16.service.ConnectionManager;
import com.daviddev16.service.ServicesFacade;

public class ServerHierachyEventListener implements EventListener {
	
	/** 
	 * 
	 * onInteractWithDatabaseNodeEvent faz o carregamento dos bancos de dados, acessando a base de dados
	 * inicialmente a base de dados "postgres". Carrega os bancos de dados do cluster na hierarquia do
	 * ServerTreeViewer.
	 * 
	 **/
	@EventHandler
	public void onInteractWithServerNodeEvent(ServerNodeInteractEvent serverNodeInteractEvent) throws SQLException 
	{
		DefaultMutableTreeNode clickedNodeTree = serverNodeInteractEvent.getInteractedTreeNode();
		ServerTreeViewer serverTreeViewer = (ServerTreeViewer) serverNodeInteractEvent.getSender();
		Server server = (Server) clickedNodeTree.getUserObject();

		final ConnectionManager connectionManager = ServicesFacade.getServices().getConnectionManager();
		final DataCollectorQueries dataCollectorQueries = ServicesFacade.getServices().getDataCollectorQueries();

		Connection connection = server.getConnection();
		JdbcArtifact serverJdbcArtifact = server.createArtifact();
		if (connection == null) {
			connection = connectionManager.createConnection(server.getNodeIdentifier(), 
					server.createArtifact(), Server.DEFAULT_DATABASE_NAME);
			synchronized (connection) {
				server.setConnection(connection);	
			}
		}
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			String effectiveDatabasesQuery = dataCollectorQueries.getEffectiveDatabasesQuery();
			resultSet = statement.executeQuery(effectiveDatabasesQuery);
			serverNodeInteractEvent.getInteractedTreeNode().removeAllChildren();
			while (resultSet.next()) 
			{
				String databaseName = resultSet.getString(1);
				Integer databaseOid = resultSet.getInt(2);

				Database database = new Database();
				database.setConnector(null); /* database deve ser seu proprio Connector */
				database.setDatabaseName(databaseName);

				EntityMetadata entityMetadata = new EntityMetadata();
				entityMetadata.setId(databaseOid);
				entityMetadata.setRelationName(databaseName);

				database.setPostgresObjectMetadata(entityMetadata);
				database.setParentJdbcArtifact(serverJdbcArtifact);

				clickedNodeTree.insert(new DefaultMutableTreeNode(database), 0);
			}

			serverTreeViewer.reloadAndRestoreExpandedState(clickedNodeTree);
			server.setNodeState(NodeState.LOADED);

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
	 * onInteractWithDatabaseNodeEvent faz o carregamento dos grupos de relacionamento do
	 * banco de dados selecionado no ServerTreeViewer.
	 * 
	 **/
	@EventHandler
	public void onInteractWithDatabaseNodeEvent(DatabaseNodeInteractEvent databaseNodeInteractEvent) throws SQLException 
	{
		DefaultMutableTreeNode clickedNodeTree = databaseNodeInteractEvent.getInteractedTreeNode();
		ServerTreeViewer serverTreeViewer = (ServerTreeViewer) databaseNodeInteractEvent.getSender();
		Database database = (Database) clickedNodeTree.getUserObject();

		final ConnectionManager connectionManager = ServicesFacade.getServices().getConnectionManager();

		Connection connection = database.getConnection();
		if (connection == null) {
			connection = connectionManager.createConnection(database.getNodeIdentifier(), 
					database.getParentJdbcArtifact(), database.getDatabaseName());
			synchronized (connection) {
				database.setConnection(connection);			
			}	
		}

		clickedNodeTree.removeAllChildren();
		executeAllPreLoadersBefore(database);

		SchemaGroup schemaGroupLabel = new SchemaGroup(SchemaType.COMMON);
		schemaGroupLabel.setParent(database);
		schemaGroupLabel.setConnector(database);

		EntityMetadata entityMetadata = new EntityMetadata();
		entityMetadata.setParentRelationId( database.getPostgresObjectMetadata().getId() );

		schemaGroupLabel.setPostgresObjectMetadata(entityMetadata);

		clickedNodeTree.insert(new DefaultMutableTreeNode(schemaGroupLabel), 0);

		SchemaGroup catalogGroupLabel = new SchemaGroup(SchemaType.CATALOG);
		catalogGroupLabel.setParent(database);
		catalogGroupLabel.setConnector(database);

		EntityMetadata postgresObjectMetadata1 = new EntityMetadata();
		postgresObjectMetadata1.setParentRelationId( database.getPostgresObjectMetadata().getId() );

		catalogGroupLabel.setPostgresObjectMetadata(postgresObjectMetadata1);

		clickedNodeTree.insert(new DefaultMutableTreeNode(catalogGroupLabel), 1);

		serverTreeViewer.reloadAndRestoreExpandedState(clickedNodeTree);
		
		database.setNodeState(NodeState.LOADED);
	}

	private void executePreLoader(Connector connector, String preLoaderSqlScript) 
			throws SQLException {
		Connection connection = ConnectionManager.validateConnectionParent(connector);
		Statement statement = null;
		@SuppressWarnings("unused")
		boolean done = false;
		try {
			statement = connection.createStatement();
			done = !statement.execute(preLoaderSqlScript);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}
	
	private void executeAllPreLoadersBefore(Connector connector) throws SQLException 
	{
		DataCollectorQueries dataCollectorQueries = ServicesFacade.getServices().getDataCollectorQueries();
		for (String preLoaderScript : dataCollectorQueries.getAllPreLoadersSQL()) {
			executePreLoader(connector, preLoaderScript);
		}
	}

}
