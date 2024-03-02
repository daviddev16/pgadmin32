package com.daviddev16.style;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.LookAndFeel;

import org.fife.ui.rsyntaxtextarea.Theme;

import com.daviddev16.core.StyleConfigurator;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

public final class LightStyleConfigurator implements StyleConfigurator {

	private Theme editorTheme;
	private LookAndFeel lookAndFeel;
	
	public LightStyleConfigurator() {
		try {
			lookAndFeel = new FlatMacLightLaf();
			InputStream inputStream = LightStyleConfigurator.class
					.getResourceAsStream("/com/daviddev16/themes/syntax/eclipse.xml");
			editorTheme = Theme.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public LookAndFeel getStyleLookAndFeel() {
		return lookAndFeel;
	}
	
	@Override
	public Theme getEditorTheme() {
		return editorTheme;
	}

	@Override
	public String getStyleDisplayName() {
		return "Light";
	}
	
}
