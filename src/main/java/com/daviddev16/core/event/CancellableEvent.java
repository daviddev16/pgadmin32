package com.daviddev16.core.event;

public interface CancellableEvent extends Event {

	default boolean isCancelled() {
		return false;
	}
	
	void cancel();
	
}
