package com.daviddev16.style;

import java.awt.Color;

import javax.swing.UIManager;

public class CustomStylePropertiesHolder {

	private final Color customInnerBackgroundColor;
	private final Color customOuterComponentLineColor;
	
	public CustomStylePropertiesHolder() {
		customInnerBackgroundColor    = copyColor("Style.innerComponentBackgroundColor");
		customOuterComponentLineColor = copyColor("Style.outerComponentLineColor");
	}
	
	private Color copyColor(final String uiColorKey) {
		return new Color(UIManager.getColor(uiColorKey).getRGB());
	}
	
	public Color getInnerBackgroundColor() {
		return customInnerBackgroundColor;
	}
	
	public Color getOuterComponentLineColor() {
		return customOuterComponentLineColor;
	}

}
