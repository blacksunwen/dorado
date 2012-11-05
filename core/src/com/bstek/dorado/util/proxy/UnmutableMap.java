/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.util.proxy;

import java.util.Map;

/**
 * 不可修改的Map集合。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apr 10, 2008
 */
public class UnmutableMap<K, V> extends ChildrenMapSupport<K, V> {

	/**
	 * @param target
	 */
	public UnmutableMap(Map<K, V> target) {
		super(target);
	}

	@Override
	protected void childAdded(K key, V child) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void childRemoved(K key, V child) {
		throw new UnsupportedOperationException();
	}

}
