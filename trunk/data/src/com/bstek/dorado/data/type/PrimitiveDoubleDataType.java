package com.bstek.dorado.data.type;

import org.apache.commons.lang.StringUtils;

/**
 * 用于描述double的数据类型。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 13, 2007
 */
public class PrimitiveDoubleDataType extends DoubleDataType {

	@Override
	public Object fromText(String text) {
		if (StringUtils.isEmpty(text)) {
			return new Double(0);
		}
		return super.fromText(text);
	}

	@Override
	public Object fromObject(Object value) {
		if (value == null) {
			return new Double(0);
		}
		return super.fromObject(value);
	}

}
