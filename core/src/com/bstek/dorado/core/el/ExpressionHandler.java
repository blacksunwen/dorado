package com.bstek.dorado.core.el;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;

/**
 * EL表达式处理器的通用接口。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 4, 2007
 */
public interface ExpressionHandler {
	JexlEngine getJexlEngine() throws Exception;

	/**
	 * 编译一段文本。如果该段文本中包含有EL表达式则返回一个EL表达式的描述对象，否则返回null。
	 * 
	 * @param text
	 *            要编译的文本。
	 * @return EL表达式的描述对象。
	 */
	Expression compile(String text);

	/**
	 * 返回一个Jexl的上下文对象。
	 * <p>
	 * 由于Dorado内部通过apache提供的JEXL通过包来实现EL表达式的解析和求值等操作，
	 * 因此在对EL表达式进行求值前需要首先获得一个有效的Jexl上下文对象。
	 * </p>
	 */
	JexlContext getJexlContext();
}
