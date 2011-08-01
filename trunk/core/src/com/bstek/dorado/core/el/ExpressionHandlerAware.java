package com.bstek.dorado.core.el;

/**
 * 可感知EL表达式处理器的对象的接口。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 5, 2007
 */
public interface ExpressionHandlerAware {
	/**
	 * 设置EL表达式的处理器。
	 */
	void setExpressionHandler(ExpressionHandler expressionHandler);
}
