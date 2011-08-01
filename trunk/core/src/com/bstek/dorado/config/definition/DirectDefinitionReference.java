package com.bstek.dorado.config.definition;

/**
 * 通过直接关联指向某配置声明对象的引用。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 21, 2008
 */
public class DirectDefinitionReference<T extends Definition> implements
		DefinitionReference<T> {
	private T definition;

	/**
	 * @param definition 被引用的配置声明对象
	 */
	public DirectDefinitionReference(T definition) {
		this.definition = definition;
	}

	public T getDefinition() {
		return definition;
	}

}
