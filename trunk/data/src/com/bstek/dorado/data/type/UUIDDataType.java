/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.data.type;

import java.util.UUID;

/**
 * 用于描述java.util.UUID的数据类型。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2015/10/10
 */
public class UUIDDataType extends SimpleDataType {

	public Object fromText(String text) {
		if (text == null || text.length() == 0) {
			return null;
		} else {
			return UUID.fromString(text);
		}
	}

	@Override
	public Object fromObject(Object value) {
		if (value == null) {
			return null;
		} else if (value instanceof UUID) {
			return value;
		} else if (value instanceof String) {
			return fromText((String) value);
		} else {
			throw new DataConvertException(value.getClass(), UUID.class);
		}
	}

}
