package com.bstek.dorado.util.clazz;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.io.ResourceUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-12-26
 */
public class ClassUtils {
	@SuppressWarnings("rawtypes")
	public static Class forName(String className) throws ClassNotFoundException {
		try {
			return org.apache.commons.lang.ClassUtils.getClass(className);
		} catch (Error e) {
			throw new ClassNotFoundException(e.getMessage(), e);
		}
	}

	public static Set<Class<?>> foundClassTypes(String expression,
			Class<?> targetType) throws IOException, ClassNotFoundException {
		Set<Class<?>> classTypes = new HashSet<Class<?>>();
		String pathExpression = "classpath*:" + expression.replace('.', '/')
				+ ".class";
		Resource[] resources = ResourceUtils.getResources(pathExpression);
		for (Resource resource : resources) {
			String path = resource.getPath();
			int i1 = path.lastIndexOf('/');
			String simpleClassName = path.substring((i1 < 0) ? 0 : (i1 + 1),
					path.length() - 6);
			int i2 = expression.lastIndexOf('.');
			String className = expression.substring(0, i2 + 1)
					+ simpleClassName;

			Class<?> type = null;
			try {
				type = ClassUtils.forName(className);
			} catch (Exception e) {
				// do nothing
			}
			if (type != null && targetType.isAssignableFrom(type)) {
				classTypes.add(type);
			}
		}
		return classTypes;
	}

	public static Set<Class<?>> foundClassTypes(String[] expressions,
			Class<?> targetType) throws IOException, ClassNotFoundException {
		Set<Class<?>> classTypes = new HashSet<Class<?>>();
		for (String expression : expressions) {
			classTypes.addAll(foundClassTypes(expression, targetType));
		}
		return classTypes;
	}
}
