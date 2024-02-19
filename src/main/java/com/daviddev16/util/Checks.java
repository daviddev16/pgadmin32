package com.daviddev16.util;

public class Checks {

	public static void nonNull(Object object, String identifier) {
		if (object == null)
			throw new NullPointerException(String.format("\"%s\" cannot be null.", identifier));
	}
	
}
