package com.daviddev16.event.query;

import com.daviddev16.core.event.AbstractCancellableEvent;

public class QueryRequestEvent extends AbstractCancellableEvent {

	private final String queryText;
	
	public QueryRequestEvent(Object sender, String queryText) {
		super(sender);
		this.queryText = queryText;
	}
	
	public String getQueryText() {
		return queryText;
	}
	
}
