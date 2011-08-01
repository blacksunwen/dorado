package com.bstek.dorado.data.entity;

import java.lang.reflect.Method;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.bstek.dorado.data.type.EntityDataType;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-12-19
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class MapEntityInterceptor extends AbstractMapEntityEnhancer implements
		MethodInterceptor {
	private static final String GET = "get";
	private static final String PUT = "put";

	public MapEntityInterceptor(EntityDataType dataType) {
		super(dataType);
	}

	@Override
	protected Object internalReadProperty(Object entity, String property)
			throws Exception {
		return ((Map) entity).get(property);
	}

	@Override
	protected void internalWriteProperty(Object entity, String property,
			Object value) throws Exception {
		((Map) entity).put(property, value);
	}

	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		Object result = interceptor(methodInvocation);
		if (result == UNDISPOSED_VALUE) {
			result = methodInvocation.proceed();
		}
		return result;
	}

	public Object interceptor(MethodInvocation methodInvocation)
			throws Throwable {
		Object result = UNDISPOSED_VALUE;
		Method method = methodInvocation.getMethod();
		String mothedName = method.getName();
		if (GET.equals(mothedName) && method.getParameterTypes().length == 1) { // read
			String property = (String) methodInvocation.getArguments()[0];
			result = interceptReadMethod(methodInvocation, property);
		} else if (PUT.equals(mothedName)
				&& method.getParameterTypes().length == 2) { // write
			String property = (String) methodInvocation.getArguments()[0];
			if (!interceptWriteMethod(methodInvocation, property)) {
				return null;
			}
		}
		return result;
	}

	protected Object interceptReadMethod(MethodInvocation methodInvocation,
			String property) throws Throwable {
		return interceptReadMethod(methodInvocation.getThis(), property,
				methodInvocation.proceed(), false);
	}

	protected boolean interceptWriteMethod(MethodInvocation methodInvocation,
			String property) throws Throwable {
		return interceptWriteMethod(methodInvocation.getThis(), property,
				methodInvocation.getArguments()[1], false);
	}

}
