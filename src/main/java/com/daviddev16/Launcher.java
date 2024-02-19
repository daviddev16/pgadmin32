package com.daviddev16;

import java.awt.EventQueue;

import com.daviddev16.service.ServicesFacade;

public class Launcher {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServicesFacade.createAllServices();
					String configuredStyleName = ServicesFacade.getServices().getOptionsConfiguration()
							.getActiveStyleConfiguratorName();
					ServicesFacade.getServices().getStyleManager().configureStyleByName(configuredStyleName);
					FrmApplicationMain frame = new FrmApplicationMain();
					frame.setVisible(true);			
				} catch (Exception e) {
					e.printStackTrace();	
				}
			}
		});

	}

}
