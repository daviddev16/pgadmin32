package com.daviddev16.service.configuration;

import java.util.ArrayList;
import java.util.List;

import com.daviddev16.node.Server;

public class ServerConfiguration {

	private List<Server> servers = new ArrayList<Server>();

	public ServerConfiguration() {}
	
	public List<Server> getServers() {
		return servers;
	}

	public void setServers(List<Server> servers) {
		this.servers = servers;
	}
	
}
