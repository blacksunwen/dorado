package com.bstek.dorado.data.type;

/**
 * 用于描述java.lang.Byte的数据类型。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 13, 2007
 */
public class ByteDataType extends IntegralDataType {

	public Object fromText(String text) {
		if (text == null) {
			return null;
		} else {
			return Byte.valueOf(text);
		}
	}

	public Object fromObject(Object value) {
		if (value == null) {
			return null;
		} else if (value instanceof Byte) {
			return value;
		} else if (value instanceof Number) {
			return new Byte(((Number) value).byteValue());
		} else if (value instanceof String) {
			return fromText((String) value);
		} else {
			throw new DataConvertException(value.getClass(), Byte.class);
		}
	}

}
