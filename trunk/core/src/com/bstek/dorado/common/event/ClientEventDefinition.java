/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.common.event;

import com.bstek.dorado.config.definition.ObjectDefinition;

/**
 * 客户端事件的配置声明对象。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apr 11, 2008
 */
public class ClientEventDefinition extends ObjectDefinition {

	public ClientEventDefinition() {
		setImplType(DefaultClientEvent.class);
	}

	/**
	 * 返回事件名
	 */
	public String getName() {
		return (String) getProperties().get("name");
	}

	/**
	 * 设置事件名
	 */
	public void setName(String name) {
		getProperties().put("name", name);
	}

	public String getSignature() {
		return (String) getProperties().get("signature");
	}

	public void setSignature(String signature) {
		getProperties().put("signature", signature);
	}

	/**
	 * 返回事件体的脚本。
	 */
	public Object getScript() {
		return getProperties().get("script");
	}

	/**
	 * 设置事件体的脚本。
	 */
	public void setScript(Object script) {
		getProperties().put("script", script);
	}

}
