package com.bstek.dorado.util.proxy;

import java.util.Set;

/**
 * 不可修改的Set集合。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apr 10, 2008
 */
public class UnmutableSet<E> extends ChildrenSetSupport<E> {

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
