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
package com.bstek.dorado.util;

/**
 * 计数器。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 3, 2008
 */
public class Counter {
	private int i = 0;

	/**
	 * 增加一个计数。
	 */
	public void increase() {
		i++;
	}

	/**
	 * 减少一个计数。
	 */
	public void decrease() {
		i--;
	}

	/**
	 * 直接设置计数值。
	 */
	public void setValue(int i) {
		this.i = i;
	}

	/**
	 * 返回计数值。
	 */
	public int getValue() {
		return i;
	}
}
