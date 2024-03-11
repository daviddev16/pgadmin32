package com.daviddev16.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.daviddev16.core.EventListener;
import com.daviddev16.core.annotation.EventHandler;
import com.daviddev16.core.event.CancellableEvent;
import com.daviddev16.core.event.Event;
import com.daviddev16.core.event.EventContextHolder;
import com.daviddev16.core.event.EventPriority;

public final class EventManager implements Comparator<EventContextHolder> {

	private final List<EventListener> eventListeners;

	public EventManager() {
		this.eventListeners = Collections.synchronizedList(new ArrayList<EventListener>());
	}

	@Override
	public int compare(EventContextHolder evtCntxHolder1, EventContextHolder evtCntxHolder2) {
		return -Integer.compare(evtCntxHolder1.getEventPriority().getPriorityLevel(), 
				evtCntxHolder2.getEventPriority().getPriorityLevel());
	}

	private List<EventContextHolder> createEventContextHolderFromEvent(Event event) {
		Class<?> eventClassType = event.getClass();
		List<EventContextHolder> compatibleMethodsList = new LinkedList<EventContextHolder>();
		for (EventListener eventListener : getEventListeners()) {
			Class<?> listenerClassType = eventListener.getClass();
			for (Method method : listenerClassType.getMethods()) {
				EventHandler eventHandlerAnn = method.getAnnotation(EventHandler.class);
				if (eventHandlerAnn == null) {
					continue;
				}
				Class<?>[] methodParameterTypes = method.getParameterTypes();
				if (methodParameterTypes.length > 1) {
					throw new IllegalStateException("Methods annotated with @EventHandler "
							+ "can't have more than 1 parameter.");
				}
				if (!methodParameterTypes[0].isAssignableFrom(eventClassType)) {
					continue;
				}
				EventPriority eventPriority = eventHandlerAnn.priority();
				compatibleMethodsList.add(new EventContextHolder(eventListener, method, eventPriority));
			}
		}
		compatibleMethodsList.sort(this);
		return compatibleMethodsList;
	}

	public void dispatchEvent(Event event) {
		Objects.requireNonNull(event, "The \"event\" object must not be null.");
		boolean eventChainCancelledState = false;
		for (EventContextHolder eventContextHolder : createEventContextHolderFromEvent(event)) {			
			if (eventChainCancelledState) 
				break;
			if (event instanceof CancellableEvent) {
				eventChainCancelledState = ((CancellableEvent)event).isCancelled();
			}
			executeEventContextHolder(eventContextHolder, event);				
		}
	}

	private void executeEventContextHolder(EventContextHolder eventContextHolder, Event genericEvent) {
		try {
			EventListener eventListener = eventContextHolder.getEventListener();
			Method  eventHandlerMethod = eventContextHolder.getEventHandlerMethod();
			eventHandlerMethod.invoke(eventListener, genericEvent);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

	}

	public void registerListener(EventListener eventListener) {
		this.eventListeners.add(eventListener);
	}

	public List<EventListener> getEventListeners() {
		return eventListeners;
	}

}
