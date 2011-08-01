package com.bstek.dorado.util.clazz;

import java.lang.reflect.Field;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-12-7
 */
public abstract class BeanPropertyUtils {

	public static boolean isValidProperty(Class<?> beanClass, String property) {
		// "callbacks" from net.sf.cglib.proxy.Factory
		// "handler" from javassist.util.proxy.ProxyObject
		// "hibernateLazyInitializer" from org.hibernate.proxy.LazyInitializer
		return (!"class".equals(property) && !"callbacks".equals(property)
				&& !"handler".equals(property) && !"hibernateLazyInitializer"
				.equals(property));
	}

	public static Object getFieldValue(Object bean, String property)
			throws Exception {
		Field field = bean.getClass().getDeclaredField(property);
		field.setAccessible(true);
		return field.get(bean);
	}

	public static void setFieldValue(Object bean, String property, Object value)
			throws Exception {
		Field field = bean.getClass().getDeclaredField(property);
		field.setAccessible(true);
		field.set(bean, value);
	}
}
