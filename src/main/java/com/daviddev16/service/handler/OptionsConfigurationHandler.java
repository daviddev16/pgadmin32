package com.daviddev16.service.handler;

import java.io.File;
import java.io.IOException;

import com.daviddev16.core.AbstractJsonConfigurationHandler;
import com.daviddev16.service.configuration.OptionsConfiguration;

public class OptionsConfigurationHandler 
	extends AbstractJsonConfigurationHandler<OptionsConfiguration> {

	public OptionsConfigurationHandler(File configurationFile) throws IOException {
		super(configurationFile);
	}
	
	@Override
	public void createIfNecessary() throws IOException {
		OptionsConfiguration optionsConfiguration = new OptionsConfiguration();
		setHandledConfiguration(optionsConfiguration);
		super.createIfNecessary();
	}
	
	public void initialize() {}
	
	@Override
	public Class<OptionsConfiguration> typed() {
		return OptionsConfiguration.class;
	}

}
