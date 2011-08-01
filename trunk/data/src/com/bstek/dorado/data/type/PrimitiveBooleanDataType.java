package com.bstek.dorado.data.type;

/**
 * 用于描述boolean的数据类型。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 13, 2007
 */
public class PrimitiveBooleanDataType extends BooleanDataType {

	@Override
	public Object fromObject(Object value) {
		if (value == null) {
			return Boolean.FALSE;
		} else {
			return super.fromObject(value);
		}
	}

	@Override
	public Object fromText(String text) {
		if (text == null) {
			return Boolean.FALSE;
		} else {
			return super.fromText(text);
		}
	}

}
