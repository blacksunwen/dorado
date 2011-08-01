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
