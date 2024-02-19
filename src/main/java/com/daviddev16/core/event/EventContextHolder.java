package com.daviddev16.core.event;

import java.lang.reflect.Method;

import com.daviddev16.core.EventListener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public final class EventContextHolder {

	private final EventListener eventListener;
	private final Method eventHandlerMethod;
	private final EventPriority eventPriority;
	
}
