package com.bstek.dorado.config.xml;

import java.util.Map;

import com.bstek.dorado.core.el.ExpressionHandler;

/**
 * 可在Spring配置文件中方便的进行配置的XML分派解析器。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 3, 2007
 */
public class ConfigurableDispatchableXmlParser extends DispatchableXmlParser {

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

	/**
	 * 设置所有的属性解析器。
	 * @param propertyParsers 属性解析器的映射集合。其中Map的键值为约束条件，值为属性解析器的实例。
	 */
	public void setPropertyParsers(Map<String, XmlParser> propertyParsers) {
		for (Map.Entry<String, XmlParser> entry : propertyParsers.entrySet()) {
			registerPropertyParser(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * 设置所有的子解析器。
	 * @param subParsers 子解析器的映射集合。其中Map的键值为约束条件，值为子解析器的实例。
	 */
	public void setSubParsers(Map<String, XmlParser> subParsers) {
		for (Map.Entry<String, XmlParser> entry : subParsers.entrySet()) {
			registerSubParser(entry.getKey(), entry.getValue());
		}
	}

}
