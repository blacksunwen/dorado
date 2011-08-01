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
