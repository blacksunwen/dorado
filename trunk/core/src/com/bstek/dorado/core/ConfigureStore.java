package com.bstek.dorado.core;

import java.util.Set;

import org.apache.commons.lang.BooleanUtils;

/**
 * 用于存贮配置信息的对象。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Sep 27, 2008
 */
public abstract class ConfigureStore {

	/**
	 * 检查是否包含某项配置信息。
	 */
	public abstract boolean contains(String key);

	/**
	 * 根据给定的配置项的名称返回其值。
	 * @param key 配置项的名称
	 */
	public abstract Object get(String key);

	/**
	 * 设置给定的配置项的值。
	 * @param key 配置项的名称
	 * @param value 值。
	 */
	public abstract void set(String key, Object value);

	/**
	 * 返回所有配置项名称的集合。
	 * @return 所有配置项名称的集合。
	 */
	public abstract Set<String> keySet();

	/**
	 * 以String形式返回某配置项的值。
	 * @param key 配置项的名称
	 */
	public String getString(String key) {
		Object value = get(key);
		return (value == null) ? null : value.toString();
	}

	/**
	 * 以String形式返回某配置项的值，如果该配置项不存在则返回给定的默认值。
	 * @param key 配置项的名称
	 * @param defaultValue 默认值
	 */
	public String getString(String key, String defaultValue) {
		if (contains(key)) {
			return getString(key);
		}
		else {
			return defaultValue;
		}
	}

	/**
	 * 以boolean形式返回某配置项的值。
	 * @param key 配置项的名称
	 */
	public boolean getBoolean(String key) {
		Object value = get(key);
		return (value instanceof Boolean) ? ((Boolean) value).booleanValue()
				: BooleanUtils.toBoolean((value == null) ? null : value
						.toString());
	}

	/**
	 * 以boolean形式返回某配置项的值，如果该配置项不存在则返回给定的默认值。
	 * @param key 配置项的名称
	 * @param defaultValue 默认值
	 */
	public boolean getBoolean(String key, boolean defaultValue) {
		if (contains(key)) {
			return getBoolean(key);
		}
		else {
			return defaultValue;
		}
	}

	/**
	 * 以long形式返回某配置项的值。
	 * @param key 配置项的名称
	 */
	public long getLong(String key) {
		Object value = get(key);
		return (value instanceof Number) ? ((Number) value).longValue() : Long
				.parseLong((value == null) ? null : value.toString());
	}

	/**
	 * 以long形式返回某配置项的值，如果该配置项不存在则返回给定的默认值。
	 * @param key 配置项的名称
	 * @param defaultValue 默认值
	 */
	public long getLong(String key, long defaultValue) {
		if (contains(key)) {
			return getLong(key);
		}
		else {
			return defaultValue;
		}
	}
}
