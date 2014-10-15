package com.bstek.dorado.vidorsupport.internal;

import java.util.Collection;
import java.util.Map;

import com.bstek.dorado.data.resolver.manager.DataResolverManager;
import com.bstek.dorado.vidorsupport.iapi.IDataResolverWorkshop;

public class DataResolverWorkshop implements IDataResolverWorkshop {

	private DataResolverManager dataResolverManager;
	
	public DataResolverManager getDataResolverManager() {
		return dataResolverManager;
	}
	public void setDataResolverManager(DataResolverManager dataResolverManager) {
		this.dataResolverManager = dataResolverManager;
	}


	public Collection<String> names(Map<String, Object> parameter) throws Exception {
		Collection<String> nameSet = dataResolverManager.getDataResolverDefinitionManager().getDefinitions().keySet();
		nameSet = Utils.filter(nameSet, parameter);
		return nameSet;
	}

}
