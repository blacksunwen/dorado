﻿/*
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
/**
 *
 */
package com.bstek.dorado.data.method;

import java.lang.reflect.Method;

/**
 * 方法描述对象。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jan 11, 2008
 */
public class MethodDescriptor {
	private Method method;
	private int[] argIndexs;
	private int matchingRate;

	public MethodDescriptor(Method method, int[] argIndexs, int matchingRate) {
		this.method = method;
		this.argIndexs = argIndexs;
		this.matchingRate = matchingRate;
	}

	public Method getMethod() {
		return method;
	}

	public int[] getArgIndexs() {
		return argIndexs;
	}

	public int getMatchingRate() {
		return matchingRate;
	}
}
