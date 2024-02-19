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
@Resourced(resourceIdentifier = "IndexNode13px")
public class Index extends DatabaseDataObject {
	
	private String indexName;

	@Override
	public String getNodeName() {
		return indexName;
	}

	@Override
	public String getNodeIdentifier() {
		return indexName;
	}
}
