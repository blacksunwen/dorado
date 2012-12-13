/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.view.service;

import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
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
import com.bstek.dorado.core.resource.ResourceManager;
import com.bstek.dorado.core.resource.ResourceManagerUtils;
import com.bstek.dorado.data.JsonUtils;
import com.bstek.dorado.data.ParameterWrapper;
import com.bstek.dorado.data.method.MethodAutoMatchingException;
import com.bstek.dorado.data.method.MethodAutoMatchingUtils;
import com.bstek.dorado.data.method.MoreThanOneMethodsMatchsException;
import com.bstek.dorado.data.variant.MetaData;
import com.bstek.dorado.util.Assert;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.view.resolver.ViewServiceResolver;
import com.bstek.dorado.web.DoradoContext;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-11-30
 */
public class RemoteServiceProcessor extends DataServiceProcessorSupport {
	public static final String SERVICE_NAME_ATTRIBUTE = ViewServiceResolver.class
			.getName() + ".serviceName";

	private static final Log logger = LogFactory
			.getLog(RemoteServiceProcessor.class);
	private static final ResourceManager resourceManager = ResourceManagerUtils
			.get(RemoteServiceProcessor.class);

	private static final Object[] EMPTY_ARGS = new Object[0];
	private static final String[] EMPTY_NAMES = new String[0];
	private static final Class<?>[] EMPTY_TYPES = new Class[0];

	private static final class AbortException extends RuntimeException {
		private static final long serialVersionUID = 4378048486115786904L;
	};

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
		String methodName = exposedService.getMethod();
		Method[] methods = MethodAutoMatchingUtils.getMethodsByName(
				serviceBean.getClass(), methodName);
		if (methods.length == 0) {
			throw new NoSuchMethodException("Method [" + methodName
					+ "] not found in [" + exposedService.getBeanName() + "].");
		}

		Object returnValue = null;

		context.setAttribute(SERVICE_NAME_ATTRIBUTE, exposedService.getName());

		boolean methodInvoked = false;
		MethodAutoMatchingException[] exceptions = new MethodAutoMatchingException[4];
		int i = 0;
		try {
			try {
				returnValue = invokeByParameterName(serviceBean, methods,
						parameter, false);
				methodInvoked = true;
			} catch (MoreThanOneMethodsMatchsException e) {
				throw e;
			} catch (MethodAutoMatchingException e) {
				exceptions[i++] = e;
			} catch (AbortException e) {
				// do nothing
			}

			if (!methodInvoked) {
				try {
					returnValue = invokeByParameterName(serviceBean, methods,
							parameter, true);
					methodInvoked = true;
				} catch (MoreThanOneMethodsMatchsException e) {
					throw e;
				} catch (MethodAutoMatchingException e) {
					exceptions[i++] = e;
				} catch (AbortException e) {
					// do nothing
				}
			}

			if (!methodInvoked) {
				try {
					returnValue = invokeByParameterType(serviceBean, methods,
							parameter, false);
					methodInvoked = true;
				} catch (MoreThanOneMethodsMatchsException e) {
					throw e;
				} catch (MethodAutoMatchingException e) {
					exceptions[i++] = e;
				} catch (AbortException e) {
					// do nothing
				}
			}

			if (!methodInvoked) {
				try {
					returnValue = invokeByParameterType(serviceBean, methods,
							parameter, true);
					methodInvoked = true;
				} catch (MoreThanOneMethodsMatchsException e) {
					throw e;
				} catch (MethodAutoMatchingException e) {
					exceptions[i++] = e;
				} catch (AbortException e) {
					// do nothing
				}
			}
		} catch (MethodAutoMatchingException e) {
			exceptions[i++] = e;
		}

		if (methodInvoked) {
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
		} else {
			for (MethodAutoMatchingException e : exceptions) {
				if (e == null) {
					break;
				}
				logger.error(e.getMessage());
			}
			throw new IllegalArgumentException(resourceManager.getString(
					"common/noMatchingMethodError", serviceBean.getClass()
							.getName(), methodName));
		}
	}

	protected Object invokeByParameterName(Object serviceBean,
			Method[] methods, Object parameter, boolean disassembleParameter)
			throws MethodAutoMatchingException, Exception {
		Map<String, Object> sysParameter = null;
		if (parameter instanceof ParameterWrapper) {
			ParameterWrapper parameterWrapper = (ParameterWrapper) parameter;
			parameter = parameterWrapper.getParameter();
			sysParameter = parameterWrapper.getSysParameter();
		}

		if (disassembleParameter
				&& (parameter == null && !(parameter instanceof Map<?, ?>))) {
			throw new AbortException();
		}

		String[] parameterParameterNames = EMPTY_NAMES;
		Object[] parameterParameters = EMPTY_ARGS;
		if (parameter != null && parameter instanceof Map<?, ?>) {
			if (disassembleParameter) {
				Map<?, ?> map = (Map<?, ?>) parameter;
				parameterParameterNames = new String[map.size()];
				parameterParameters = new Object[parameterParameterNames.length];

				int i = 0;
				for (Map.Entry<?, ?> entry : map.entrySet()) {
					parameterParameterNames[i] = (String) entry.getKey();
					parameterParameters[i] = entry.getValue();
					i++;
				}
			} else {
				parameterParameterNames = new String[] { "parameter" };
				parameterParameters = new Object[] { parameter };
			}
		} else {
			parameterParameterNames = new String[] { "parameter" };
			parameterParameters = new Object[] { parameter };
		}

		String[] optionalParameterNames = new String[parameterParameterNames.length];
		Object[] optionalParameters = new Object[optionalParameterNames.length];
		System.arraycopy(parameterParameterNames, 0, optionalParameterNames, 0,
				parameterParameterNames.length);
		System.arraycopy(parameterParameters, 0, optionalParameters, 0,
				parameterParameters.length);

		String[] extraParameterNames = null;
		Object[] extraParameters = null;
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
			Method[] methods, Object parameter, boolean disassembleParameter)
			throws MethodAutoMatchingException, Exception {
		Map<String, Object> sysParameter = null;
		if (parameter instanceof ParameterWrapper) {
			ParameterWrapper parameterWrapper = (ParameterWrapper) parameter;
			parameter = parameterWrapper.getParameter();
			sysParameter = parameterWrapper.getSysParameter();
		}

		if (disassembleParameter
				&& (parameter == null && !(parameter instanceof Map<?, ?>))) {
			throw new AbortException();
		}

		Type[] optionalParameterTypes = EMPTY_TYPES;
		Object[] optionalParameters = EMPTY_ARGS;
		if (parameter != null) {
			if (parameter instanceof Map<?, ?>) {
				if (disassembleParameter) {
					Map<?, ?> map = (Map<?, ?>) parameter;
					optionalParameterTypes = new Class[map.size()];
					optionalParameters = new Object[optionalParameterTypes.length];

					int i = 0;
					for (Object value : map.values()) {
						if (value != null) {
							optionalParameterTypes[i] = MethodAutoMatchingUtils
									.getTypeForMatching(value);
							optionalParameters[i] = value;
							i++;
						}
					}
				} else {
					optionalParameterTypes = new Type[] { MethodAutoMatchingUtils
							.getTypeForMatching(parameter) };
					optionalParameters = new Object[] { parameter };
				}
			} else {
				optionalParameterTypes = new Type[] { MethodAutoMatchingUtils
						.getTypeForMatching(parameter) };
				optionalParameters = new Object[] { parameter };
			}
		} else {
			optionalParameterTypes = new Type[] { Object.class };
			optionalParameters = new Object[] { null };
		}

		Type[] exactArgTypes = null;
		Object[] exactArgs = null;
		Map<Type, Object> extraArgMap = new HashMap<Type, Object>();
		if (sysParameter != null && !sysParameter.isEmpty()) {
			for (Map.Entry<?, ?> entry : sysParameter.entrySet()) {
				Object value = entry.getValue();
				if (value != null) {
					extraArgMap.put(
							MethodAutoMatchingUtils.getTypeForMatching(value),
							value);
				}
			}

			if (!extraArgMap.isEmpty()) {
				exactArgTypes = new Class[extraArgMap.size()];
				exactArgs = new Object[exactArgTypes.length];
				int i = 0;
				for (Map.Entry<?, ?> entry : extraArgMap.entrySet()) {
					exactArgTypes[i] = (Class<?>) entry.getKey();
					exactArgs[i] = entry.getValue();
					i++;
				}
			}
		}

		return MethodAutoMatchingUtils.invokeMethod(methods, serviceBean, null,
				null, exactArgTypes, exactArgs, optionalParameterTypes,
				optionalParameters, null);
	}

}
