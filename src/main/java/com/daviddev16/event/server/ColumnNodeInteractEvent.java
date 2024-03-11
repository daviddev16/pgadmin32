package com.daviddev16.event.server;

import javax.swing.tree.DefaultMutableTreeNode;

import com.daviddev16.core.component.TreeViewer;
import com.daviddev16.event.GenericTreeNodeInteractEvent;

/** Evento disparado quando ocorre interação com uma coluna na arvore da interface. */
public class ColumnNodeInteractEvent extends GenericTreeNodeInteractEvent {

	public ColumnNodeInteractEvent(TreeViewer parentTreeViewer, 
								   DefaultMutableTreeNode interactedTreeNode) {
		super(parentTreeViewer, interactedTreeNode);
	}

}
