package com.bstek.dorado.vidorsupport.iapi;

import java.util.Collection;
import java.util.Map;

public interface IDataTypeWorkshop {

	final String GLOBAL_PREFIX = "global:";
	
	Collection<String> names(Map<String, Object> parameter, boolean containsBaseType) throws Exception;
	
	Collection<String> baseNames() throws Exception;
	
	String reflection(String json) throws Exception;
	
	String json(String name) throws Exception;
	
	String json(Map<String, String> jsonMap, String name) throws Exception;
}
