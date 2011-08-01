package com.bstek.dorado.data.type;

import java.math.BigDecimal;

/**
 * 用于描述java.math.BigDecimal的数据类型。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 13, 2007
 */
public class BigDecimalDataType extends DecimalDataType {

	public Object fromText(String text) {
		if (text == null) {
			return null;
		} else {
			return new BigDecimal(text);
		}
	}

	@Override
	public Object fromObject(Object value) {
		if (value == null) {
			return null;
		} else if (value instanceof BigDecimal) {
			return value;
		} else if (value instanceof Number) {
			return new BigDecimal(((Number) value).toString());
		} else if (value instanceof String) {
			return fromText((String) value);
		} else {
			throw new DataConvertException(value.getClass(), BigDecimal.class);
		}
	}
}
