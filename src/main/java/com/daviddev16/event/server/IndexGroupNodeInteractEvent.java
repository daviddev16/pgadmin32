package com.daviddev16.event.server;

import javax.swing.tree.DefaultMutableTreeNode;

import com.daviddev16.core.component.TreeViewer;
import com.daviddev16.event.GenericTreeNodeInteractEvent;

/** Evento disparado quando ocorre interação com um grupo de indexes na arvore da interface. */
public class IndexGroupNodeInteractEvent extends GenericTreeNodeInteractEvent {

	public IndexGroupNodeInteractEvent(TreeViewer parentTreeViewer, 
								       DefaultMutableTreeNode interactedTreeNode) {
		super(parentTreeViewer, interactedTreeNode);
	}

}
