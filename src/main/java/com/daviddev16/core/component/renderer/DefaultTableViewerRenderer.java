package com.daviddev16.core.component.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;

import com.daviddev16.core.ResourceLocator;
import com.daviddev16.core.ResourcedEntityDataNode;
import com.daviddev16.core.component.TableViewer;

public class DefaultTableViewerRenderer implements TableCellRenderer {

	public static final Border EMPTY_INSETS_5_BORDER = new EmptyBorder(5, 5, 5, 5);
	public static final Border CELL_BORDER = new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(4, 4, 4, 4));
	public static final Color C1 = new Color(240, 240, 240);
	
	private final ResourceLocator resourceLocator;
	
	public DefaultTableViewerRenderer(ResourceLocator resourceLocator) {
		this.resourceLocator = resourceLocator;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JLabel componentLabel = new JLabel();
		if (value instanceof ResourcedEntityDataNode) {
			ResourcedEntityDataNode resourcedEntityDataNode = ((ResourcedEntityDataNode)value);
			componentLabel.setText(resourcedEntityDataNode.getNodeName());
			String resourceIdentifier = resourcedEntityDataNode.getResourceIdentifier();
			ImageIcon resourceImageIcon = getResourceLocator().cachedImageIcon(resourceIdentifier);
			componentLabel.setIcon(resourceImageIcon);
		} else {
			String valueString = (value != null) ? value.toString() : "?";
			componentLabel.setText(valueString);
		}
		componentLabel.setBorder(EMPTY_INSETS_5_BORDER);
		if (hasFocus) {
			if (column == 0 && ((TableViewer)table).hasFirstFixedColumn())
			{
			} 
			else {
				componentLabel.setBorder(CELL_BORDER);
				componentLabel.setBackground(C1);
				componentLabel.setOpaque(true);
			}
		}
		if (column == 0) {
			componentLabel.setHorizontalAlignment(SwingConstants.LEFT);
			componentLabel.setIconTextGap(10);
			componentLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		}
		return componentLabel;
	}

	public ResourceLocator getResourceLocator() {
		return resourceLocator;
	}
	
}
