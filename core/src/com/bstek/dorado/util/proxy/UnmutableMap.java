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
