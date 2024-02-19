package com.daviddev16.node.group;

import com.daviddev16.node.Schema.SchemaType;
import com.daviddev16.util.TextUtil;

public class SchemaGroup extends NodeGroup {

	private final SchemaType schemaType;
	private final String schemaGroupIdentifier;
	
	private String schemaTypeName;
	private String resourceIdentifier;

	public SchemaGroup(SchemaType schemaType) {
		this.schemaType = schemaType;
		if (schemaType == SchemaType.CATALOG) {
			schemaTypeName = "Catalog";
			resourceIdentifier = "Catalog10px";
		} 
		else if (schemaType == SchemaType.COMMON) {
			schemaTypeName = "Schema";
			resourceIdentifier = "Schema10px";
		}
		this.schemaGroupIdentifier 
			= TextUtil.createIdentifier(SchemaGroup.class);
	}
	
	@Override
	public String getNodeIdentifier() {
		return schemaGroupIdentifier;
	}

	@Override
	public String getResourceIdentifier() {
		return resourceIdentifier;
	}
	
	@Override
	public String getNodeName() {
		return schemaTypeName;
	}
	
	public SchemaType getSchemaType() {
		return schemaType;
	}

}
