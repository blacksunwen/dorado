package com.bstek.dorado.vidorsupport.iapi;

import java.util.Collection;
import java.util.Map;

public interface IDataProviderWorkshop {

	Collection<String> names(Map<String, Object> parameter) throws Exception;
	
}
