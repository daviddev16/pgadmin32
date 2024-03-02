package com.daviddev16.entity;


import java.util.HashMap;
import java.util.Map;

import com.daviddev16.core.ResourcedEntityDataNode;
import com.daviddev16.core.annotation.Resourced;
import com.daviddev16.core.data.TableProperty;

@Resourced(resourceIdentifier = "Property16px")
public class TableResourcedProperty implements ResourcedEntityDataNode {

	final Map<String, String> tableDescriptionMap = new HashMap<String, String>();
	{
		tableDescriptionMap.put("oid",     				  "Identificaodr OID da tabela");
		tableDescriptionMap.put("relname", 				  "Nome da tabela");
		tableDescriptionMap.put("nspname", 				  "Nome do schema da tabela");
		tableDescriptionMap.put("physical_location_path", "Localização física da tabela");
		tableDescriptionMap.put("total_size", 			  "Tamanho total da relação");
		tableDescriptionMap.put("toast_name", 			  "Nome da tabela toast relacionada");
		tableDescriptionMap.put("toast_size", 			  "Tamanho da tabela toast relacionada");
		tableDescriptionMap.put("reltoastrelid",          "Identificador OID da tabela toast relacionada");
		tableDescriptionMap.put("table_size", 			  "Tamanho da tabela");
		tableDescriptionMap.put("rolname",   			  "Dono");
	}

	private String tablePropertyDescription;
	
	public TableResourcedProperty(TableProperty tableProperty) {
		String propertyName = tableProperty.getPropertyName();
		if (propertyName != null) {
			tablePropertyDescription = tableDescriptionMap.getOrDefault(propertyName, propertyName);
		} else {
			propertyName = "<unknown table property>";
		}
	}

	@Override
	public String getNodeName() {
		return tablePropertyDescription;
	}

	@Override
	public String getNodeIdentifier() {
		return null;
	}
}
