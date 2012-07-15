package com.bstek.dorado.core.resource;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-5-10
 */
public class DefaultResourceBundle implements ListableResourceBundle,
		Serializable {
	private static final long serialVersionUID = 7678593697684435624L;

	private Properties properties;

	public DefaultResourceBundle(Properties properties) {
		this.properties = properties;
	}

	public String getString(String key, Object... args) {
		String result = properties.getProperty(key);
		if (result != null && args != null) {
			result = String.format(result, args);
		}
		return result;
	}

	public Enumeration<String> getKeys() {
		return new KeyEnumeration(properties.keys());
	}

}

class KeyEnumeration implements Enumeration<String> {
	private Enumeration<Object> enumeration;

	public KeyEnumeration(Enumeration<Object> enumeration) {
		this.enumeration = enumeration;
	}

	public boolean hasMoreElements() {
		return enumeration.hasMoreElements();
	}

	public String nextElement() {
		return (String) enumeration.nextElement();
	}

}
