package com.daviddev16.core.data;

public abstract class Property {

	private final String propertyName;
	private final Object propertyValue;

	public Property(String propertyName, Object propertyValue) {
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Object getPropertyValue() {
		return propertyValue;
	}

	@Override
	public String toString() {
		return "Property [propertyName=" + propertyName + ", propertyValue=" + propertyValue + "]";
	}
	
}
