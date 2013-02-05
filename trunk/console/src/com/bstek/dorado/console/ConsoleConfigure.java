package com.bstek.dorado.console;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.BooleanUtils;

public abstract class ConsoleConfigure {
	private static final String PROPERTIES_PATH = "com/bstek/dorado/console/configure.properties";
	@SuppressWarnings("rawtypes")
	private static Map map;
	static {
		Properties properties = new Properties();
		map = properties;
		InputStream in = ConsoleConfigure.class.getClassLoader()
				.getResourceAsStream(PROPERTIES_PATH);

		try {
			properties.load(in);
		} catch (IOException e) {
		} finally {
			try {
				in.close();
			} catch (IOException e) {
			
			}
		}
	}

	public static Object get(String key) {
		return map.get(key);

	}

	/**
	 * 以String形式返回某配置项的值。
	 * 
	 * @param key
	 *            配置项的名称
	 */
	public static String getString(String key) {
		Object value = get(key);
		return (value == null) ? null : value.toString();
	}

	/**
	 * 以boolean形式返回某配置项的值。
	 * 
	 * @param key
	 *            配置项的名称
	 */
	public static boolean getBoolean(String key) {
		Object value = get(key);
		return (value instanceof Boolean) ? ((Boolean) value).booleanValue()
				: BooleanUtils.toBoolean((value == null) ? null : value
						.toString());
	}

	/**
	 * 以long形式返回某配置项的值。
	 * 
	 * @param key
	 *            配置项的名称
	 */
	public static long getLong(String key) {
		Object value = get(key);
		return (value instanceof Number) ? ((Number) value).longValue() : Long
				.parseLong((value == null) ? null : value.toString());
	}

}
