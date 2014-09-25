package com.bstek.dorado.vidorsupport.impl;

import java.util.Collection;
import java.util.Map;

import com.bstek.dorado.common.service.ExposedServiceManager;
import com.bstek.dorado.vidorsupport.iapi.IExposedServiceWorkshop;

public class ExposedServiceWorkshop implements
	IExposedServiceWorkshop {
	
	private ExposedServiceManager exposedServiceManager;

	public ExposedServiceManager getExposedServiceManager() {
		return exposedServiceManager;
	}
	public void setExposedServiceManager(ExposedServiceManager exposedServiceManager) {
		this.exposedServiceManager = exposedServiceManager;
	}

	@Override
	public Collection<String> names(Map<String, Object> parameter) throws Exception {
		Collection<String> names = exposedServiceManager.getServices().keySet();
		names = Utils.filter(names, parameter);
		return names;
	}

}
