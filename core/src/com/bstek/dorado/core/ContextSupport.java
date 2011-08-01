package com.bstek.dorado.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.io.ResourceLoader;

/**
 * 实现了上下文属性的维护和资源读取功能的上下文抽象支持类。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 19, 2007
 * @see com.bstek.dorado.core.io.Resource
 * @see com.bstek.dorado.core.io.ResourceLoader
 */
public abstract class ContextSupport extends Context {
	private Map<String, Object> attributes = new HashMap<String, Object>();

	@Override
	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	@Override
	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}

	@Override
	public void removeAttribute(String key) {
		attributes.remove(key);
	}

	protected abstract ResourceLoader getResourceLoader();

	@Override
	public Resource getResource(String resourceLocation) {
		return getResourceLoader().getResource(resourceLocation);
	}

	@Override
	public Resource[] getResources(String locationPattern) throws IOException {
		return getResourceLoader().getResources(locationPattern);
	}

	@Override
	public ClassLoader getClassLoader() {
		return getResourceLoader().getClassLoader();
	}

}
