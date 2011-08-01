package com.bstek.dorado.data.type.property;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.keyvalue.MultiKey;

/**
 * 经过索引的被参照数据。即参照功能将根据约束条件中这些数据查找匹配的数据作为参照属性的值。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jan 2, 2008
 */
public class IndexedLookupData {
	private String[] keyProperties;
	private Map<Object, Object> index = new HashMap<Object, Object>();

	/**
	 * 设置执行参照功能时使用的约束条件。本对象将根据此出传入的约束条件对数据进行索引。
	 */
	public void setConstraints(List<LookupConstraint> constraints) {
		int size = constraints.size();
		String[] keyProperties = new String[size];
		for (int i = 0; i < size; i++) {
			LookupConstraint constraint = constraints.get(i);
			keyProperties[i] = constraint.getLookupKeyProperty();
		}
		this.keyProperties = keyProperties;

		index.clear();
	}

	/**
	 * 以数组的形式返回用于建立索引的属性的数组。
	 */
	public String[] getKeyProperties() {
		return keyProperties;
	}

	/**
	 * 向索引中添加一个实体对象。
	 */
	public void add(Object entity) throws Exception {
		if (entity != null) {
			if (keyProperties.length == 1) {
				index.put(PropertyUtils.getProperty(entity, keyProperties[0]),
						entity);
			} else {
				Object[] keys = new Object[keyProperties.length];
				for (int i = 0; i < keyProperties.length; i++) {
					keys[i] = PropertyUtils.getProperty(entity,
							keyProperties[i]);
				}
				index.put(new MultiKey(keys), entity);
			}
		}
	}

	/**
	 * 向索引中添加一组实体对象。
	 */
	public void addAll(Collection<Object> entities) throws Exception {
		for (Object entity : entities) {
			add(entity);
		}
	}

	/**
	 * 跟据传入的键值到索引中查找匹配的数据。
	 * 
	 * @param key
	 *            键值
	 * @return 查找到的数据
	 */
	public Object find(Object key) {
		return index.get(key);
	}

	/**
	 * 跟据传入的一组键值到索引中查找匹配的数据。
	 * 
	 * @param keys
	 *            键值数组
	 * @return 查找到的数据
	 */
	public Object find(Object[] keys) {
		if (keys.length == 1) {
			return index.get(keys[0]);
		} else {
			return index.get(new MultiKey(keys));
		}
	}
}
