package com.bstek.dorado.common.service;

import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.collections.map.UnmodifiableMap;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-11-29
 */
public class ExposedServiceManager {
	private Map<String, ExposedService> serviceMap = new Hashtable<String, ExposedService>();

	public void registerService(ExposedService exposedService) {
		serviceMap.put(exposedService.getName(), exposedService);
	}

	public ExposedService getService(String name) {
		return serviceMap.get(name);
	}

	@SuppressWarnings("unchecked")
	public Map<String, ExposedService> getServices() {
		return UnmodifiableMap.decorate(serviceMap);
	}
}
