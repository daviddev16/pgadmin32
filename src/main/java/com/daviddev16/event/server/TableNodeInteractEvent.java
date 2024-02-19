package com.daviddev16.event.server;

import javax.swing.tree.DefaultMutableTreeNode;

import com.daviddev16.core.component.TreeViewer;
import com.daviddev16.event.base.GenericTreeNodeInteractEvent;

/** Evento disparado quando ocorre interação com uma tabela na arvore da interface. */
public class TableNodeInteractEvent extends GenericTreeNodeInteractEvent {

	public TableNodeInteractEvent(TreeViewer parentTreeViewer, 
								   DefaultMutableTreeNode interactedTreeNode) {
		super(parentTreeViewer, interactedTreeNode);
	}

}
