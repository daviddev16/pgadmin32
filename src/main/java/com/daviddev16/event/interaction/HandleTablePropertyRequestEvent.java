package com.daviddev16.event.interaction;

import java.util.List;

import com.daviddev16.core.data.TableProperty;
import com.daviddev16.core.event.AbstractEvent;

public class HandleTablePropertyRequestEvent extends AbstractEvent {

	private final List<TableProperty> tableProperties;
	
	public HandleTablePropertyRequestEvent(Object sender, List<TableProperty> tableProperties) {
		super(sender);
		this.tableProperties = tableProperties;
	}

	public List<TableProperty> getTableProperties() {
		return tableProperties;
	}
	
}
