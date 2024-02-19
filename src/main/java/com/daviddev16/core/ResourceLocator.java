package com.daviddev16.core;

import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;

public interface ResourceLocator {
	
	public void register(String resourceIdentifier, Object resourceObject);

	ImageIcon imageIcon(String resourceIdentifier);
	
	ImageIcon cachedImageIcon(String resourceIdentifier);
	
	public Image image(String resourceIdentifier);
	
	public Object objectOf(String resourceIdentifier);
	
	public <T> List<T> findAllByType(Class<T> clazzType);
	
	public <T> T findTyped(String resourceIdentifier, Class<T> clazzType);
	
}
