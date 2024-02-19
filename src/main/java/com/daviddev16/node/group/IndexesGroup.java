package com.daviddev16.node.group;

import com.daviddev16.core.annotation.Resourced;
import com.daviddev16.util.TextUtil;

import lombok.ToString;

@ToString
@Resourced(resourceIdentifier = "IndexesGroup16px")
public class IndexesGroup extends NodeGroup {
	
	private final String indexesGroupIdentifier;
	
	public IndexesGroup() {
		indexesGroupIdentifier 
			= TextUtil.createIdentifier(IndexesGroup.class);
	}
	
	@Override
	public String getNodeIdentifier() {
		return indexesGroupIdentifier;
	}

	@Override
	public String getNodeName() {
		return "Indexes";
	}

}
