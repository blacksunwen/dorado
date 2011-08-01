package com.bstek.dorado.util.proxy;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于辅助实现子对象管理功能的抽象Map。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Dec 30, 2007
 */
public abstract class ChildrenMapSupport<K, V> extends MapProxySupport<K, V> {

	public ChildrenMapSupport() {
		super(new HashMap<K, V>());
	}

	/**
	 * @param target 被代理的Map。
	 */
	public ChildrenMapSupport(Map<K, V> target) {
		super(target);
	}

	/**
	 * 当有新的子对象被添加到Map时被激活的方法。
	 * @param key 键值
	 * @param child 子对象
	 */
	protected abstract void childAdded(K key, V child);

	/**
	 * 当有子对象从Map被移除时被激活的方法。
	 * @param key 键值
	 * @param child 子对象
	 */
	protected abstract void childRemoved(K key, V child);

	@Override
	public void clear() {
		for (Map.Entry<K, V> entry : entrySet()) {
			childRemoved(entry.getKey(), entry.getValue());
		}
		super.clear();
	}

	@Override
	public V put(K key, V value) {
		V retval = super.put(key, value);
		childAdded(key, value);
		return retval;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> t) {
		super.putAll(t);
		for (Map.Entry<? extends K, ? extends V> entry : t.entrySet()) {
			childAdded(entry.getKey(), entry.getValue());
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public V remove(Object key) {
		V retval = super.remove(key);
		if (retval != null) childRemoved((K) key, retval);
		return retval;
	}

}
