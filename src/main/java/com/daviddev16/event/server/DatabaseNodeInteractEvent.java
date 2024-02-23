package com.daviddev16.event.server;

import javax.swing.tree.DefaultMutableTreeNode;

import com.daviddev16.core.component.TreeViewer;
import com.daviddev16.event.GenericTreeNodeInteractEvent;

/** Evento disparado quando ocorre interação com um banco de dados na arvore da interface. */
public class DatabaseNodeInteractEvent extends GenericTreeNodeInteractEvent {

	public DatabaseNodeInteractEvent(TreeViewer parentTreeViewer, 
								     DefaultMutableTreeNode interactedTreeNode) {
		super(parentTreeViewer, interactedTreeNode);
	}

}
