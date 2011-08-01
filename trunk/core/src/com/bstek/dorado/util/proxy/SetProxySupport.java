package com.bstek.dorado.util.proxy;

import java.util.Collection;
import java.util.Set;

/**
 * {@link java.util.Set}代理的抽象支持类。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 28, 2007
 */
public abstract class SetProxySupport<E> extends CollectionProxySupport<E>
		implements Set<E> {

	/**
	 * 设置被代理{@link java.util.Set}对象。
	 */
	public SetProxySupport(Collection<E> target) {
		super(target);
	}

}
