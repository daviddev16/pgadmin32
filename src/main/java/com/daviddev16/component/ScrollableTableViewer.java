package com.daviddev16.component;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;

import com.daviddev16.core.component.TableViewer;

public class ScrollableTableViewer extends JScrollPane {
	
	private static final long serialVersionUID = -8948317281462252879L;

	private JTableHeader tableHeader;
	
	public ScrollableTableViewer(final TableViewer tableViewer) {
		tableViewer.setFillsViewportHeight(true);
		tableViewer.setMinimumSize(new Dimension(2000, 100));
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		setBackground(UIManager.getColor("Style.innerComponentBackgroundColor"));
		setViewportView(tableViewer);
		setBorder(new EmptyBorder(0, 0, 0, 0));
	}

	public JTableHeader getTableHeader() {
		return tableHeader;
	}
	

}
