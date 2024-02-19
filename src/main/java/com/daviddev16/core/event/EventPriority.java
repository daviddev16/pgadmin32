package com.daviddev16.core.event;

public enum EventPriority {

	HIGHEST((byte)4), 
	HIGH   ((byte)3), 
	MEDIUM ((byte)2), 
	LOW    ((byte)1), 
	LOWEST ((byte)0);
	
	private final byte priority;

	private EventPriority(byte priority) {
		this.priority = priority;
	}
	
	public byte getPriorityLevel() {
		return priority;
	}
}
