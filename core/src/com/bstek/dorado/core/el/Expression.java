package com.bstek.dorado.core.el;

/**
 * 用于EL表达式的通用接口。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 4, 2007
 */
public interface Expression {

	/**
	 * @return
	 */
	EvaluateMode getEvaluateMode();

	/**
	 * 对表达式进行求值，返回其结果。
	 */
	Object evaluate();
}
