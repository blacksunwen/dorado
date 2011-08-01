package com.bstek.dorado.data.type;

/**
 * 用于描述java.lang.String的数据类型。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 13, 2007
 */
public class StringDataType extends SimpleDataType {

	public Object fromText(String text) {
		return text;
	}

	public Object fromObject(Object value) {
		return (value == null) ? null : value.toString();
	}
}
