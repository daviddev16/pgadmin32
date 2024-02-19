package com.daviddev16.util;

import java.io.File;

public final class Resources {

	private static final File RESOURCES_ROOT_DIRECTORY = new File("./resources/"); 
			
	public static File path(String pathname) {
		return new File(RESOURCES_ROOT_DIRECTORY, pathname);
	}
	
}
