package com.bstek.dorado.common.service;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.bstek.dorado.util.Assert;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-1-21
 */
public class ExposedServiceRegister implements BeanFactoryPostProcessor {
	private ExposedServiceManager exposedServiceManager;
	private List<String> services;

	public void setExposedServiceManager(
			ExposedServiceManager exposedServiceManager) {
		this.exposedServiceManager = exposedServiceManager;
	}

	public void setServices(List<String> services) {
		this.services = services;
	}

	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		if (services != null) {
			synchronized (exposedServiceManager) {
				for (String service : services) {
					String bean, method = null;

					Assert.notEmpty(service);
					int i = service.lastIndexOf('#');
					if (i > 0) {
						bean = service.substring(0, i);
						method = service.substring(i + 1);
					}
					else {
						bean = service;
					}
					exposedServiceManager.registerService(new ExposedService(
							service, bean, method));
				}
			}
		}
	}
}
