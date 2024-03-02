package com.daviddev16.style.laf;

import com.formdev.flatlaf.FlatDarkLaf;

public class FlatMacUnionLaf extends FlatDarkLaf
{
	private static final long serialVersionUID = 4022792848943463827L;
	public static final String NAME = "FlatLaf macOS Union";

	public static boolean setup() {
		return setup( new FlatMacUnionLaf() );
	}

	public static void installLafInfo() {
		installLafInfo( NAME, FlatMacUnionLaf.class );
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return "FlatLaf macOS Union Look and Feel";
	}

	@Override
	public boolean isDark() {
		return true;
	}
}
