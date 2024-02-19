package com.daviddev16.util;

import java.util.UUID;

public final class TextUtil {

	public static String createIdentifier(Class<?> clazzType) {
		String identifierName = clazzType.getSimpleName();
		return createIdentifierStr(identifierName);
	}
	
	public static String createIdentifierStr(String identifierName) {
		return String.format("%s(%s)", identifierName, UUID.randomUUID().toString());
	}
	
	public static String bold(final String nonHtmlText) {
		return String.format("<html><b>%s</b></html>", nonHtmlText);
	}
	
	public static String center(final String nonHtmlText) {
		return String.format("<html><center>%s</center></html>", nonHtmlText);
	}
	
	public static String reduce(String text) {
		return text.replaceAll("\\s+", "");
	}
	
}
