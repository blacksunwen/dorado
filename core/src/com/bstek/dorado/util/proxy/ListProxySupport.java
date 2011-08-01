package com.bstek.dorado.util.proxy;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * {@link java.util.List}代理的抽象支持类。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 28, 2007
 */
public abstract class ListProxySupport<E> extends CollectionProxySupport<E>
		implements List<E> {

	private List<E> listTarget;

	/**
	 * @param target 被代理{@link java.util.List}对象。
	 */
	public ListProxySupport(Collection<E> target) {
		super(target);
	}

	@Override
	protected void setTarget(Collection<E> target) {
		super.setTarget(target);
		listTarget = (List<E>) target;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return listTarget.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		return listTarget.addAll(index, c);
	}

	public E get(int index) {
		return listTarget.get(index);
	}

	public E set(int index, E element) {
		return listTarget.set(index, element);
	}

	public void add(int index, E element) {
		listTarget.add(index, element);
	}

	public E remove(int index) {
		return listTarget.remove(index);
	}

	public int indexOf(Object o) {
		return listTarget.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return listTarget.lastIndexOf(o);
	}

	public ListIterator<E> listIterator() {
		return listTarget.listIterator();
	}

	public ListIterator<E> listIterator(int index) {
		return listTarget.listIterator(index);
	}

	public List<E> subList(int fromIndex, int toIndex) {
		return listTarget.subList(fromIndex, toIndex);
	}
}
