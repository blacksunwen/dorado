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

import java.util.Collection;
import java.util.Set;

/**
 * {@link java.util.Set}代理的抽象支持类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 28, 2007
 */
public abstract class SetProxySupport<E> extends CollectionProxySupport<E>
		implements Set<E> {
	private static final long serialVersionUID = -2833020433482534654L;

	/**
	 * 设置被代理{@link java.util.Set}对象。
	 */
	public SetProxySupport(Collection<E> target) {
		super(target);
	}
}
