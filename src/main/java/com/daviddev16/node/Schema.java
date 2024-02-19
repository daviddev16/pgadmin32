package com.daviddev16.node;

import com.daviddev16.core.DatabaseDataObject;

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
public class Schema extends DatabaseDataObject {

	public enum SchemaType { CATALOG, COMMON }
	
	private String schemaName;
	private SchemaType schemaType;
	
	@Override
	public String getNodeName() {
		if (schemaName.equals("pg_catalog") || schemaName.equals("information_schema")) {
			return String.format("<html>%s <i>(Catálogo)</i></html>", schemaName);
		}
		return schemaName;
	}
	
	@Override
	public String getResourceIdentifier() {
		return (schemaType == SchemaType.CATALOG) ? "Catalog10px" : "Schema10px";
	}
	
	@Override
	public String getNodeIdentifier() {
		return schemaName;
	}


}
