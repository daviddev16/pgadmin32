package com.daviddev16.entity;

public abstract class Statistic {

	private String statisticName;
	private Object statisticValue;

	public String getStatisticName() {
		return statisticName;
	}
	
	public void setStatisticName(String statisticName) {
		this.statisticName = statisticName;
	}
	
	public Object getStatisticValue() {
		return statisticValue;
	}
	
	public void setStatisticValue(Object statisticValue) {
		this.statisticValue = statisticValue;
	}

	@Override
	public String toString() {
		return "Statistic [statisticName=" + statisticName + ", statisticValue=" + statisticValue + "]";
	}
	
}
