/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
package com.bstek.dorado.vidorsupport.plugin.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import com.bstek.dorado.vidorsupport.plugin.ConfigureParseException;
import com.bstek.dorado.vidorsupport.plugin.iapi.ConfigureData;

public class JsonData extends ConfigureData {
	
	public JsonData(JsonNode jsonNode) {
		super();
		Iterator<String> fieldNames = jsonNode.getFieldNames();
		while (fieldNames.hasNext()) {
			String name = fieldNames.next();
			JsonNode valueNode = jsonNode.get(name);
			this.setValue(name, valueNode);
		}
	}
	
	private void setValue(String name, JsonNode valueNode) {
		if (valueNode.isNull())
			return;

		Object value = this.value(valueNode);
		if (value instanceof Boolean) {
			this.bool(name, (Boolean) value);
		} else if (value instanceof Integer) {
			this.integer(name, (Integer) value);
		} else if (value instanceof String) {
			this.string(name, (String) value);
		} else if (value instanceof ConfigureData) {
			this.data(name, (ConfigureData) value);
		} else if (value.getClass().isArray()) {
			this.value(name, value);
		}

	}

	private Object value(JsonNode valueNode) {
		if (valueNode.isBoolean()) {
			boolean value = valueNode.asBoolean();
			return Boolean.valueOf(value);
		} else if (valueNode.isNumber()) {
			int value = valueNode.asInt();
			return Integer.valueOf(value);
		} else if (valueNode.isTextual()) {
			String value = valueNode.asText();
			return value;
		} else if (valueNode.isObject()) {
			ConfigureData valueData = new JsonData(valueNode);
			return valueData;
		} else if (valueNode.isArray()) {
			List<Object> values = new ArrayList<Object>();
			Iterator<JsonNode> nodes = valueNode.getElements();
			while (nodes.hasNext()) {
				JsonNode node = nodes.next();
				Object value = this.value(node);
				values.add(value);
			}
			return values.toArray();
		}

		throw new ConfigureParseException("unknown token -> "
				+ valueNode.toString());
	}
}
