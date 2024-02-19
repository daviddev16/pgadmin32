package com.daviddev16.event.style;

import com.daviddev16.core.StyleConfigurator;
import com.daviddev16.core.event.Event;

/*
 *  Esse evento ocorre quando o StyleManager configura 
 *  um novo estilo na aplicação com sucesso 
 **/
public class ChangedStyleStateEvent implements Event {

	private StyleConfigurator styleConfigurator;

	public ChangedStyleStateEvent(StyleConfigurator styleConfigurator) {
		this.styleConfigurator = styleConfigurator;
	}

	public StyleConfigurator getStyleConfigurator() {
		return styleConfigurator;
	}

	@Override
	public Object getSender() {
		return null;
	}
	
}
