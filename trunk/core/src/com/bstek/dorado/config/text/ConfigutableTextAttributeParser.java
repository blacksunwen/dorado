package com.bstek.dorado.config.text;

import com.bstek.dorado.core.el.ExpressionHandler;

/**
 * 可在Spring配置文件中方便的进行配置的字符串属性值的解析器。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apr 2, 2008
 */
public class ConfigutableTextAttributeParser extends TextAttributeParser {
	private ExpressionHandler expressionHandler;

	/**
	 * 设置EL表达式的处理器。
	 */
	public void setExpressionHandler(ExpressionHandler expressionHandler) {
		this.expressionHandler = expressionHandler;
	}

	/**
	 * 返回EL表达式的处理器。
	 */
	@Override
	protected ExpressionHandler getExpressionHandler() {
		return expressionHandler;
	}

}
