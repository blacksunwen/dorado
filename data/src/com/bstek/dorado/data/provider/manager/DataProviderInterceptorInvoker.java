package com.bstek.dorado.data.provider.manager;

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
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.resolver.DataResolver;
import com.bstek.dorado.util.Assert;

/**
 * 用于激活用户绑定于DataProvider的拦截器的类，该类本身也是一个方法拦截器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jan 11, 2008
 */
public class DataProviderInterceptorInvoker implements MethodInterceptor {
	private static final Log logger = LogFactory
			.getLog(DataProviderInterceptorInvoker.class);

	public static final String INTERCEPTING_METHOD_NAME = "getResult";
	public static final String DEFAULT_METHOD_NAME = INTERCEPTING_METHOD_NAME;

	private static final Object[] EMPTY_ARGS = new Object[0];
	private static final String[] EMPTY_NAMES = new String[0];

	private String interceptorName;
	private String methodName;
	private Object interceptor;

	/**
	 * @param interceptorExpression
	 *            用户指定的方法拦截器。
	 */
	public DataProviderInterceptorInvoker(String interceptorExpression) {
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

		DataProvider dataProvider = (DataProvider) methodInvocation.getThis();
		try {
			return invokeInterceptorByParamName(dataProvider, methods,
					methodInvocation);
		} catch (MethodAutoMatchingException e1) {
			try {
				return invokeInterceptorByParamType(dataProvider, methods,
						methodInvocation);
			} catch (MethodAutoMatchingException e2) {
				logger.error(e2, e2);
				throw e1;
			}
		}
	}

	private Object invokeInterceptorByParamName(DataProvider dataProvider,
			Method[] methods, MethodInvocation methodInvocation)
			throws Exception {
		Method proxyMethod = methodInvocation.getMethod();
		Object[] proxyArgs = methodInvocation.getArguments();
		Class<?>[] parameterTypes = proxyMethod.getParameterTypes();

		String[] requiredParameterNames = null;
		Object[] requiredParameters = null;
		String[] optionalParameterNames = null;
		Object[] optionalParameters = null;

		Object parameter = null;
		int parameterParameterIndex = MethodAutoMatchingUtils.indexOfTypes(
				parameterTypes, Object.class);
		if (parameterParameterIndex >= 0) {
			parameter = proxyArgs[parameterParameterIndex];
		}

		int pageParameterIndex = MethodAutoMatchingUtils.indexOfTypes(
				parameterTypes, Page.class);
		if (pageParameterIndex >= 0) {
			requiredParameterNames = new String[] { "page" };
			requiredParameters = new Object[] { proxyArgs[pageParameterIndex] };
		}

		String[] parameterParameterNames = EMPTY_NAMES;
		Object[] parameterParameters = EMPTY_ARGS;
		if (parameter != null && parameter instanceof Map<?, ?>) {
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
		} else {
			parameterParameterNames = new String[] { "parameter" };
			parameterParameters = new Object[] { parameter };
		}

		optionalParameterNames = new String[parameterParameterNames.length + 2];
		optionalParameterNames[0] = "dataProvider";
		optionalParameterNames[1] = "methodInvocation";
		System.arraycopy(parameterParameterNames, 0, optionalParameterNames, 2,
				parameterParameterNames.length);

		optionalParameters = new Object[optionalParameterNames.length];
		optionalParameters[0] = dataProvider;
		optionalParameters[1] = methodInvocation;
		System.arraycopy(parameterParameters, 0, optionalParameters, 2,
				parameterParameters.length);

		return MethodAutoMatchingUtils.invokeMethod(methods, interceptor,
				requiredParameterNames, requiredParameters,
				optionalParameterNames, optionalParameters);
	}

	private Object invokeInterceptorByParamType(DataProvider dataProvider,
			Method[] methods, MethodInvocation methodInvocation)
			throws MethodAutoMatchingException, Exception {
		Method proxyMethod = methodInvocation.getMethod();
		Object[] proxyArgs = methodInvocation.getArguments();
		Class<?>[] proxyArgTypes = proxyMethod.getParameterTypes();

		Class<?>[] requiredArgTypes = null;
		Object[] requiredArgs = null;

		int pageArgIndex = MethodAutoMatchingUtils.indexOfTypes(proxyArgTypes,
				Page.class);
		if (pageArgIndex >= 0) {
			requiredArgTypes = new Class[] { Page.class };
			requiredArgs = new Object[] { proxyArgs[pageArgIndex] };
		}

		Class<?>[] exactArgTypes = new Class[] { DataResolver.class,
				MethodInvocation.class };
		Object[] exactArgs = new Object[] { dataProvider, methodInvocation };

		Class<?>[] optionalArgTypes = null;
		Object[] optionalArgs = null;

		Object parameter = null;
		int parameterArgIndex = MethodAutoMatchingUtils.indexOfTypes(
				proxyArgTypes, Object.class);
		if (parameterArgIndex >= 0) {
			parameter = proxyArgs[parameterArgIndex];
		}
		if (parameter != null && parameter instanceof Map<?, ?>) {
			Map<?, ?> map = (Map<?, ?>) parameter;
			optionalArgTypes = new Class[map.size() + 1];
			optionalArgs = new Object[optionalArgTypes.length];
			optionalArgTypes[0] = parameter.getClass();
			optionalArgs[0] = parameter;

			int i = 1;
			for (Object value : map.values()) {
				if (value != null) {
					optionalArgTypes[i] = value.getClass();
					optionalArgs[i] = value;
					i++;
				}
			}
		} else if (parameter != null) {
			optionalArgTypes = new Class[] { parameter.getClass() };
			optionalArgs = new Object[] { parameter };
		} else {
			optionalArgTypes = new Class[] { Object.class };
			optionalArgs = new Object[] { null };
		}

		return MethodAutoMatchingUtils.invokeMethod(methods, interceptor,
				requiredArgTypes, requiredArgs, exactArgTypes, exactArgs,
				optionalArgTypes, optionalArgs, null);
	}
}
