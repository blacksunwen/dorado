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
