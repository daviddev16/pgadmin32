package com.daviddev16.listener;

import java.io.IOException;

import javax.swing.JOptionPane;

import com.daviddev16.core.EventListener;
import com.daviddev16.core.annotation.EventHandler;
import com.daviddev16.core.event.EventPriority;
import com.daviddev16.event.interaction.CreatedServerEvent;
import com.daviddev16.service.ServicesFacade;
import com.daviddev16.service.configuration.ServerConfiguration;
import com.daviddev16.service.handler.ServerConfigurationHandler;

public class ModifiedStateForConfigurationEventListener implements EventListener {

	private final ServerConfigurationHandler serverConfigurationHandler;

	public ModifiedStateForConfigurationEventListener() {
		serverConfigurationHandler = ServicesFacade.getServices()
				.getServerConfigurationHandler();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCreatedNewServerEvent(CreatedServerEvent createdServerEvent) {
		try {
			ServerConfiguration serverConfiguration = serverConfigurationHandler.getHandledConfiguration();
			serverConfiguration.getServers().add(createdServerEvent.getNewestServer());
			serverConfigurationHandler.save();
		} catch (IOException e) {
			createdServerEvent.cancel();
			JOptionPane.showMessageDialog(null, "Não foi possível salvar a configuração!");
		}
	}

}
