package com.bstek.dorado.vidorsupport.iapi;

import java.util.Collection;
import java.util.Map;

public interface IExposedServiceWorkshop {

	Collection<String> names(Map<String, Object> parameter) throws Exception;
	
}
