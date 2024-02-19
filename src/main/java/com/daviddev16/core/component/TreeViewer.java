package com.daviddev16.core.component;


import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;

import com.daviddev16.core.ResourceLocator;
import com.daviddev16.core.ResourcedEntityDataNode;
import com.daviddev16.service.ServicesFacade;

public abstract class TreeViewer extends JTree implements TreeCellRenderer {
	
	private static final long serialVersionUID = -4225588298845515448L;

	private final ResourceLocator resourceLocator = ServicesFacade.getServices().getFileResourceLocator();
	
	private final DefaultTreeModel treeModel;
	
	public TreeViewer() {
		super();
		treeModel = new DefaultTreeModel(null, true);
		setModel(treeModel);
		setCellRenderer(this);
		setEditable(true);
		initilize();
	}
	
	public abstract void initilize();
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object treeNode, boolean isSelected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		
		if (!(treeNode instanceof DefaultMutableTreeNode) || 
				!(((DefaultMutableTreeNode)treeNode).getUserObject() instanceof ResourcedEntityDataNode)) 
			return new JLabel("nonDisplayableBean");
		
		final ResourcedEntityDataNode nodeDataEntity = (ResourcedEntityDataNode)
				((DefaultMutableTreeNode)treeNode).getUserObject();
	
		return getLabelBasedOnResourcedEntityDataNode(nodeDataEntity, isSelected);
	}

	public JLabel getLabelBasedOnResourcedEntityDataNode(ResourcedEntityDataNode nodeDataEntity, boolean isSelected) {

		JLabel customRenderLabel = new JLabel();

		customRenderLabel.setText(nodeDataEntity.getNodeName());
		customRenderLabel.setForeground(getSelectionColorForeground(customRenderLabel, isSelected));
		customRenderLabel.setOpaque(false);
		
		customRenderLabel.setIcon(resourceLocator
				.cachedImageIcon(nodeDataEntity.getResourceIdentifier()));

		return customRenderLabel;
	}
	
	private Color getSelectionColorForeground(JLabel customRenderLabel, boolean isSelected) {
		if (isSelected)
			return UIManager.getColor("Tree.selectionForeground");
		else
			return UIManager.getColor("Tree.selectionInactiveForeground");
	}
	
	public DefaultMutableTreeNode createTreeNodeByEntityData(ResourcedEntityDataNode dataEntity) {
		return new DefaultMutableTreeNode(dataEntity);
	}
	
	public DefaultTreeModel getDefaultModel() {
		return treeModel;
	}

}
