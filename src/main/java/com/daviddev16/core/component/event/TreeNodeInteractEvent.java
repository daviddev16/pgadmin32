package com.daviddev16.core.component.event;

import javax.swing.tree.DefaultMutableTreeNode;

import com.daviddev16.core.component.TreeViewer;
import com.daviddev16.core.event.AbstractCancellableEvent;

public abstract class TreeNodeInteractEvent extends AbstractCancellableEvent {

	private TreeNodeInteractionType treeNodeInteractionType = TreeNodeInteractionType.UNKNOWN_EVENT;
	private final DefaultMutableTreeNode interactedTreeNode;
	private final TreeViewer treeViewer;

	public TreeNodeInteractEvent(TreeViewer treeViewer, 
								 DefaultMutableTreeNode interactedTreeNode) {
		this.treeViewer = treeViewer;
		this.interactedTreeNode = interactedTreeNode;
	}

	public void setTreeNodeInteractionType(TreeNodeInteractionType treeNodeInteractionType) {
		this.treeNodeInteractionType = treeNodeInteractionType;
	}
	
	public TreeNodeInteractionType getTreeNodeInteractionType() {
		return treeNodeInteractionType;
	}

	public DefaultMutableTreeNode getInteractedTreeNode() {
		return interactedTreeNode;
	}
	
	public TreeViewer getTreeViewer() {
		return treeViewer;
	}
	
	@Override
	public Object getSender() {
		return treeViewer;
	}
	
}
