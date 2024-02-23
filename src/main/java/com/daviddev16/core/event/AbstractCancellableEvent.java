package com.daviddev16.core.event;

public abstract class AbstractCancellableEvent implements CancellableEvent {

	private boolean cancelled = false;
	private final Object sender;
	
	public AbstractCancellableEvent(Object sender) {
		this.sender = sender;
	}

	@Override
	public void cancel() {
		this.cancelled = true;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public Object getSender() {
		return sender;
	}
	
}
