package com.daviddev16.style;

import java.awt.Color;

import javax.swing.UIManager;

import com.daviddev16.util.Checks;

public class CustomStylePropertiesHolder {

	private final Color customInnerBackgroundColor;
	private final Color customOuterComponentLineColor;
	
	public CustomStylePropertiesHolder() {
		customInnerBackgroundColor    = copyColor("Style.innerComponentBackgroundColor");
		customOuterComponentLineColor = copyColor("Style.outerComponentLineColor");
	}
	
	private Color copyColor(final String uiColorKey) {
		final Color originalColor = UIManager.getColor(uiColorKey);
		Checks.nonNull(originalColor, String.format("[color of %s]", uiColorKey));
		return new Color(originalColor.getRGB());
	}
	
	public Color getInnerBackgroundColor() {
		return customInnerBackgroundColor;
	}
	
	public Color getOuterComponentLineColor() {
		return customOuterComponentLineColor;
	}

}
