package com.daviddev16.core;


public interface DataColletorPreLoaderQueries {

	String getCreateOfFunctionSequenceLastValueSQL();

	default String[] getAllPreLoadersSQL() {
		return new String[] 
		{ getCreateOfFunctionSequenceLastValueSQL() };
	}
	
}
