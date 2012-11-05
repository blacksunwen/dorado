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

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;

import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.annotation.Unexpose;
import com.bstek.dorado.core.EngineStartupListener;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-11-29
 */
public class ExposedServiceAnnotationBeanPostProcessor extends
		EngineStartupListener implements MergedBeanDefinitionPostProcessor {
	private static final Log logger = LogFactory
			.getLog(ExposedServiceAnnotationBeanPostProcessor.class);

	private ExposedServiceManager exposedServiceManager;
	private Set<PendingDataObject> pendingDataObjects = new HashSet<PendingDataObject>();

	public void setExposedServiceManager(
			ExposedServiceManager exposedServiceManager) {
		this.exposedServiceManager = exposedServiceManager;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void postProcessMergedBeanDefinition(
			RootBeanDefinition beanDefinition, Class beanType, String beanName) {
		boolean defaultExposed = (beanType.getAnnotation(Expose.class) != null)
				&& (beanType.getAnnotation(Unexpose.class) == null);

		for (Method method : beanType.getMethods()) {
			Expose annotation = method.getAnnotation(Expose.class);
			boolean exposed = defaultExposed
					|| ((annotation != null) && (beanType
							.getAnnotation(Unexpose.class) == null));
			if (!exposed) {
				continue;
			}
			pendingDataObjects.add(new PendingDataObject(annotation, beanName,
					method.getName()));
		}
	}

	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

	private String autoRegisterExposedServices(
			PendingDataObject pendingDataObject) throws Exception {
		String beanName = pendingDataObject.getBeanName();
		String methodName = pendingDataObject.getMethodName();

		String serviceBeanName = "spring:" + beanName;
		String serviceName = beanName + '#' + methodName;
		ExposedService service = new ExposedService(serviceName,
				serviceBeanName, methodName);
		exposedServiceManager.registerService(service);
		return serviceName;
	}

	@Override
	public void onStartup() throws Exception {
		StringBuffer servicesText = new StringBuffer();
		for (PendingDataObject pendingDataObject : pendingDataObjects) {
			String serviceName = autoRegisterExposedServices(pendingDataObject);
			if (StringUtils.isNotEmpty(serviceName)) {
				if (servicesText.length() > 0) {
					servicesText.append(',');
				}
				servicesText.append(serviceName);
			}
		}
		pendingDataObjects.clear();

		if (servicesText.length() > 0) {
			logger.info("Registered ExposedService(via Annotation): ["
					+ servicesText + "]");
		}
	}
}

class PendingDataObject {
	private Expose annotation;
	private String beanName;
	private String methodName;
	private String uniqueName;

	public PendingDataObject(Expose annotation, String beanName,
			String methodName) {
		this.annotation = annotation;
		this.beanName = beanName;
		this.methodName = methodName;
		uniqueName = beanName + '#' + methodName;
	}

	public Expose getAnnotation() {
		return annotation;
	}

	public String getBeanName() {
		return beanName;
	}

	public String getMethodName() {
		return methodName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof PendingDataObject))
			return false;
		return (uniqueName != null) ? uniqueName
				.equals(((PendingDataObject) obj).uniqueName) : super
				.equals(obj);
	}

	@Override
	public int hashCode() {
		return (uniqueName != null) ? uniqueName.hashCode() : super.hashCode();
	}
}
