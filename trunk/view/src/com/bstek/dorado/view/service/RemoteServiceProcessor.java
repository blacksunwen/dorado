package com.bstek.dorado.view.service;

import java.io.Writer;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.type.TypeReference;

import com.bstek.dorado.common.method.MethodAutoMatchingException;
import com.bstek.dorado.common.method.MethodAutoMatchingUtils;
import com.bstek.dorado.common.service.ExposedService;
import com.bstek.dorado.common.service.ExposedServiceManager;
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.bean.BeanFactoryUtils;
import com.bstek.dorado.data.JsonUtils;
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
		String[] optionalParameterNames = null;
		Object[] optionalParameters = null;
		if (parameter != null && parameter instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) parameter;
			optionalParameterNames = new String[map.size() + 1];
			optionalParameters = new Object[optionalParameterNames.length];
			optionalParameterNames[0] = "parameter";
			optionalParameters[0] = parameter;

			int i = 1;
			for (Map.Entry<?, ?> entry : map.entrySet()) {
				optionalParameterNames[i] = (String) entry.getKey();
				optionalParameters[i] = entry.getValue();
				i++;
			}
		} else if (parameter != null) {
			optionalParameterNames = new String[] { "parameter" };
			optionalParameters = new Object[] { parameter };
		}

		return MethodAutoMatchingUtils.invokeMethod(methods, serviceBean, null,
				null, optionalParameterNames, optionalParameters);
	}

	protected Object invokeByParameterType(Object serviceBean,
			Method[] methods, Object parameter)
			throws MethodAutoMatchingException, Exception {
		Class<?>[] optionalParameterTypes = null;
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
					optionalParameterTypes[i] = value.getClass();
					optionalParameters[i] = value;
					i++;
				}
			}
		} else if (parameter != null) {
			optionalParameterTypes = new Class[] { parameter.getClass() };
			optionalParameters = new Object[] { parameter };
		} else {
			optionalParameterTypes = new Class[] { Object.class };
			optionalParameters = new Object[] { null };
		}

		return MethodAutoMatchingUtils.invokeMethod(methods, serviceBean, null,
				null, null, null, optionalParameterTypes, optionalParameters,
				null);
	}

}
