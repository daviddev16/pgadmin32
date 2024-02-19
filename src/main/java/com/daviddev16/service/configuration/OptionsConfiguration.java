package com.daviddev16.service.configuration;

import com.daviddev16.style.LightStyleConfigurator;

public class OptionsConfiguration {

	private boolean showStatisticBracketInformation = false;
	private String activeStyleConfiguratorName      = LightStyleConfigurator.class.getSimpleName();
	
	public OptionsConfiguration() {}

	public boolean isStatisticBracketInformationEnabled() {
		return showStatisticBracketInformation;
	}

	public void setStatisticBracketInformationState(boolean showStatisticBracketInformation) {
		this.showStatisticBracketInformation = showStatisticBracketInformation;
	}
	
	public void setActiveStyleConfiguratorName(String activeStyleConfiguratorName) {
		this.activeStyleConfiguratorName = activeStyleConfiguratorName;
	}
	
	public String getActiveStyleConfiguratorName() {
		return activeStyleConfiguratorName;
	}
	
}
