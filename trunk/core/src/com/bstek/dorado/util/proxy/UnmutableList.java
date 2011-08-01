package com.bstek.dorado.util.proxy;

import java.util.List;

/**
 * 不可修改的List集合。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apr 10, 2008
 */
public class UnmutableList<E> extends ChildrenListSupport<E> {

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
