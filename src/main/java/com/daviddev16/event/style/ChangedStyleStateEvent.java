package com.daviddev16.event.style;

import com.daviddev16.core.StyleConfigurator;
import com.daviddev16.core.event.Event;
import com.daviddev16.style.CustomStylePropertiesHolder;

/*
 *  Esse evento ocorre quando o StyleManager configura 
 *  um novo estilo na aplicação com sucesso 
 **/
public class ChangedStyleStateEvent implements Event {

	private final StyleConfigurator styleConfigurator;
	private final CustomStylePropertiesHolder customStylePropertiesHolder;

	public ChangedStyleStateEvent(StyleConfigurator styleConfigurator, 
								  CustomStylePropertiesHolder customStylePropertiesHolder) 
	{
		this.styleConfigurator = styleConfigurator;
		this.customStylePropertiesHolder = customStylePropertiesHolder;
	}

	public StyleConfigurator getStyleConfigurator() {
		return styleConfigurator;
	}
	
	public CustomStylePropertiesHolder getCustomStylePropertiesHolder() {
		return customStylePropertiesHolder;
	}

	@Override
	public Object getSender() {
		return null;
	}
	
}
