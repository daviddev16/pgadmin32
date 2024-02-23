package com.daviddev16.event;

import javax.swing.tree.DefaultMutableTreeNode;

import com.daviddev16.core.component.TreeViewer;
import com.daviddev16.core.component.event.TreeNodeInteractEvent;

public abstract class GenericTreeNodeInteractEvent extends TreeNodeInteractEvent {

	public GenericTreeNodeInteractEvent(TreeViewer parentTreeViewer, 
								    	DefaultMutableTreeNode interactedTreeNode) {
		super(parentTreeViewer, interactedTreeNode);
	}

}
