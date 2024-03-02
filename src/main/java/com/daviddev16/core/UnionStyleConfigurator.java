package com.daviddev16.core;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.LookAndFeel;

import org.fife.ui.rsyntaxtextarea.Theme;

import com.daviddev16.style.laf.FlatMacUnionLaf;

public final class UnionStyleConfigurator implements StyleConfigurator {

	private Theme editorTheme;
	private LookAndFeel lookAndFeel;
	
	public UnionStyleConfigurator() {
		try {
			lookAndFeel = new FlatMacUnionLaf();
			InputStream inputStream = UnionStyleConfigurator.class
					.getResourceAsStream("/com/daviddev16/themes/syntax/monokai2.xml");
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
		return "Union";
	}
	
}
