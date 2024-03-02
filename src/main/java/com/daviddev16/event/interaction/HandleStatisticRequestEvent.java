package com.daviddev16.event.interaction;

import java.util.List;

import com.daviddev16.core.data.Statistic;
import com.daviddev16.core.event.AbstractEvent;

public class HandleStatisticRequestEvent extends AbstractEvent {

	private final List<Statistic> statistics;
	
	public HandleStatisticRequestEvent(Object sender, List<Statistic> statistics) {
		super(sender);
		this.statistics = statistics;
	}
	
	public List<Statistic> getStatistics() {
		return statistics;
	}

}
