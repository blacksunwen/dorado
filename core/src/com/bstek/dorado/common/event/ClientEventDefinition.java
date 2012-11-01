/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.common.event;

import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.Definition;
import com.bstek.dorado.config.definition.DefinitionUtils;
import com.bstek.dorado.core.el.EvaluateMode;
import com.bstek.dorado.core.el.Expression;

/**
 * 客户端事件的配置声明对象。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apr 11, 2008
 */
public class ClientEventDefinition extends Definition {
	private String name;
	private String signature;
	private Object script;

	/**
	 * 返回事件名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置事件名
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	/**
	 * 返回事件体的脚本。
	 */
	public Object getScript() {
		return script;
	}

	/**
	 * 设置事件体的脚本。
	 */
	public void setScript(Object script) {
		this.script = script;
	}

	@Override
	protected Object doCreate(CreationContext context, Object[] constructorArgs)
			throws Exception {
		DefaultClientEvent event = new DefaultClientEvent();
		Object script = DefinitionUtils.getRealValue(this.script, context);
		if (script instanceof Expression
				&& ((Expression) script).getEvaluateMode() == EvaluateMode.onInstantiate) {
			script = ((Expression) script).evaluate();
		}
		event.setSignature(signature);
		event.setScript((String) script);
		return event;
	}

}
