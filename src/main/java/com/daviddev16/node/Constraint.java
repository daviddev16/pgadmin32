package com.daviddev16.node;

import com.daviddev16.core.DatabaseDataObject;
import com.daviddev16.core.annotation.Resourced;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@Resourced(resourceIdentifier = "ConstraintNode13px")
public class Constraint extends DatabaseDataObject {
	
	private String constraintName;

	@Override
	public String getNodeName() {
		return constraintName;
	}

	@Override
	public String getNodeIdentifier() {
		return constraintName;
	}
}
