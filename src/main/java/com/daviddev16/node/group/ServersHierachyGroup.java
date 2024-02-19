package com.daviddev16.node.group;

import com.daviddev16.core.ResourcedEntityDataNode;
import com.daviddev16.core.annotation.Resourced;
import com.daviddev16.util.TextUtil;

import lombok.Getter;

@Getter
@Resourced(resourceIdentifier = "ServerHierachyGroup16px")
public class ServersHierachyGroup implements ResourcedEntityDataNode {

	private final String hierachyGroupIdentifier;
	
	public ServersHierachyGroup() {
		this.hierachyGroupIdentifier 
			= TextUtil.createIdentifier(ServersHierachyGroup.class);
	}

	@Override
	public String getNodeIdentifier() {
		return hierachyGroupIdentifier; 
	}
	
	@Override
	public String getNodeName() {
		return "Clusters";
	}

}
