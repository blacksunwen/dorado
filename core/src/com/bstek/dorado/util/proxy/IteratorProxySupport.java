package com.bstek.dorado.util.proxy;

import java.util.Iterator;

/**
 * {@link java.util.Iterator}代理的抽象支持类。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 28, 2007
 */
public abstract class IteratorProxySupport<E> implements Iterator<E> {
	private Iterator<E> target;

	/**
	 * @param target 被代理{@link java.util.Iterator}对象。
	 */
	public IteratorProxySupport(Iterator<E> target) {
		this.target = target;
	}

	/**
	 * 返回被代理的{@link java.util.Iterator}对象。
	 */
	public Iterator<E> getTarget() {
		return target;
	}

	public boolean hasNext() {
		return target.hasNext();
	}

	public E next() {
		return target.next();
	}

	public void remove() {
		target.remove();
	}
}
