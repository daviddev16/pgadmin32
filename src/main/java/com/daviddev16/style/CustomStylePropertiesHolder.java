package com.daviddev16.style;

import java.awt.Color;

import javax.swing.UIManager;

public class CustomStylePropertiesHolder {

	private final Color customInnerBackgroundColor;
	private final Color customOuterComponentLineColor;
	
	public CustomStylePropertiesHolder() 
	{
		customInnerBackgroundColor    = new Color(UIManager.getColor("Style.innerComponentBackgroundColor").getRGB());
		customOuterComponentLineColor = new Color(UIManager.getColor("Style.outerComponentLineColor").getRGB());
	}
	
	public Color getInnerBackgroundColor() {
		return customInnerBackgroundColor;
	}
	
	public Color getOuterComponentLineColor() {
		return customOuterComponentLineColor;
	}

}
