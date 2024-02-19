package com.daviddev16.style;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.LookAndFeel;

import org.fife.ui.rsyntaxtextarea.Theme;

import com.daviddev16.core.StyleConfigurator;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

public final class DarkStyleConfigurator implements StyleConfigurator {

	private Theme editorTheme;
	private LookAndFeel lookAndFeel;
	
	public DarkStyleConfigurator() {
		try {
			lookAndFeel = new FlatMacDarkLaf();
			InputStream inputStream = DarkStyleConfigurator.class
					.getResourceAsStream("/com/daviddev16/themes/syntax/monokai.xml");
			editorTheme = Theme.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getStyleName() {
		return DarkStyleConfigurator.class.getSimpleName();
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
		return "Dark";
	}
	
}
