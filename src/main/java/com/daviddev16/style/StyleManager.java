package com.daviddev16.style;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.LookAndFeel;

import com.daviddev16.core.ConfigurableStyleManager;
import com.daviddev16.core.StyleConfigurator;
import com.daviddev16.event.style.ChangedStyleStateEvent;
import com.daviddev16.service.EventManager;
import com.daviddev16.service.ServicesFacade;
import com.daviddev16.util.Checks;
import com.daviddev16.util.Resources;
import com.formdev.flatlaf.FlatLaf;

public final class StyleManager implements ConfigurableStyleManager {

	private final EventManager eventManager;
	private final Map<String, StyleConfigurator> styleConfiguratorsMap;
	private volatile StyleConfigurator activeStyleConfigurator;

	public StyleManager() {
		styleConfiguratorsMap = new HashMap<String, StyleConfigurator>();
		eventManager = ServicesFacade.getServices().getEventManager();
		FlatLaf.registerCustomDefaultsSource(Resources.path("theme/flatlaf"));
	}

	@Override
	public void configureStyle(StyleConfigurator styleConfigurator) {
		this.activeStyleConfigurator = styleConfigurator;
		styleConfigurator.initialize();
		LookAndFeel lookAndFeel = styleConfigurator.getStyleLookAndFeel();
		if (!FlatLaf.setup(lookAndFeel)) {
			throw new IllegalStateException("Could not load the style look and feel.");
		}
		styleConfigurator.done();
		eventManager.dispatchEvent(
				new ChangedStyleStateEvent(styleConfigurator, new CustomStylePropertiesHolder()));
	}

	@Override
	public void configureStyleByName(String styleName) {
		Checks.nonNull(styleName, "styleName");
		StyleConfigurator styleConfigurator = styleConfiguratorsMap.get(styleName);
		if (styleConfigurator != null) {
			configureStyle(styleConfigurator);
		}
	}

	public void registerStyle(String styleName, StyleConfigurator styleConfigurator) {
		Checks.nonNull(styleName, "styleName");
		Checks.nonNull(styleConfigurator, "styleConfigurator");
		styleConfiguratorsMap.putIfAbsent(styleName, styleConfigurator);
	}

	public void registerStyle(StyleConfigurator styleConfigurator) {
		registerStyle(styleConfigurator.getStyleName(), styleConfigurator);
	}

	public Collection<StyleConfigurator> getAsListOfStyleConfigurators() {
		return styleConfiguratorsMap.values();
	}

	public Map<String, StyleConfigurator> getStyleConfigurators() {
		return styleConfiguratorsMap;
	}

	public StyleConfigurator getActiveStyleConfigurator() {
		return activeStyleConfigurator;
	}

}
