package com.daviddev16.style;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.LookAndFeel;

import org.fife.ui.rsyntaxtextarea.Theme;

import com.daviddev16.core.StyleConfigurator;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

public final class DarkStyleConfigurator implements StyleConfigurator {

	private Theme editorTheme;
	private LookAndFeel lookAndFeel;
	
	public DarkStyleConfigurator() {
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(new File("./resources/theme/syntax/monokai.xml"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			lookAndFeel = new FlatMacDarkLaf();
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
		return "Dark";
	}
	
}
