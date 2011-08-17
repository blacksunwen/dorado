package com.bstek.dorado.web;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-8-17
 */
public class RequestParameterWrapperMap implements Map<String, String> {
	private HttpServletRequest request;

	public RequestParameterWrapperMap(HttpServletRequest request) {
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

	public String get(Object key) {
		return request.getParameter((String) key);
	}

	public String put(String key, String value) {
		throw new UnsupportedOperationException();
	}

	public String remove(Object key) {

		throw new UnsupportedOperationException();
	}

	public void putAll(Map<? extends String, ? extends String> m) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	public Set<String> keySet() {
		throw new UnsupportedOperationException();
	}

	public Collection<String> values() {
		throw new UnsupportedOperationException();
	}

	public Set<java.util.Map.Entry<String, String>> entrySet() {
		throw new UnsupportedOperationException();
	}

}
