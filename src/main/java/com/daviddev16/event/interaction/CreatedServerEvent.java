package com.daviddev16.event.interaction;

import com.daviddev16.core.event.AbstractCancellableEvent;
import com.daviddev16.node.Server;

public class CreatedServerEvent extends AbstractCancellableEvent {

	private final Server newestServer;
	
	public CreatedServerEvent(Object sender, Server newestServer) {
		super(sender);
		this.newestServer = newestServer;
	}
	
	public Server getNewestServer() {
		return newestServer;
	}

}
