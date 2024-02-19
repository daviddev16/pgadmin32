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
@Resourced(resourceIdentifier = "Column16px")
public class Column extends DatabaseDataObject {
	
	private String columnName;
	private Integer columnDataTypeLength;
	private String columnTypeName;
	private String columnDefaultDefinition;
	private boolean isNullable;

	@Override
	public String getNodeName() {

		StringBuilder builder = new StringBuilder()
				.append("<html>")
				.append(columnName)
				.append(" - <i>")
				.append(columnTypeName)
				.append(" ");

		if (columnDataTypeLength != null && columnDataTypeLength > 0)
			builder.append("(").append(columnDataTypeLength).append(")");
		
		builder.append("</i></html>");
		return builder.toString();
	}

	@Override
	public String getNodeIdentifier() {
		return columnName;
	}
}
