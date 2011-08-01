package com.bstek.dorado.config.definition;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建最终对象的上下文。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 12, 2008
 */
public class CreationContext {
	private Class<?> defaultImpl;

	// TODO: 可能不必保留此功能，而是交给用户自行解决。因为出现Defintion引用自身是不合理的现象。
	private Map<Definition, Object> instanceStack = new HashMap<Definition, Object>();

	/**
	 * @return the defaultImpl
	 */
	public Class<?> getDefaultImpl() {
		return defaultImpl;
	}

	/**
	 * @param defaultImpl the defaultImpl to set
	 */
	public void setDefaultImpl(Class<?> defaultImpl) {
		this.defaultImpl = defaultImpl;
	}

	public void pushInstanceStack(Definition definition, Object object) {
		instanceStack.put(definition, object);
	}

	public Object findInstance(Definition definition) {
		return instanceStack.get(definition);
	}

	public void removeInstanceStack(Definition definition) {
		instanceStack.remove(definition);
	}
}
