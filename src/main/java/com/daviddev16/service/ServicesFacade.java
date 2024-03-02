package com.daviddev16.service;

import static com.daviddev16.util.IOUtils.createDerivedSvgImageFromFile;
import static com.daviddev16.util.IOUtils.createImageFromFile;
import static com.daviddev16.util.IOUtils.createSvgImageFromFile;
import static com.daviddev16.util.Resources.path;

import java.io.File;
import java.io.IOException;

import javax.management.InstanceAlreadyExistsException;

import com.daviddev16.collector.PostgresDataCollectorQueries;
import com.daviddev16.core.DataCollectorQueries;
import com.daviddev16.core.ResourceLocator;
import com.daviddev16.core.SQLPreLoader;
import com.daviddev16.core.StyleConfigurator;
import com.daviddev16.core.UnionStyleConfigurator;
import com.daviddev16.listener.GroupsEventListener;
import com.daviddev16.listener.ModifiedStateForConfigurationEventListener;
import com.daviddev16.listener.ModifiedStateForUserInterfaceEventListener;
import com.daviddev16.listener.SchemaEventListener;
import com.daviddev16.listener.ServerHierachyEventListener;
import com.daviddev16.listener.TableEventListener;
import com.daviddev16.service.configuration.OptionsConfiguration;
import com.daviddev16.service.configuration.ServerConfiguration;
import com.daviddev16.service.handler.OptionsConfigurationHandler;
import com.daviddev16.service.handler.ServerConfigurationHandler;
import com.daviddev16.style.DarkStyleConfigurator;
import com.daviddev16.style.LightStyleConfigurator;
import com.daviddev16.style.StyleManager;
import com.daviddev16.util.IOUtils;

public class ServicesFacade {

	private static ServicesFacade servicesFacadeInstance;

	private DataCollectorQueries  dataCollectorQueries;
	private final ResourceLocator fileResourceLocator;

	private final EventManager      eventManager;
	private final ConnectionManager connectionManager;
	private final StyleManager      styleManager;

	private final ServerConfigurationHandler  serverConfigurationHandler;
	private final OptionsConfigurationHandler optionsConfigurationHandler;

	public static void createAllServices() 
			throws InstanceAlreadyExistsException, IOException
	{
		if (servicesFacadeInstance != null)
			throw new InstanceAlreadyExistsException("ServicesFacade is already instantiated.");
		else
			new ServicesFacade();
	}

	private ServicesFacade() 
			throws IOException 
	{
		servicesFacadeInstance = this;
		{
			eventManager 		 = new EventManager();
			styleManager         = new StyleManager();
			fileResourceLocator  = new FileResourceLocator();

			connectionManager 	 = new ConnectionManager();
			dataCollectorQueries = new PostgresDataCollectorQueries();

			serverConfigurationHandler  = new ServerConfigurationHandler(path("config/servers.json"));
			optionsConfigurationHandler = new OptionsConfigurationHandler(path("config/options.json"));
		}
		{
			fileResourceLocator.register("ConstraintsGroup16px",    createSvgImageFromFile( path("ui/ConstraintsGroup16px.svg") ));
			fileResourceLocator.register("ServerHierachyGroup16px", createSvgImageFromFile( path("ui/ServerHierachyGroup16px.svg") ));
			fileResourceLocator.register("IndexesGroup16px", 	    createDerivedSvgImageFromFile( path("ui/IndexesGroup16px.svg"), 13, 13));
			fileResourceLocator.register("SequencesGroup16px", 	    createDerivedSvgImageFromFile( path("ui/SequencesGroup16px.svg"), 13, 13));

			fileResourceLocator.register("SequenceNode16px", 	createSvgImageFromFile( path("ui/SequenceNode16px.svg") ));
			fileResourceLocator.register("ConstraintNode13px", createDerivedSvgImageFromFile( path("ui/ConstraintNode16px.svg"), 13, 13));
			fileResourceLocator.register("IndexNode13px", 	   createDerivedSvgImageFromFile( path("ui/IndexNode16px.svg"), 13, 13));

			fileResourceLocator.register("Server16px",   createSvgImageFromFile( path("ui/Server16px.svg") ));
			fileResourceLocator.register("Database16px", createSvgImageFromFile( path("ui/Database16px.svg") ));
			fileResourceLocator.register("Table16px", 	 createSvgImageFromFile( path("ui/Table16px.svg") ));
			fileResourceLocator.register("Execute16px",  createSvgImageFromFile( path("ui/Execute16px.svg") ));

			fileResourceLocator.register("Column16px",  createSvgImageFromFile( path("ui/Column16px.svg") ));
			fileResourceLocator.register("Catalog10px", createDerivedSvgImageFromFile( path("ui/Catalog16px.svg"), 10, 10));
			fileResourceLocator.register("Schema10px",	createDerivedSvgImageFromFile( path("ui/Schema16px.svg"), 10, 10));
			fileResourceLocator.register("Error32px",	createDerivedSvgImageFromFile( path("ui/Error16px.svg"), 32, 32));

			fileResourceLocator.register("Postgres16px",  createSvgImageFromFile( path("ui/Postgres16px.svg") ));
			fileResourceLocator.register("Property16px",  createSvgImageFromFile( path("ui/Property16px.svg") ));
			fileResourceLocator.register("Statistic16px", createSvgImageFromFile( path("ui/Statistic16px.svg") ));
			fileResourceLocator.register("Up16px",        createSvgImageFromFile( path("ui/Up16px.svg") ));

			fileResourceLocator.register("pgadmin1", createImageFromFile( path("ui/pgadmin-1.png") ));
			fileResourceLocator.register("pgadmin2", createImageFromFile( path("ui/pgadmin-2.png") ));
			fileResourceLocator.register("pgadmin3", createImageFromFile( path("ui/pgadmin-3.png") ));
			fileResourceLocator.register("pgadmin4", createImageFromFile( path("ui/pgadmin-4.png") ));

			fileResourceLocator.register("Collapse16px", createSvgImageFromFile( path("ui/Collapse16px.svg")));
		}
		{
			configureAllSQLPreLoaders( path("preloaders") );
		}
		{
			eventManager.registerListener(new ModifiedStateForConfigurationEventListener());
			eventManager.registerListener(new ModifiedStateForUserInterfaceEventListener());

			eventManager.registerListener(new ServerHierachyEventListener());
			eventManager.registerListener(new SchemaEventListener());
			eventManager.registerListener(new GroupsEventListener());
			eventManager.registerListener(new TableEventListener());

			styleManager.registerStyle(new LightStyleConfigurator());
			styleManager.registerStyle(new DarkStyleConfigurator());
			styleManager.registerStyle(new UnionStyleConfigurator());
		}		
	}

	public void configureAllSQLPreLoaders(File preloadersDirectory) {
		if (preloadersDirectory.isDirectory()) 
		{
			for (File preLoaderSqlFile : preloadersDirectory.listFiles()) {
				try {
					String preloaderResourceName = IOUtils.getFileName(preLoaderSqlFile);
					String preloaderSqlContent = IOUtils.readFile(preLoaderSqlFile);
					SQLPreLoader sqlPreLoader = new SQLPreLoader(preloaderResourceName, preloaderSqlContent);
					fileResourceLocator.register(preloaderResourceName, sqlPreLoader);
					System.out.println(sqlPreLoader);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public StyleConfigurator getActiveStyleConfigurator() {
		String activeStyleConfiguratorName = getOptionsConfiguration().getActiveStyleConfiguratorName();
		if (activeStyleConfiguratorName != null) {
			return getStyleManager().getStyleConfigurators().get(activeStyleConfiguratorName);
		}
		return null;
	}

	public static ServicesFacade getServices() {
		return servicesFacadeInstance;
	}

	public DataCollectorQueries getDataCollectorQueries() {
		return dataCollectorQueries;
	}

	public ServerConfigurationHandler getServerConfigurationHandler() {
		return serverConfigurationHandler;
	}

	public OptionsConfigurationHandler getOptionsConfigurationHandler() {
		return optionsConfigurationHandler;
	}

	public ResourceLocator getFileResourceLocator() {
		return fileResourceLocator;
	}

	public ConnectionManager getConnectionManager() {
		return connectionManager;
	}

	public StyleManager getStyleManager() {
		return styleManager;
	}

	public ServerConfiguration getServerConfiguration() {
		return serverConfigurationHandler
				.getHandledConfiguration();
	}

	public OptionsConfiguration getOptionsConfiguration() {
		return optionsConfigurationHandler
				.getHandledConfiguration();
	}

	public EventManager getEventManager() {
		return eventManager;
	}

}
