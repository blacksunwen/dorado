/**
 * 
 */
package com.bstek.dorado.web;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-8-17
 */
public class RequestWrapperMap implements Map<String, Object> {
	private HttpServletRequest request;

	public RequestWrapperMap(HttpServletRequest request) {
		this.request = request;
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
		return request.getAttribute((String) key);
	}

	public Object put(String key, Object value) {
		request.setAttribute((String) key, value);
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
