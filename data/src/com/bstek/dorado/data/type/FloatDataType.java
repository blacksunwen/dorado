package com.bstek.dorado.data.type;

/**
 * 用于描述java.lang.Float的数据类型。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 13, 2007
 */
public class FloatDataType extends DecimalDataType {

	public Object fromText(String text) {
		if (text == null) {
			return null;
		} else {
			return Float.valueOf(text);
		}
	}

	public Object fromObject(Object value) {
		if (value == null) {
			return null;
		} else if (value instanceof Float) {
			return value;
		} else if (value instanceof Number) {
			return new Float(((Number) value).floatValue());
		} else if (value instanceof String) {
			return fromText((String) value);
		} else {
			throw new DataConvertException(value.getClass(), Float.class);
		}
	}
}
