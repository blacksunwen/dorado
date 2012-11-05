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

package com.bstek.dorado.util.proxy;

import java.util.List;

/**
 * 不可修改的List集合。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apr 10, 2008
 */
public class UnmutableList<E> extends ChildrenListSupport<E> {
	private static final long serialVersionUID = 7796147072959964853L;

	/**
	 * @param target
	 */
	public UnmutableList(List<E> target) {
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
