package com.bstek.dorado.data.entity;

import java.util.Collection;
import java.util.Set;

import com.bstek.dorado.data.type.AggregationDataType;

/**
 * {@link java.util.Set}型返回结果的代理。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apirl 15, 2007
 */
public class EntitySet<E> extends EntityCollection<E> implements Set<E> {

	/**
	 * @param target
	 *            被代理的{@link java.util.Set}对象
	 * @param dataType
	 *            相应的数据类型
	 * @throws Exception
	 */
	public EntitySet(Set<E> target, AggregationDataType dataType)
			throws Exception {
		super(target, dataType);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void replaceAllElementWithProxyIfNecessary(
			Collection<? extends E> collection) {
		Set<E> target = (Set<E>) collection;
		Object[] elements = target.toArray();
		target.clear();
		for (Object o : elements) {
			target.add(proxyElementIfNecessary((E) o));
		}
	}

}
