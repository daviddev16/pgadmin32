package com.daviddev16.component;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;

import com.daviddev16.core.EventListener;
import com.daviddev16.core.annotation.EventHandler;
import com.daviddev16.core.component.TableViewer;
import com.daviddev16.event.style.ChangedStyleStateEvent;
import com.daviddev16.service.ServicesFacade;

public class ScrollableTablePanel extends JScrollPane implements EventListener {
	
	private static final long serialVersionUID = -8948317281462252879L;

	private JTableHeader tableHeader;
	
	public ScrollableTablePanel(final TableViewer tableViewer) 
	{
		super();
		tableViewer.setFillsViewportHeight(true);
		tableViewer.setMinimumSize(new Dimension(2000, 100));
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		setViewportView(tableViewer);
		
		ServicesFacade.getServices()
			.getEventManager().registerListener(this);
	}

	@EventHandler
	public void onChangedStyleStateEvent(ChangedStyleStateEvent styleStateEvent) 
	{
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setBackground(styleStateEvent
						.getCustomStylePropertiesHolder()
						.getInnerBackgroundColor());
	}
	
	public JTableHeader getTableHeader() {
		return tableHeader;
	}

}
