/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
package com.bstek.dorado.common.service;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import com.bstek.dorado.util.Assert;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-1-21
 */
public class ExposedServiceRegister implements InitializingBean {
	private ExposedServiceManager exposedServiceManager;
	private Map<String, String> services;

	public void setExposedServiceManager(
			ExposedServiceManager exposedServiceManager) {
		this.exposedServiceManager = exposedServiceManager;
	}

	public void setServices(Map<String, String> services) {
		this.services = services;
	}

	public void afterPropertiesSet() throws Exception {
		if (services != null) {
			synchronized (exposedServiceManager) {
				for (Map.Entry<String, String> entry : services.entrySet()) {
					String name = entry.getKey(), service = entry.getValue();
					Assert.notEmpty(name);
					Assert.notEmpty(service);

					String bean, method = null;
					int i = service.lastIndexOf('#');
					if (i > 0) {
						bean = service.substring(0, i);
						method = service.substring(i + 1);
					} else {
						bean = service;
					}
					exposedServiceManager.registerService(new ExposedService(
							name, bean, method));
				}
			}
		}
	}
}
