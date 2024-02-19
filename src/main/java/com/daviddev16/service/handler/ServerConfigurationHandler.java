package com.daviddev16.service.handler;

import java.io.File;
import java.io.IOException;

import com.daviddev16.core.AbstractJsonConfigurationHandler;
import com.daviddev16.service.configuration.ServerConfiguration;

public class ServerConfigurationHandler 
	extends AbstractJsonConfigurationHandler<ServerConfiguration> {

	public ServerConfigurationHandler(File configurationFile) throws IOException {
		super(configurationFile);
	}
	
	@Override
	public void createIfNecessary() throws IOException {
		ServerConfiguration newServerConfiguration = new ServerConfiguration();
		//newServerConfiguration.getServers().add(Server.LOCALHOST);
		setHandledConfiguration(newServerConfiguration);
		super.createIfNecessary();
	}
	
	public void initialize() {}
	
	@Override
	public Class<ServerConfiguration> typed() {
		return ServerConfiguration.class;
	}

}
