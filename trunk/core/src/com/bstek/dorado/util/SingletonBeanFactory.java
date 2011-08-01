package com.bstek.dorado.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;

/**
 * 单例对象的创建工厂。
 * <p>
 * 单例工厂会记住每一创建过的实例，这样当单例工厂下一次被请求创建同样的对象时将直接返回上一次创建的相应实例。
 * 单例工厂就是通过这种方式确保每一个对象只被创建一次。
 * </p>
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 15, 2007
 */
public abstract class SingletonBeanFactory {
	private static Map<Class<?>, Object> instances = new HashMap<Class<?>, Object>();

	private SingletonBeanFactory() {}

	/**
	 * 获取某个Class类型的Singleton实例。
	 * @param className 例对象的Class类名
	 * @return 单例对象的实例
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public static Object getInstance(String className)
			throws IllegalAccessException, InstantiationException,
			ClassNotFoundException {
		Assert.notNull(className, "\"className\" is required");
		return getInstance(ClassUtils.getClass(className));
	}

	/**
	 * 获取某个Class类型的Singleton实例。
	 * @param type 例对象的Class类型
	 * @return 单例对象的实例
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public synchronized static Object getInstance(Class<?> type)
			throws IllegalAccessException, InstantiationException {
		Assert.notNull(type, "\"type\" is required");
		Object instance = instances.get(type);
		if (instance == null) {
			instance = type.newInstance();
			instances.put(type, instance);
		}
		return instance;
	}
}
