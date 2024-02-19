package com.daviddev16.core.component;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import com.daviddev16.core.component.renderer.DefaultTableViewerRenderer;
import com.daviddev16.service.ServicesFacade;

public class TableViewer extends JTable {

	private static final long serialVersionUID = -1195934634556185056L;

	private final DefaultTableViewerRenderer tableCellRenderer;
	private boolean hasFirstFixedColumn;
	
	public TableViewer() {
		super();
		initializeTableViewerDefaults();
		tableCellRenderer = new DefaultTableViewerRenderer(ServicesFacade.getServices().getFileResourceLocator());
		setDefaultRenderer(Object.class, tableCellRenderer);
	}
	
	public void setHasFirstFixedColumn(boolean hasFirstFixedColumn) {
		this.hasFirstFixedColumn = hasFirstFixedColumn;
	}
	
	public boolean hasFirstFixedColumn() {
		return hasFirstFixedColumn;
	}
	
	private void initializeTableViewerDefaults() {
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setCellSelectionEnabled(true);
		setRowSelectionAllowed(true);	
	}
	
	
}
