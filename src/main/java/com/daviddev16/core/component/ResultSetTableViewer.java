package com.daviddev16.core.component;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public final class ResultSetTableViewer extends TableViewer {

	private static final long serialVersionUID = 6356664162208875101L;

	public ResultSetTableViewer() { super(); }
	
	public void setResultSet(final ResultSet resultSet) {
		try {
			setHasFirstFixedColumn(true);
			final DefaultTableModel tableModel = new DefaultTableModel() {
				private static final long serialVersionUID = -6901805822459648889L;
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			setColumnModel(new DefaultTableColumnModel());
			setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			Vector<String> columnIdentifier = new Vector<String>(resultSetMetaData.getColumnCount() + 2);
			columnIdentifier.add("");
			int[] widths = new int[resultSetMetaData.getColumnCount()];
			for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
				String columnName = resultSetMetaData.getColumnName(i);
				columnName = String.format("<html>%s<br>%s</html>", (columnName != null ? columnName.toString() : ""), "defined type");
				columnIdentifier.add(columnName);
				int fontStrWidth = getFontMetrics(getFont()).stringWidth(columnName) + 30;
				if (widths[i - 1] < fontStrWidth) {
					widths[i - 1] = fontStrWidth;
				}
			}
			tableModel.setColumnIdentifiers(columnIdentifier);
			int contador = 0;
			while (resultSet.next()) {
				Vector<Object> dataVector = new Vector<Object>(resultSetMetaData.getColumnCount() + 2);
				dataVector.add(""+contador);
				contador++;
				for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
					Object dataObject = resultSet.getObject(i);
					if (dataObject == null) {
						dataObject = "";
					}
					if (dataObject instanceof Clob || dataObject instanceof Blob) {
						dataObject = "<binary data>";
					}
					dataVector.add(dataObject);
					int fontStrWidth = Math.min(getFontMetrics(getFont()).stringWidth(dataObject.toString()) + 30, 80);
					if (widths[i - 1] < fontStrWidth) {
						widths[i - 1] = fontStrWidth;
					}
				}
				tableModel.addRow(dataVector.toArray());
			}
			setModel(tableModel);
			TableColumn column = null;
			for (int i = 0; i < getColumnModel().getColumnCount(); i++) {
				column = getColumnModel().getColumn(i);
				if (i == 0) {
					column.setPreferredWidth(60);
					column.setMinWidth(60);
					column.setMaxWidth(60);
				} else {
					column.setPreferredWidth(widths[i - 1]);
				}
			}    
//			getTableHeader().setPreferredSize(new Dimension(getTableHeader().getWidth(),45));
			//setAutoCreateRowSorter(true);
			//TableRowSorter<?> rowSorter = (TableRowSorter<?>) getRowSorter();
			//rowSorter.setSortsOnUpdates(true);
			revalidate();
			System.gc();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
