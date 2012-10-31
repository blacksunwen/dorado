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
package com.bstek.dorado.data.variant;

import com.bstek.dorado.data.type.PrimitiveBooleanDataType;

public class ReversePrimitiveBooleanDataType extends PrimitiveBooleanDataType {

	@Override
	public Object fromObject(Object value) {
		Boolean b = (Boolean) super.fromObject(value);
		b = new Boolean(!b.booleanValue());
		return b;
	}

}
