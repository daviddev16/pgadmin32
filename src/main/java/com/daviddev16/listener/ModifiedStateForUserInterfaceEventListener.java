package com.daviddev16.listener;

import com.daviddev16.FrmApplicationMain;
import com.daviddev16.core.EventListener;
import com.daviddev16.core.annotation.EventHandler;
import com.daviddev16.core.event.EventPriority;
import com.daviddev16.event.interaction.CreatedServerEvent;

public class ModifiedStateForUserInterfaceEventListener implements EventListener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onCreatedNewServerEvent(CreatedServerEvent createdServerEvent) {
		FrmApplicationMain.getMainUI().getServerTreeViewer()
			.addServerToTree(createdServerEvent.getNewestServer());
	}
	
}
