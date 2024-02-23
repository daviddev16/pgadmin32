package com.daviddev16.component;

import javax.swing.JButton;

public class BorderlessButton extends JButton {
	
	private static final long serialVersionUID = 102057044461075908L;
	
	public BorderlessButton() {
		super();
		SwingUtil.initializeDefaultsBordelessButton(this, SwingUtil.DIMENSION_SQUARE_20);
	}

}
