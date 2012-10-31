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
package com.bstek.dorado.view.service;

import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.type.TypeReference;

import com.bstek.dorado.common.service.ExposedService;
import com.bstek.dorado.common.service.ExposedServiceManager;
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.bean.BeanFactoryUtils;
import com.bstek.dorado.data.JsonUtils;
import com.bstek.dorado.data.ParameterWrapper;
import com.bstek.dorado.data.method.MethodAutoMatchingException;
import com.bstek.dorado.data.method.MethodAutoMatchingUtils;
import com.bstek.dorado.data.method.MoreThanOneMethodsMatchsException;
import com.bstek.dorado.data.variant.MetaData;
import com.bstek.dorado.util.Assert;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.web.DoradoContext;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-11-30
 */
public class RemoteServiceProcessor extends DataServiceProcessorSupport {
	private static final Log logger = LogFactory
			.getLog(RemoteServiceProcessor.class);
	private ExposedServiceManager exposedServiceManager;

	public void setExposedServiceManager(
			ExposedServiceManager exposedServiceManager) {
		this.exposedServiceManager = exposedServiceManager;
	}

	@Override
	protected void doExecute(Writer writer, ObjectNode objectNode,
			DoradoContext context) throws Exception {
		String serviceAlias = JsonUtils.getString(objectNode, "service");
		Assert.notEmpty(serviceAlias);

		// String serviceName =
		// StringAliasUtils.getOriginalString(serviceAlias);
		String serviceName = serviceAlias;
		if (serviceName == null) {
			throw new IllegalArgumentException("Invalid ServiceAlias ["
					+ serviceAlias + "].");
		}
		ExposedService exposedService = exposedServiceManager
				.getService(serviceName);
		if (exposedService == null) {
			throw new IllegalArgumentException("Unknown ExposedService ["
					+ serviceName + "].");
		}

		Object parameter = jsonToJavaObject(objectNode.get("parameter"), null,
				null, false);
		MetaData sysParameter = (MetaData) jsonToJavaObject(
				objectNode.get("sysParameter"), null, null, false);

		if (sysParameter != null && !sysParameter.isEmpty()) {
			parameter = new ParameterWrapper(parameter, sysParameter);
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

		Object returnValue;
		try {
			returnValue = invokeByParameterName(serviceBean, methods, parameter);
		} catch (MoreThanOneMethodsMatchsException e) {
			throw e;
		} catch (MethodAutoMatchingException e1) {
			try {
				returnValue = invokeByParameterType(serviceBean, methods,
						parameter);
			} catch (MethodAutoMatchingException e2) {
				logger.error(e2, e2);
				throw e1;
			}
		}

		OutputContext outputContext = new OutputContext(writer);
		boolean supportsEntity = JsonUtils.getBoolean(objectNode,
				"supportsEntity");
		if (supportsEntity) {
			List<String> loadedDataTypes = JsonUtils.get(objectNode,
					"loadedDataTypes", new TypeReference<List<String>>() {
					});
			outputContext.setLoadedDataTypes(loadedDataTypes);
		}
		outputContext.setUsePrettyJson(Configure
				.getBoolean("view.outputPrettyJson"));
		outputContext.setShouldOutputDataTypes(supportsEntity);

		outputResult(returnValue, outputContext);
	}

	protected Object invokeByParameterName(Object serviceBean,
			Method[] methods, Object parameter)
			throws MethodAutoMatchingException, Exception {
		Map<String, Object> sysParameter = null;
		if (parameter instanceof ParameterWrapper) {
			ParameterWrapper parameterWrapper = (ParameterWrapper) parameter;
			parameter = parameterWrapper.getParameter();
			sysParameter = parameterWrapper.getSysParameter();
		}

		String[] parameterParameterNames = null;
		Object[] parameterParameters = null;
		String[] extraParameterNames = null;
		Object[] extraParameters = null;

		if (parameter != null && parameter instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) parameter;
			parameterParameterNames = new String[map.size() + 1];
			parameterParameters = new Object[parameterParameterNames.length];
			parameterParameterNames[0] = "parameter";
			parameterParameters[0] = parameter;

			int i = 1;
			for (Map.Entry<?, ?> entry : map.entrySet()) {
				parameterParameterNames[i] = (String) entry.getKey();
				parameterParameters[i] = entry.getValue();
				i++;
			}
		} else if (parameter != null) {
			parameterParameterNames = new String[] { "parameter" };
			parameterParameters = new Object[] { parameter };
		} else {
			parameterParameterNames = new String[0];
			parameterParameters = new Object[0];
		}

		String[] optionalParameterNames = new String[parameterParameterNames.length];
		Object[] optionalParameters = new Object[optionalParameterNames.length];
		System.arraycopy(parameterParameterNames, 0, optionalParameterNames, 0,
				parameterParameterNames.length);
		System.arraycopy(parameterParameters, 0, optionalParameters, 0,
				parameterParameters.length);

		if (sysParameter != null && !sysParameter.isEmpty()) {
			extraParameterNames = new String[sysParameter.size()];
			extraParameters = new Object[extraParameterNames.length];

			int i = 0;
			for (Map.Entry<?, ?> entry : sysParameter.entrySet()) {
				extraParameterNames[i] = (String) entry.getKey();
				extraParameters[i] = entry.getValue();
				i++;
			}
		}

		return MethodAutoMatchingUtils.invokeMethod(methods, serviceBean, null,
				null, optionalParameterNames, optionalParameters,
				extraParameterNames, extraParameters);
	}

	protected Object invokeByParameterType(Object serviceBean,
			Method[] methods, Object parameter)
			throws MethodAutoMatchingException, Exception {
		Type[] optionalParameterTypes = null;
		Object[] optionalParameters = null;
		if (parameter != null && parameter instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) parameter;
			optionalParameterTypes = new Class<?>[map.size() + 1];
			optionalParameters = new Object[optionalParameterTypes.length];
			optionalParameterTypes[0] = Map.class;
			optionalParameters[0] = parameter;

			int i = 1;
			for (Object value : map.values()) {
				if (value != null) {
					optionalParameterTypes[i] = MethodAutoMatchingUtils
							.getTypeForMatching(value);
					optionalParameters[i] = value;
					i++;
				}
			}
		} else if (parameter != null) {
			optionalParameterTypes = new Type[] { MethodAutoMatchingUtils
					.getTypeForMatching(parameter) };
			optionalParameters = new Object[] { parameter };
		} else {
			optionalParameterTypes = new Type[] { Object.class };
			optionalParameters = new Object[] { null };
		}

		return MethodAutoMatchingUtils.invokeMethod(methods, serviceBean, null,
				null, null, null, optionalParameterTypes, optionalParameters,
				null);
	}

}
