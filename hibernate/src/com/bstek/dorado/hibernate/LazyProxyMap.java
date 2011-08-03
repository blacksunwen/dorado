package com.bstek.dorado.hibernate;

import java.util.Collections;
import java.util.Map;

import net.sf.cglib.beans.BeanMap;

import com.bstek.dorado.util.proxy.UnmutableMap;

@SuppressWarnings({"unchecked", "rawtypes"})
public class LazyProxyMap extends UnmutableMap<String, Object> {

	private static final LazyProxyMap EMPTY_MAP = new LazyProxyMap(Collections.EMPTY_MAP);

	public LazyProxyMap(Map target) {
		super(target);
	}

	public static LazyProxyMap create(Object bean) {
		if (bean == null) {
			return EMPTY_MAP;
		}

		if (bean instanceof Map) {
			return new LazyProxyMap((Map)bean);
		} else {
			BeanMap beanMap = BeanMap.create(bean);
			return new LazyProxyMap(beanMap);
		}
	}

	@Override
	public Object get(Object key) {
		Object value = super.get(key);
		if (value != null) {
			Class clazz = value.getClass();
			if (!clazz.isPrimitive()) {
				if (!clazz.equals(String.class)) {
					LazyProxyMap valueMap = create(value);
					value = valueMap;
				}
			}
		}
		return value;
	}
}
