package com.daviddev16.util;

public class Checks {

	public static <E> E nonNull(E nonNullableObject, String identifier) {
		if (nonNullableObject == null)
			throw new NullPointerException(String.format("\"%s\" cannot be null.", identifier));
		return nonNullableObject;
	}
	
}
