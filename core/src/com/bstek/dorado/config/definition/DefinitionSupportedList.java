package com.bstek.dorado.config.definition;

import com.bstek.dorado.util.proxy.ChildrenListSupport;

/**
 * 可记录其中是否存在配置声明对象的集合。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-9-29
 */
public class DefinitionSupportedList<E> extends ChildrenListSupport<E> {
	private int definitionCount = 0;

	@Override
	protected void childAdded(E child) {
		if (child instanceof Definition) definitionCount++;
	}

	@Override
	protected void childRemoved(E child) {
		if (child instanceof Definition) definitionCount--;
	}

	/**
	 * 元素中是否存在存在配置声明对象。
	 */
	public boolean hasDefinitions() {
		return definitionCount > 0;
	}
}
