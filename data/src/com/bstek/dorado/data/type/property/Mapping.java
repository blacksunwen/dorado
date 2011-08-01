package com.bstek.dorado.data.type.property;

import com.bstek.dorado.annotation.ViewAttribute;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jun 24, 2009
 */
public class Mapping {
	private Object mapValues;
	private String keyProperty;
	private String valueProperty;

	@ViewAttribute(editor = "collection[pojo]")
	public Object getMapValues() {
		return mapValues;
	}

	public void setMapValues(Object mapValues) {
		this.mapValues = mapValues;
	}

	public String getKeyProperty() {
		return keyProperty;
	}

	public void setKeyProperty(String keyProperty) {
		this.keyProperty = keyProperty;
	}

	public String getValueProperty() {
		return valueProperty;
	}

	public void setValueProperty(String valueProperty) {
		this.valueProperty = valueProperty;
	}
}
