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
package com.bstek.dorado.common;

/**
 * 可获知父对象的对象的通用接口。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Dec 30, 2007
 */
public interface ParentAware<T> {
	/**
	 * 设置父对象
	 */
	void setParent(T parent);

	T getParent();
}
