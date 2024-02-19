package com.daviddev16.core;

import java.io.IOException;
import java.io.InputStream;

public interface ConfigurationHandler<T> {

	void load(InputStream inputStream) throws IOException;
	
	void reload() throws IOException;

	void save() throws IOException;

	void createIfNecessary() throws IOException;
	
	public T getHandledConfiguration();

	Class<T> typed();
	
	void clear();
	
}
