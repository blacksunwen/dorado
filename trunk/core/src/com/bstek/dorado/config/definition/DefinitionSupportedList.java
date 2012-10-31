/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
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
