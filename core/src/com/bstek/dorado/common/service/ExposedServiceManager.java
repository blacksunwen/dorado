/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

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
