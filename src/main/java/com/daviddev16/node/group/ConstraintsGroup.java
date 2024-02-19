package com.daviddev16.node.group;

import com.daviddev16.core.annotation.Resourced;
import com.daviddev16.util.TextUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Resourced(resourceIdentifier = "ConstraintsGroup16px")
public class ConstraintsGroup extends NodeGroup {
	
	private final String constraintsGroupIdentifier;
	
	public ConstraintsGroup() {
		constraintsGroupIdentifier 
			= TextUtil.createIdentifier(ConstraintsGroup.class);
	}
	
	@Override
	public String getNodeIdentifier() {
		return constraintsGroupIdentifier;
	}

	@Override
	public String getNodeName() {
		return "Constraints";
	}

}