package com.daviddev16;

import java.awt.EventQueue;
import java.awt.Toolkit;

import com.daviddev16.service.ServicesFacade;

public class Launcher {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServicesFacade.createAllServices();
					Toolkit.getDefaultToolkit().setDynamicLayout(true);
					String configuredStyleName = ServicesFacade.getServices().getOptionsConfiguration()
							.getActiveStyleConfiguratorName();
					FrmApplicationMain frame = new FrmApplicationMain();
					ServicesFacade.getServices().getStyleManager().configureStyleByName(configuredStyleName);
					frame.setVisible(true);			
				} catch (Exception e) {
					e.printStackTrace();	
				}
			}
		});

	}

}
