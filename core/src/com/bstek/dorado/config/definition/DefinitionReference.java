package com.bstek.dorado.config.definition;

/**
 * 指向一个具体配置声明对象的引用。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 7, 2008
 */
public interface DefinitionReference<T extends Definition> {
	/**
	 * 返回指向的配置声明对象。
	 */
	T getDefinition();
}
