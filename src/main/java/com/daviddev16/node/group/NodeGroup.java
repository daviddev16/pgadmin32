package com.daviddev16.node.group;

import com.daviddev16.core.DatabaseDataObject;
import com.daviddev16.core.NodeState;

public abstract class NodeGroup extends DatabaseDataObject {
	
	/* 
	 * by default NodeGroup should be Stateless 
	 */
	{ setNodeState(NodeState.STATELESS); }
	
}
