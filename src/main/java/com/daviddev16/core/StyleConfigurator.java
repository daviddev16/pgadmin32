package com.daviddev16.core;

import javax.swing.LookAndFeel;

import org.fife.ui.rsyntaxtextarea.Theme;

public interface StyleConfigurator {

	default String getStyleName() { return getClass().getSimpleName(); }

	String getStyleDisplayName();
	
	default void initialize() {}

	default void done() {}

	Theme getEditorTheme();
	
	LookAndFeel getStyleLookAndFeel();
	
}
