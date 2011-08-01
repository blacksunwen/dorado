package com.bstek.dorado.data.type;

/**
 * 用于描述java.lang.Integer的数据类型。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 13, 2007
 */
public class IntegerDataType extends IntegralDataType {

	public Object fromText(String text) {
		if (text == null) {
			return null;
		} else {
			return Integer.valueOf(text);
		}
	}

	public Object fromObject(Object value) {
		if (value == null) {
			return null;
		} else if (value instanceof Integer) {
			return value;
		} else if (value instanceof Number) {
			return new Integer(((Number) value).intValue());
		} else if (value instanceof String) {
			return fromText((String) value);
		} else {
			throw new DataConvertException(value.getClass(), Integer.class);
		}
	}
}
