package com.daviddev16.core.component;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;

class BaseDataSetTable extends JTable {

	private static final long serialVersionUID = 1220854221214338451L;

	public BaseDataSetTable() {
		super();
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setCellSelectionEnabled(true);
		setRowSelectionAllowed(true);		
		setDefaultRenderer(Object.class, new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				JLabel jLabel = new JLabel(value != null ? value.toString() : "");
				if (hasFocus) {
					if (column > 0) { 
						jLabel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(5, 5, 5, 5)));
						jLabel.setBackground(new Color(240, 240, 240));
						jLabel.setOpaque(true);
					}
				} else {
					jLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
				}
				if (column == 0) {
					jLabel.setHorizontalAlignment(SwingConstants.CENTER);
					jLabel.setHorizontalTextPosition(SwingConstants.CENTER);
				}
				return jLabel;
			}
		});
	}



}
