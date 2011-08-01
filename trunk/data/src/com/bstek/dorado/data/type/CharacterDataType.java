package com.bstek.dorado.data.type;

/**
 * 用于描述java.lang.Character的数据类型。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 13, 2007
 */
public class CharacterDataType extends SimpleDataType {

	public Object fromText(String text) {
		if (text == null || text.length() == 0) {
			return null;
		} else {
			return new Character(text.charAt(0));
		}
	}

	public Object fromObject(Object value) {
		if (value == null) {
			return null;
		} else if (value instanceof Character) {
			return value;
		} else if (value instanceof String) {
			return fromText((String) value);
		} else {
			throw new DataConvertException(value.getClass(), Character.class);
		}
	}

}
