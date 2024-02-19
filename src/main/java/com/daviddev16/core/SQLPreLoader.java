package com.daviddev16.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SQLPreLoader {
	
	private String preloaderName;
	private String preloaderSqlScript;

	@Override
	public String toString() {
		return "SQLPreLoader [preloaderName=" + preloaderName + "]";
	}
	
}
