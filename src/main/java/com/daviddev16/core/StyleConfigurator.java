package com.daviddev16.core;

import javax.swing.LookAndFeel;

import org.fife.ui.rsyntaxtextarea.Theme;

public interface StyleConfigurator {

	default void initialize() {
		System.out.println(String.format("%s iniciou o carregamneto do estilo.", getStyleName()));
	}

	default void done() {
		System.out.println(String.format("%s carregou o estilo com sucesso.", getStyleName()));
	}
	
	String getStyleDisplayName();

	String getStyleName();

	Theme getEditorTheme();
	
	LookAndFeel getStyleLookAndFeel();
	
}
