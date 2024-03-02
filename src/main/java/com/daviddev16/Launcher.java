package com.daviddev16;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.daviddev16.service.ServicesFacade;
import com.formdev.flatlaf.util.SystemInfo;

public class Launcher {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if (SystemInfo.isMacOS) {
						System.setProperty("apple.laf.useScreenMenuBar", "true");
						System.setProperty("apple.awt.application.name", "pgAdmin32");
						System.setProperty("apple.awt.application.appearance", "system");
					}
	
					JFrame.setDefaultLookAndFeelDecorated(true);
					JDialog.setDefaultLookAndFeelDecorated(true);
					
					ServicesFacade.createAllServices();
					
					String configuredStyleName = ServicesFacade.getServices()
							.getOptionsConfiguration().getActiveStyleConfiguratorName();

					FrmApplicationMain frame = new FrmApplicationMain();
					frame.setVisible(true);			
					
					ServicesFacade.getServices().getStyleManager().configureStyleByName(configuredStyleName);
					Toolkit.getDefaultToolkit().setDynamicLayout(true);
					
				} catch (Exception e) {
					e.printStackTrace();	
				}
			}
		});
	}

}
