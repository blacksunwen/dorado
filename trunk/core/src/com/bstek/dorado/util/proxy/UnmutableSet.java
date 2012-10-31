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
package com.bstek.dorado.util.proxy;

import java.util.Set;

/**
 * 不可修改的Set集合。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apr 10, 2008
 */
public class UnmutableSet<E> extends ChildrenSetSupport<E> {
	private static final long serialVersionUID = -1449090065345660683L;

	/**
	 * @param target
	 */
	public UnmutableSet(Set<E> target) {
		super(target);
	}

	@Override
	protected void childAdded(E child) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void childRemoved(E child) {
		throw new UnsupportedOperationException();
	}

}
