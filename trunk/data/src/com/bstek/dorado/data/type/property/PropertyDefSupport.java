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
package com.bstek.dorado.data.type.property;

import com.bstek.dorado.common.Namable;
import com.bstek.dorado.common.ParentAware;
import com.bstek.dorado.data.type.EntityDataType;

/**
 * 属性声明对象的抽象支持类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apirl 20, 2007
 */
public abstract class PropertyDefSupport extends PropertyDef implements
		Namable, ParentAware<EntityDataType> {

	/**
	 * 设置属性的名称。
	 */
	@Override
	public void setName(String name) {
		super.setName(name);
	}
}
