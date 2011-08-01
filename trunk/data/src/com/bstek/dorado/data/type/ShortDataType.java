package com.bstek.dorado.data.type;

/**
 * 用于描述java.lang.Short的数据类型。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 13, 2007
 */
public class ShortDataType extends IntegralDataType {

	public Object fromText(String text) {
		if (text == null) {
			return null;
		} else {
			return Short.valueOf(text);
		}
	}

	public Object fromObject(Object value) {
		if (value == null) {
			return null;
		} else if (value instanceof Short) {
			return value;
		} else if (value instanceof Number) {
			return new Short(((Number) value).shortValue());
		} else if (value instanceof String) {
			return fromText((String) value);
		} else {
			throw new DataConvertException(value.getClass(), Short.class);
		}
	}

}
