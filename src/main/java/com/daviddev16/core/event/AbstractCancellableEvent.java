package com.daviddev16.core.event;

public abstract class AbstractCancellableEvent implements CancellableEvent {

	private boolean cancelled = false;
	
	@Override
	public void cancel() {
		this.cancelled = true;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
}
