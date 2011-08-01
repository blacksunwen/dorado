package com.bstek.dorado.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bstek.dorado.util.Assert;

/**
 * 用于管理及方便读取Dorado基础配置的工具类。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Dec 11, 2007
 */
public abstract class Configure {
	private static final String PROPERTIES_PATH = "com/bstek/dorado/core/configure.properties";
	private static final Log logger = LogFactory.getLog(Configure.class);

	private static ConfigureStore store;

	static {
		Properties properties = new Properties();
		store = new PropertiesConfigureStore(properties);

		InputStream in = Configure.class.getClassLoader().getResourceAsStream(
				PROPERTIES_PATH);
		Assert.notNull(in, "Can not found resource \"" + PROPERTIES_PATH
				+ "\"!");

		try {
			properties.load(in);
		}
		catch (IOException e) {
			logger.error(e, e);
		}
	}

	/**
	 * 返回内部用于存贮配置信息的对象。
	 */
	public static ConfigureStore getStore() {
		return store;
	}

	/**
	 * 以String形式返回某配置项的值。
	 * @param key 配置项的名称
	 */
	public static String getString(String key) {
		return store.getString(key);
	}

	/**
	 * 以String形式返回某配置项的值，如果该配置项不存在则返回给定的默认值。
	 * @param key 配置项的名称
	 * @param defaultValue 默认值
	 */
	public static String getString(String key, String defaultValue) {
		return store.getString(key, defaultValue);
	}

	/**
	 * 以boolean形式返回某配置项的值。
	 * @param key 配置项的名称
	 */
	public static boolean getBoolean(String key) {
		return store.getBoolean(key);
	}

	/**
	 * 以boolean形式返回某配置项的值，如果该配置项不存在则返回给定的默认值。
	 * @param key 配置项的名称
	 * @param defaultValue 默认值
	 */
	public static boolean getBoolean(String key, boolean defaultValue) {
		return store.getBoolean(key, defaultValue);
	}

	/**
	 * 以long形式返回某配置项的值。
	 * @param key 配置项的名称
	 */
	public static long getLong(String key) {
		return store.getLong(key);
	}

	/**
	 * 以long形式返回某配置项的值，如果该配置项不存在则返回给定的默认值。
	 * @param key 配置项的名称
	 * @param defaultValue 默认值
	 */
	public static long getLong(String key, long defaultValue) {
		return store.getLong(key, defaultValue);
	}
}

class PropertiesConfigureStore extends ConfigureStore {
	private Map<Object, Object> map;

	public PropertiesConfigureStore(Properties properties) {
		this.map = properties;
	}

	@Override
	public boolean contains(String key) {
		return map.containsKey(key);
	}

	@Override
	public Object get(String key) {
		return map.get(key);
	}

	@Override
	public void set(String key, Object value) {
		if (value != null) {
			map.put(key, value);
		}
		else {
			map.remove(key);
		}
	}

	@Override
	public Set<String> keySet() {
		Set<String> keys = new HashSet<String>();
		for (Object key : map.keySet()) {
			keys.add(String.valueOf(key));
		}
		return keys;
	}
};