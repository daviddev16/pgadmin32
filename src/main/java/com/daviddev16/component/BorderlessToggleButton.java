package com.daviddev16.component;

import javax.swing.JToggleButton;

public class BorderlessToggleButton extends JToggleButton {
	
	private static final long serialVersionUID = 102057044461075908L;
	
	public BorderlessToggleButton() {
		super();
		SwingUtil.initializeDefaultsBordelessButton(this, SwingUtil.DIMENSION_SQUARE_20);
	}

}
