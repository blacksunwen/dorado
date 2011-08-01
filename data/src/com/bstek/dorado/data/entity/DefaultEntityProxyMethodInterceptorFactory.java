package com.bstek.dorado.data.entity;

import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;

import com.bstek.dorado.data.type.EntityDataType;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-12-20
 */
public class DefaultEntityProxyMethodInterceptorFactory implements
		EntityProxyMethodInterceptorFactory {

	public MethodInterceptor[] createInterceptors(EntityDataType dataType,
			Class<?> classType, Object entity) throws Exception {
		MethodInterceptor mi;
		if (Map.class.isAssignableFrom(classType)) {
			mi = new MapEntityInterceptor(dataType);
		} else {
			mi = new BeanEntityInterceptor(dataType, classType);
		}
		return new MethodInterceptor[] { mi };
	}

}
