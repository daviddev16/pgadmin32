package com.daviddev16.component;

import java.awt.Dimension;

import javax.swing.AbstractButton;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public final class SwingUtil {

	public static final Border ZERO_INSETS_EMPTY     = new EmptyBorder(0, 0, 0, 0);
	public static final Border INNER_10_INSETS_EMPTY = new EmptyBorder(10, 10, 10, 10);
	
	public static final Dimension DIMENSION_SQUARE_24 = new Dimension(24, 24);
	public static final Dimension DIMENSION_SQUARE_20 = new Dimension(20, 20);
	public static final Dimension DIMENSION_SQUARE_18 = new Dimension(18, 18);
	
	public static void initializeDefaultsBordelessButton(AbstractButton abstractButton, 
														 Dimension preferredSize) 
	{
		abstractButton.setBorder(ZERO_INSETS_EMPTY);
		abstractButton.setPreferredSize(preferredSize);
		abstractButton.setFocusPainted(false);
	}
	
}
