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
	protected Object doCreate(CreationContext context) throws Exception {
		DefaultClientEvent event = new DefaultClientEvent();
		Object script = DefinitionUtils.getRealValue(this.script, context);
		if (script instanceof Expression
				&& ((Expression) script).getEvaluateMode() == EvaluateMode.onInstantiate) {
			script = ((Expression) script).evaluate();
		}
		event.setScript((String) script);
		return event;
	}

}
