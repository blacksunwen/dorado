package com.bstek.dorado.data.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.bstek.dorado.core.resource.DefaultResourceBundle;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-5-10
 */
public class ModelResourceBundle extends DefaultResourceBundle {
	private static final long serialVersionUID = -3873583139272775060L;

	private Map<String, Properties> subPropertiesMap;

	public ModelResourceBundle(Properties properties) {
		super(properties);

		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			if (key.charAt(0) == '#') {
				key = key.substring(1);
				int i = key.indexOf('.');
				if (i < 1) {
					throw new IllegalArgumentException(
							"Invalid resource key \"" + key + "\".");
				}
				String dataTypeName = key.substring(0, i);
				String subKey = key.substring(i + 1);

				Properties subProperties = null;
				if (subPropertiesMap == null) {
					subPropertiesMap = new HashMap<String, Properties>();
				} else {
					subProperties = subPropertiesMap.get(dataTypeName);
				}

				if (subProperties == null) {
					subProperties = new Properties();
					subPropertiesMap.put(dataTypeName, subProperties);
				}
				subProperties.setProperty(subKey, value);
			}
		}
	}

	public Properties getSubProperties(String dataTypeName) {
		return (subPropertiesMap == null) ? null : subPropertiesMap
				.get(dataTypeName);
	}

}
