package com.bstek.dorado.util.clazz;

import java.lang.reflect.ParameterizedType;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-12-7
 */
public class TypeInfo {
	private static Pattern AGGREGATION_PATTERN_1 = Pattern
			.compile("^([\\w|.]*)\\<([\\w|.]+)\\>$");
	private static Pattern AGGREGATION_PATTERN_2 = Pattern
			.compile("^([\\w|.]*)\\[\\]$");

	private Class<?> type;
	private boolean aggregated;

	public static TypeInfo parse(String className)
			throws ClassNotFoundException {
		if (StringUtils.isEmpty(className)) {
			return null;
		}

		String typeName = null;
		boolean aggregated = false;
		Matcher matcher = AGGREGATION_PATTERN_1.matcher(className);
		if (matcher.matches()) {
			typeName = matcher.group(2);
			aggregated = true;
		} else {
			matcher = AGGREGATION_PATTERN_2.matcher(className);
			if (matcher.matches()) {
				typeName = matcher.group(1);
				aggregated = true;
			}
		}
		if (typeName == null) {
			typeName = className;
		}

		return new TypeInfo(ClassUtils.getClass(typeName), aggregated);
	}

	public static TypeInfo parse(ParameterizedType type, boolean aggregated)
			throws ClassNotFoundException {
		Class<?> classType;
		try {
			classType = (Class<?>) type.getActualTypeArguments()[0];
		} catch (Exception e) {
			classType = Object.class;
		}
		return new TypeInfo(classType, aggregated);
	}

	public TypeInfo(Class<?> type, boolean aggregated) {
		this.type = type;
		this.aggregated = aggregated;
	}

	public Class<?> getType() {
		return type;
	}

	public boolean isAggregated() {
		return aggregated;
	}
}
