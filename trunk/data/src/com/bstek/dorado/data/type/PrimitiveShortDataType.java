package com.bstek.dorado.data.type;

/**
 * 用于描述short的数据类型。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 13, 2007
 */
public class PrimitiveShortDataType extends ShortDataType {

	@Override
	public Object fromText(String text) {
		if (text == null) {
			return new Short((short) 0);
		}
		return super.fromText(text);
	}

	@Override
	public Object fromObject(Object value) {
		if (value == null) {
			return new Short((short) 0);
		}
		return super.fromObject(value);
	}

}
