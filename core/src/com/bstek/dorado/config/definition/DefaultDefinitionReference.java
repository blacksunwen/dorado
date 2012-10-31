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

import com.bstek.dorado.core.resource.ResourceManager;
import com.bstek.dorado.core.resource.ResourceManagerUtils;

/**
 * 利用名称指向某配置声明对象的引用。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 7, 2008
 */
public class DefaultDefinitionReference<T extends Definition> implements
		DefinitionReference<T> {
	private static final ResourceManager resourceManager = ResourceManagerUtils
			.get(DefaultDefinitionReference.class);

	private DefinitionManager<T> definitionManager;
	private String name;

	/**
	 * @param definitionManager
	 *            配置声明管理器
	 * @param name
	 *            指向的声明对象在注册时使用的名称
	 */
	public DefaultDefinitionReference(DefinitionManager<T> definitionManager,
			String name) {
		this.definitionManager = definitionManager;
		this.name = name;
	}

	public DefinitionManager<T> getDefinitionManager() {
		return definitionManager;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public T getDefinition() {
		T definition = definitionManager.getDefinition(name);
		if (definition == null) {
			throw new IllegalArgumentException(resourceManager.getString(
					"common/unknownDefinition", name));
		}
		return definition;
	}

}
