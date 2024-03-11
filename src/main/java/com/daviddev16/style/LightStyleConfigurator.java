package com.daviddev16.style;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.LookAndFeel;

import org.fife.ui.rsyntaxtextarea.Theme;

import com.daviddev16.core.StyleConfigurator;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

public final class LightStyleConfigurator implements StyleConfigurator {

	private Theme editorTheme;
	private LookAndFeel lookAndFeel;
	
	public LightStyleConfigurator() {
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(new File("./resources/theme/syntax/eclipse.xml"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			lookAndFeel = new FlatMacLightLaf();
			editorTheme = Theme.load(fileInputStream);
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
