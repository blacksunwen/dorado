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
package com.bstek.dorado.view.config.definition;

import com.bstek.dorado.config.definition.ObjectDefinition;

/**
 * 布局管理器的配置声明对象。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 28, 2008
 */
public class LayoutDefinition extends ObjectDefinition {
	private String type;

	/**
	 * 返回布局管理器的类型。
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置布局管理器的类型。
	 */
	public void setType(String type) {
		this.type = type;
	}

}
