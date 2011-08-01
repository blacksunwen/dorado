package com.bstek.dorado.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 用于辅助实现对象克隆的工具类。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Dec 30, 2007
 */
public abstract class CloneUtils {
	private static final String CLONE_METHOD = "clone";
	private static final Class<?>[] CLONE_METHOD_ARGTYPES = new Class<?>[] {};
	private static final Object[] CLONE_METHOD_ARGS = new Object[] {};

	/**
	 * 克隆对象。
	 * @param object 被克隆的对象
	 * @return 克隆的对象
	 * @throws CloneNotSupportedException
	 */
	public static Object clone(Object object) throws CloneNotSupportedException {
		Object clonedObject = null;
		Class<?> cl = object.getClass();
		Method method = null;
		try {
			do {
				try {
					method = cl.getDeclaredMethod(CLONE_METHOD,
							CLONE_METHOD_ARGTYPES);
				}
				catch (NoSuchMethodException e) {
					cl = cl.getSuperclass();
				}
			} while (method == null);

			if (method != null) {
				boolean methodAccessible = method.isAccessible();
				if (!methodAccessible) method.setAccessible(true);
				try {
					clonedObject = method.invoke(object, CLONE_METHOD_ARGS);
				}
				finally {
					if (!methodAccessible) method.setAccessible(false);
				}
			}
		}
		catch (SecurityException e) {
			e.printStackTrace();
		}
		catch (InvocationTargetException e) {
			e.getCause().printStackTrace();
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return clonedObject;
	}

}
