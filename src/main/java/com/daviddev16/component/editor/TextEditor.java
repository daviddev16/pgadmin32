package com.daviddev16.component.editor;



import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import com.daviddev16.core.EventListener;
import com.daviddev16.core.StyleConfigurator;
import com.daviddev16.core.annotation.EventHandler;
import com.daviddev16.event.style.ChangedStyleStateEvent;
import com.daviddev16.service.ServicesFacade;

public class TextEditor extends RSyntaxTextArea implements EventListener {

	private static final long serialVersionUID = 7716497063385980345L;

	public TextEditor() {
		super();
		StyleConfigurator activeStyleConfigurator = ServicesFacade.getServices()
				.getStyleManager().getActiveStyleConfigurator();
		
		if (activeStyleConfigurator != null)
			applyStyleConfigurator(activeStyleConfigurator);
		
		ServicesFacade.getServices()
			.getEventManager().registerListener(this);
		
	}
	
	@EventHandler
	public void onChangedStyleStateEvent(ChangedStyleStateEvent changedStyleStateEvent) {
		StyleConfigurator styleConfigurator = changedStyleStateEvent.getStyleConfigurator();
		applyStyleConfigurator(styleConfigurator);
	}
	
	private void applyStyleConfigurator(StyleConfigurator styleConfigurator) {
		styleConfigurator.getEditorTheme().apply(this);
	}
	
}
