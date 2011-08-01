package com.bstek.dorado.data.type;

/**
 * 用于描述java.lang.Double的数据类型。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 13, 2007
 */
public class DoubleDataType extends DecimalDataType {

	public Object fromText(String text) {
		if (text == null) {
			return null;
		} else {
			return Double.valueOf(text);
		}
	}

	@Override
	public Object fromObject(Object value) {
		if (value == null) {
			return null;
		} else if (value instanceof Double) {
			return value;
		} else if (value instanceof Number) {
			return new Double(((Number) value).doubleValue());
		} else if (value instanceof String) {
			return fromText((String) value);
		} else {
			throw new DataConvertException(value.getClass(), Double.class);
		}
	}
}
