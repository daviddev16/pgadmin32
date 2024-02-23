package com.daviddev16.component;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;

import com.daviddev16.core.EventListener;
import com.daviddev16.core.annotation.EventHandler;
import com.daviddev16.core.component.TableViewer;
import com.daviddev16.event.style.ChangedStyleStateEvent;
import com.daviddev16.service.ServicesFacade;

public class ScrollableTableViewer extends JScrollPane implements EventListener {
	
	private static final long serialVersionUID = -8948317281462252879L;

	private JTableHeader tableHeader;
	
	public ScrollableTableViewer(final TableViewer tableViewer) {
		tableViewer.setFillsViewportHeight(true);
		tableViewer.setMinimumSize(new Dimension(2000, 100));
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		setViewportView(tableViewer);
		setBorder(new EmptyBorder(0, 0, 0, 0));
		
		ServicesFacade.getServices()
			.getEventManager().registerListener(this);
	}

	public JTableHeader getTableHeader() {
		return tableHeader;
	}
	

	@EventHandler
	public void onChangedStyleStateEvent(ChangedStyleStateEvent styleStateEvent) {
		setBackground(UIManager.getColor("Style.innerComponentBackgroundColor"));
	}
	

}
