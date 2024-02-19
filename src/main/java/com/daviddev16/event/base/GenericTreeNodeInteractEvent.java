package com.daviddev16.event.base;

import javax.swing.tree.DefaultMutableTreeNode;

import com.daviddev16.core.component.TreeViewer;
import com.daviddev16.core.component.event.TreeNodeInteractEvent;

public class GenericTreeNodeInteractEvent extends TreeNodeInteractEvent {

	public GenericTreeNodeInteractEvent(TreeViewer parentTreeViewer, 
								    	DefaultMutableTreeNode interactedTreeNode) {
		super(parentTreeViewer, interactedTreeNode);
	}

}
