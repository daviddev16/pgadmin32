package com.daviddev16.node.group;

import com.daviddev16.core.annotation.Resourced;
import com.daviddev16.util.TextUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@Resourced(resourceIdentifier = "SequencesGroup16px")
public class SequencesGroup extends NodeGroup {
	
	private final String sequencesGroupIdentifier;

	public SequencesGroup() {
		sequencesGroupIdentifier 
			= TextUtil.createIdentifier(SequencesGroup.class);
	}

	@Override
	public String getNodeIdentifier() {
		return sequencesGroupIdentifier;
	}
	
	@Override
	public String getNodeName() {
		return TextUtil.bold("Sequences");
	}

}
