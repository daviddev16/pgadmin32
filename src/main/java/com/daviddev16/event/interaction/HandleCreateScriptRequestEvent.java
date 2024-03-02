package com.daviddev16.event.interaction;

import com.daviddev16.core.event.AbstractEvent;

public class HandleCreateScriptRequestEvent extends AbstractEvent {

	private final String sqlScript;
	
	public HandleCreateScriptRequestEvent(Object sender, String sqlScript) {
		super(sender);
		this.sqlScript = sqlScript;
	}

	public String getSQLScript() {
		return sqlScript;
	}

}
