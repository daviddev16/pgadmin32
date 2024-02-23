package com.daviddev16.core.event;

public abstract class AbstractEvent implements Event {

	private final Object sender;
	
	public AbstractEvent(Object sender) {
		this.sender = sender;
	}

	@Override
	public Object getSender() {
		return sender;
	}
	
}
