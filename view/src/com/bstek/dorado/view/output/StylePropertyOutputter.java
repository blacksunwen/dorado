/**
 * 
 */
package com.bstek.dorado.view.output;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-6-22
 */
public class StylePropertyOutputter implements PropertyOutputter {

	public boolean isEscapeValue(Object value) {
		if (value == null) {
			return true;
		}

		if (value instanceof String) {
			return StringUtils.isBlank((String) value);
		} else if (value instanceof Map) {
			return ((Map<?, ?>) value).isEmpty();
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public void output(Object object, OutputContext context) throws Exception {
		JsonBuilder json = context.getJsonBuilder();
		if (object instanceof Map) {
			StringBuffer style = new StringBuffer();
			Map<String, String> map = (Map<String, String>) object;
			for (Map.Entry<String, String> entry : map.entrySet()) {
				String value = entry.getValue();
				if (OutputUtils.isEscapeValue(value)) {
					continue;
				}
				if (style.length() > 0) {
					style.append(';');
				}
				style.append(entry.getKey()).append(':').append(String.valueOf(value));
			}
			json.value(style.toString());
		} else {
			json.value(object);
		}
	}
}
