package com.bstek.dorado.data.resolver.manager;

import java.lang.reflect.Method;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bstek.dorado.common.method.MethodAutoMatchingException;
import com.bstek.dorado.common.method.MethodAutoMatchingUtils;
import com.bstek.dorado.core.bean.BeanFactoryUtils;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.resolver.DataResolver;
import com.bstek.dorado.util.Assert;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apr 29, 2009
 */
public class DataResolverInterceptorInvoker implements MethodInterceptor {
	private static final Log logger = LogFactory
			.getLog(DataResolverInterceptorInvoker.class);

	public static final String INTERCEPTING_METHOD_NAME = "resolve";
	public static final String DEFAULT_METHOD_NAME = INTERCEPTING_METHOD_NAME;

	private static final Object[] EMPTY_ARGS = new Object[0];
	private static final String[] EMPTY_NAMES = new String[0];
	private static final Class<?>[] EMPTY_TYPES = new Class[0];

	private String interceptorName;
	private String methodName;
	private Object interceptor;

	/**
	 * @param interceptorExpression
	 *            用户指定的方法拦截器。
	 */
	public DataResolverInterceptorInvoker(String interceptorExpression) {
		Assert.notEmpty(interceptorExpression,
				"\"interceptorExpression\" could not be empty.");
		int i = interceptorExpression.lastIndexOf("#");
		if (i > 0) {
			interceptorName = interceptorExpression.substring(0, i);
			methodName = interceptorExpression.substring(i + 1);
		} else {
			interceptorName = interceptorExpression;
		}
		if (StringUtils.isEmpty(methodName)) {
			methodName = DEFAULT_METHOD_NAME;
		}
	}

	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		Method proxyMethod = methodInvocation.getMethod();
		if (!proxyMethod.getName().equals(INTERCEPTING_METHOD_NAME)) {
			return methodInvocation.proceed();
		}

		if (interceptor == null) {
			interceptor = BeanFactoryUtils.getBean(interceptorName);
		}

		Method[] methods = MethodAutoMatchingUtils.getMethodsByName(
				interceptor.getClass(), methodName);
		if (methods.length == 0) {
			throw new NoSuchMethodException("Method [" + methodName
					+ "] not found in [" + interceptorName + "].");
		}

		DataResolver dataResolver = (DataResolver) methodInvocation.getThis();
		try {
			return invokeInterceptorByParamName(dataResolver, methods,
					methodInvocation);
		} catch (MethodAutoMatchingException e1) {
			try {
				return invokeInterceptorByParamType(dataResolver, methods,
						methodInvocation);
			} catch (MethodAutoMatchingException e2) {
				logger.error(e2, e2);
				throw e1;
			}
		}
	}

	private Object invokeInterceptorByParamName(DataResolver dataResolver,
			Method[] methods, MethodInvocation methodInvocation)
			throws Exception {
		Object[] proxyArgs = methodInvocation.getArguments();
		DataItems dataItems = (DataItems) proxyArgs[0];
		Object parameter = (proxyArgs.length > 1) ? proxyArgs[1] : null;

		String[] optionalArgNames = null;
		Object[] optionalArgs = null;

		String[] parameterArgNames = EMPTY_NAMES;
		Object[] parameterArgs = EMPTY_ARGS;
		if (parameter != null && parameter instanceof Map<?, ?>) {
			Map<?, ?> map = (Map<?, ?>) parameter;
			parameterArgNames = new String[map.size() + 1];
			parameterArgs = new Object[parameterArgNames.length];
			parameterArgNames[0] = "parameter";
			parameterArgs[0] = parameter;

			int i = 1;
			for (Map.Entry<?, ?> entry : map.entrySet()) {
				parameterArgNames[i] = (String) entry.getKey();
				parameterArgs[i] = entry.getValue();
				i++;
			}
		} else {
			parameterArgNames = new String[] { "parameter" };
			parameterArgs = new Object[] { parameter };
		}

		int dataItemsArgCount = (dataItems != null) ? dataItems.size() : 0;
		optionalArgNames = new String[dataItemsArgCount
				+ parameterArgNames.length + 3];
		optionalArgs = new Object[optionalArgNames.length];
		optionalArgNames[0] = "dataItems";
		optionalArgNames[1] = "dataResolver";
		optionalArgNames[2] = "methodInvocation";
		optionalArgs[0] = dataItems;
		optionalArgs[1] = dataResolver;
		optionalArgs[2] = methodInvocation;

		if (dataItems != null) {
			int i = 3;
			for (Map.Entry<String, Object> entry : dataItems.entrySet()) {
				optionalArgNames[i] = entry.getKey();
				optionalArgs[i] = entry.getValue();
				i++;
			}
		}
		System.arraycopy(parameterArgNames, 0, optionalArgNames,
				dataItemsArgCount + 3, parameterArgNames.length);
		System.arraycopy(parameterArgs, 0, optionalArgs, dataItemsArgCount + 3,
				parameterArgs.length);

		return MethodAutoMatchingUtils.invokeMethod(methods, interceptor, null,
				null, optionalArgNames, optionalArgs);
	}

	private Object invokeInterceptorByParamType(DataResolver dataResolver,
			Method[] methods, MethodInvocation methodInvocation)
			throws MethodAutoMatchingException, Exception {
		Object[] proxyArgs = methodInvocation.getArguments();
		DataItems dataItems = (DataItems) proxyArgs[0];
		Object parameter = (proxyArgs.length > 1) ? proxyArgs[1] : null;

		Class<?>[] exactArgTypes = new Class[] { DataItems.class,
				DataProvider.class, MethodInvocation.class };
		Object[] exactArgs = new Object[] { dataItems, dataResolver,
				methodInvocation };

		Class<?>[] optionalArgTypes = null;
		Object[] optionalArgs = null;

		Class<?>[] parameterArgTypes = EMPTY_TYPES;
		Object[] parameterArgs = EMPTY_ARGS;

		if (parameter != null && parameter instanceof Map<?, ?>) {
			Map<?, ?> map = (Map<?, ?>) parameter;
			parameterArgTypes = new Class[map.size() + 1];
			parameterArgs = new Object[parameterArgTypes.length];
			parameterArgTypes[0] = parameter.getClass();
			parameterArgs[0] = parameter;

			int i = 1;
			for (Object value : map.values()) {
				if (value != null) {
					parameterArgTypes[i] = value.getClass();
					parameterArgs[i] = value;
					i++;
				}
			}
		} else if (parameter != null) {
			parameterArgTypes = new Class[] { parameter.getClass() };
			parameterArgs = new Object[] { parameter };
		} else {
			parameterArgTypes = new Class[] { Object.class };
			parameterArgs = new Object[] { null };
		}

		int dataItemsArgCount = (dataItems != null) ? dataItems.size() : 0;
		optionalArgTypes = new Class[dataItemsArgCount
				+ parameterArgTypes.length];
		optionalArgs = new Object[optionalArgTypes.length];

		if (dataItems != null) {
			int i = 0;
			for (Object dataItem : dataItems.values()) {
				if (dataItem != null) {
					optionalArgTypes[i] = dataItem.getClass();
					optionalArgs[i] = dataItem;
					i++;
				}
			}
		}
		System.arraycopy(parameterArgTypes, 0, optionalArgTypes,
				dataItemsArgCount, parameterArgTypes.length);
		System.arraycopy(parameterArgs, 0, optionalArgs, dataItemsArgCount,
				parameterArgs.length);

		return MethodAutoMatchingUtils.invokeMethod(methods, interceptor, null,
				null, exactArgTypes, exactArgs, optionalArgTypes, optionalArgs,
				null);
	}
}
