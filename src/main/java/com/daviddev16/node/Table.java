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
@Resourced(resourceIdentifier = "Table16px")
public class Table extends DatabaseDataObject {

	private String tableName;
	
	@Override
	public String getNodeName() {
		return tableName;
	}

	@Override
	public String getNodeIdentifier() {
		return tableName;
	}


}
