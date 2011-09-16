package com.bstek.dorado.core.el;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.bstek.dorado.core.Context;

public class ContextWrapperMap implements Map<String, Object> {
	private Context context;

	public ContextWrapperMap(Context context) {
		this.context = context;
	}

	public int size() {
		throw new UnsupportedOperationException();
	}

	public boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	public boolean containsKey(Object key) {
		throw new UnsupportedOperationException();
	}

	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}

	public Object get(Object key) {
		return context.getAttribute((String) key);
	}

	public Object put(String key, Object value) {
		context.setAttribute((String) key, value);
		return value;
	}

	public Object remove(Object key) {

		throw new UnsupportedOperationException();
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	public Set<String> keySet() {
		throw new UnsupportedOperationException();
	}

	public Collection<Object> values() {
		throw new UnsupportedOperationException();
	}

	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		throw new UnsupportedOperationException();
	}

}
