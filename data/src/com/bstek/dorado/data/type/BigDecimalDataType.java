/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
package com.bstek.dorado.data.type;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

/**
 * 用于描述java.math.BigDecimal的数据类型。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 13, 2007
 */
public class BigDecimalDataType extends DecimalDataType {

	public Object fromText(String text) {
		if (StringUtils.isEmpty(text)) {
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
