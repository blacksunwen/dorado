/**
 * 
 */
package com.bstek.dorado.view.type.property.validator;

import java.lang.reflect.Method;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.common.service.ExposedService;
import com.bstek.dorado.common.service.ExposedServiceManager;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.bean.BeanFactoryUtils;
import com.bstek.dorado.util.method.MethodAutoMatchingUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-5-25
 */
@ViewObject(prototype = "dorado.validator.AjaxValidator", shortTypeName = "Ajax")
public class AjaxValidator extends AbstractAjaxValidator {
	private static ExposedServiceManager exposedServiceManager;

	private String service;

	private ExposedServiceManager getExposedServiceManager() throws Exception {
		if (exposedServiceManager == null) {
			exposedServiceManager = (ExposedServiceManager) Context
					.getCurrent().getServiceBean("exposedServiceManager");
		}
		return exposedServiceManager;
	}

	@ViewAttribute(outputter = "dorado.stringAliasPropertyOutputter")
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	@Override
	protected Object doValidate(Object value) throws Exception {
		ExposedService exposedService = getExposedServiceManager().getService(
				service);
		if (exposedService == null) {
			throw new IllegalArgumentException("Unknown ExposedService ["
					+ service + "].");
		}

		Object serviceBean = BeanFactoryUtils.getBean(exposedService
				.getBeanName());
		Method[] methods = MethodAutoMatchingUtils.getMethodsByName(
				serviceBean.getClass(), exposedService.getMethod());
		if (methods.length == 0) {
			throw new NoSuchMethodException("Method ["
					+ exposedService.getMethod() + "] not found in ["
					+ exposedService.getBeanName() + "].");
		}

		String[] optionalParameterNames = new String[] { "value" };
		Object[] optionalParameters = new Object[] { value };
		Object returnValue = MethodAutoMatchingUtils.invokeMethod(methods,
				serviceBean, null, null, optionalParameterNames,
				optionalParameters);
		return returnValue;
	}

}
