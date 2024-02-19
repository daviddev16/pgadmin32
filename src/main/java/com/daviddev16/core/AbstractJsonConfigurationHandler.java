package com.daviddev16.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.daviddev16.util.IOUtils;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractJsonConfigurationHandler<T> implements ConfigurationHandler<T> {

	public static final ObjectMapper objectMapper = new ObjectMapper();
	static {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
		objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
	}
	private final File configurationFile;
	
	private T handledConfiguration;
	
	public AbstractJsonConfigurationHandler(File configurationFile) throws IOException {
		if (!IOUtils.checkIsJson(configurationFile)) {
			throw new IOException(String.format("\"%s\" is not a json file.", 
					configurationFile.getAbsolutePath()));
		}
		this.configurationFile = configurationFile;
		if (!this.configurationFile.exists()) {
			createIfNecessary();
		}
		reload();
		initialize();
	}
	
	public abstract void initialize();

	@Override
	public void save() throws IOException {
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(configurationFile);
			objectMapper.writeValue(fileOutputStream, handledConfiguration);
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		}
	}
	
	@Override
	public void reload() throws IOException {
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(configurationFile);
			load(fileInputStream);
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			if (fileInputStream != null) {
				fileInputStream.close();
			}
		}
	}
	
	@Override
	public void load(InputStream inputStream) throws IOException {
		handledConfiguration = objectMapper
				.readValue(inputStream, typed());
	}

	@Override
	public Class<T> typed() {
		throw new IllegalStateException("AbstractJsonConfigurationHandler<?>.typed()"
				+ " should be properly implemented.");
	}
	
	@Override
	public void createIfNecessary() throws IOException {
		configurationFile.createNewFile();
		save();
	}

	protected void setHandledConfiguration(T handledConfiguration) {
		this.handledConfiguration = handledConfiguration;
	}
	
	@Override
	public T getHandledConfiguration() {
		return handledConfiguration;
	}
	
	@Override
	public void clear() {
		handledConfiguration = null;
	}
	
}
