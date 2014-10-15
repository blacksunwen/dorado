package com.bstek.dorado.vidorsupport.internal;

import java.util.Collection;
import java.util.Map;

import com.bstek.dorado.data.provider.manager.DataProviderManager;
import com.bstek.dorado.vidorsupport.iapi.IDataProviderWorkshop;

public class DataProviderWorkshop implements IDataProviderWorkshop {

	private DataProviderManager dataProviderManager;
	
	public DataProviderManager getDataProviderManager() {
		return dataProviderManager;
	}
	public void setDataProviderManager(DataProviderManager dataProviderManager) {
		this.dataProviderManager = dataProviderManager;
	}

	public Collection<String> names(Map<String, Object> parameter) throws Exception {
		Collection<String> nameSet = dataProviderManager.getDataProviderDefinitionManager().getDefinitions().keySet();
		nameSet = Utils.filter(nameSet, parameter);
		return nameSet;
	}
}
