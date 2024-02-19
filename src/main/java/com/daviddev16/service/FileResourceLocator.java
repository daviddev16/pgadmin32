package com.daviddev16.service;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.ImageIcon;

import com.daviddev16.core.ResourceLocator;
import com.daviddev16.flatlaf.FlatSVGIcon;
import com.daviddev16.util.Checks;

public final class FileResourceLocator implements ResourceLocator {

	public static final String UNKNOWN = "unknownResource";

	private final Map<String, Object> resources = new ConcurrentHashMap<String, Object>();
	private final Map<String, ImageIcon> cachedImageIcons = new ConcurrentHashMap<String, ImageIcon>();

	public FileResourceLocator() {}

	@Override
	public void register(String resourceIdentifier, Object resourceObject) {
		Checks.nonNull(resourceObject, "resourceObject");
		Checks.nonNull(resourceIdentifier, "resourceIdentifier");
		resources.putIfAbsent(resourceIdentifier, resourceObject);
	}

	@Override
	public ImageIcon cachedImageIcon(String resourceIdentifier) {
		ImageIcon imageIcon = cachedImageIcons.get(resourceIdentifier);
		if (imageIcon == null) {
			imageIcon = imageIcon(resourceIdentifier);
			cachedImageIcons.put(resourceIdentifier, imageIcon);
		}
		return imageIcon;
	}

	@SuppressWarnings("unchecked")
	private <T> T castObjectOf(String resourceIdentifier) {
		return (T) objectOf(resourceIdentifier);
	}

	@Override
	public Object objectOf(String resourceIdentifier) {
		Checks.nonNull(resourceIdentifier, "resourceIdentifier");
		return resources.get(resourceIdentifier);
	}

	@Override
	public ImageIcon imageIcon(String resourceIdentifier) {
		Object imageObject = objectOf(resourceIdentifier);
		Checks.nonNull(imageObject, "imageObject [of '" + resourceIdentifier + "']");
		if (imageObject instanceof FlatSVGIcon) {
			return ((FlatSVGIcon)imageObject);
		}
		if (imageObject instanceof Image) {
			return new ImageIcon(((Image)imageObject));
		}		
		throw new IllegalStateException("The resourceIdentifier does "
				+ "not correspond to a valid image object.");
	}

	@Override
	public Image image(String resourceIdentifier) {
		return castObjectOf(resourceIdentifier);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> findAllByType(Class<T> clazzType) {
		List<T> genericList = new ArrayList<T>();
		for (Object resourceValueObject : resources.values()) {
			if (clazzType.isAssignableFrom(resourceValueObject.getClass()))
				genericList.add((T) resourceValueObject);
		}
		return genericList;

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T findTyped(String resourceIdentifier, Class<T> clazzType) {
		Object resourceValueObject = resources.get(resourceIdentifier);
		if (clazzType.isAssignableFrom(resourceValueObject.getClass())) {
			return (T) resourceValueObject;
		}
		return null;
	}

}
