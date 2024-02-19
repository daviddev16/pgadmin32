package com.daviddev16.node;

import com.daviddev16.core.DatabaseDataObject;
import com.daviddev16.core.annotation.Resourced;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Resourced(resourceIdentifier = "SequencesGroup16px")
public class Sequence extends DatabaseDataObject {
	
	private String sequenceName;
	private Integer sequencelastValue;
	
	@Override
	public String getNodeName() {
		return String.format("<html>[%d] %s</html>", sequencelastValue, sequenceName);
	}

	@Override
	public String getNodeIdentifier() {
		return sequenceName;
	}
}
